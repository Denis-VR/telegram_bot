package com.example.telegram_bot.botapi.handlers.callback_query.errors;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class IsMessageHandler implements InputHandler {
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());
		String textReply = messagesService.getReplyText("error.notCallback");
		return keyboardBuilder.sendAnswerCallbackQuery(textReply, true, update.getCallbackQuery());
	}

	@Override
	public BotState getHandlerName() {
		return BotState.IS_MESSAGE;
	}
}
