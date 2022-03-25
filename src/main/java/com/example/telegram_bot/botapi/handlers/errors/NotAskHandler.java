package com.example.telegram_bot.botapi.handlers.errors;

import com.example.telegram_bot.enums.BotState;
import com.example.telegram_bot.botapi.handlers.InputHandler;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotAskHandler implements InputHandler {
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		CallbackQuery callbackQuery = update.getCallbackQuery();
		String textReply = messagesService.getReplyText("reply.notAsk");
		return keyboardBuilder.sendAnswerCallbackQuery(textReply, false, callbackQuery);
	}

	@Override
	public BotState getHandlerName() {
		return BotState.NOT_ASK;
	}
}
