package com.example.telegram_bot.botapi.handlers;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputMessageHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskDestinyHandler implements InputMessageHandler {

	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;

	@Override
	public SendMessage handle(Message message) {
		return processUsersInput(message);
	}

	@Override
	public BotState getHandlerName() {
		return BotState.ASK_DESTINY;
	}

	private SendMessage processUsersInput(Message inputMsg) {
		long userId = inputMsg.getFrom().getId();
		long chatId = inputMsg.getChatId();

		SendMessage replyToUser = messagesService.getReplyMessage(String.valueOf(chatId), "reply.askDestiny");
		userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
		return replyToUser;
	}
}
