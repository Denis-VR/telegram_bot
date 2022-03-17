package com.example.telegram_bot.botapi.handlers.askdestiny;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskDestinyHandler implements InputHandler {

	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		Message message = update.getMessage();
		String userId = message.getFrom().getId().toString();
		String chatId = message.getChatId().toString();

		SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askDestiny");
		replyToUser.setReplyMarkup(keyboardBuilder.getInlineMessageButtons());

		userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
		return replyToUser;
	}

	@Override
	public BotState getHandlerName() {
		return BotState.ASK_DESTINY;
	}

}
