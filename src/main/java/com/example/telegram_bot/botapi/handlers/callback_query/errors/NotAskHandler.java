package com.example.telegram_bot.botapi.handlers.callback_query.errors;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotAskHandler implements InputHandler {
	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		CallbackQuery callbackQuery = update.getCallbackQuery();
		String userId = callbackQuery.getFrom().getId().toString();
		String textReply = messagesService.getReplyText("reply.notAsk");
		AnswerCallbackQuery answer = keyboardBuilder.sendAnswerCallbackQuery(textReply, false, callbackQuery);
		userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_HELP_MENU); //todo ?

		return answer;
	}

	@Override
	public BotState getHandlerName() {
		return BotState.NOT_ASK;
	}
}
