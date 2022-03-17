package com.example.telegram_bot.botapi.handlers.errors;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
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
public class NotSupportHandler implements InputHandler {
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName()); //todo перенести в аспект?

		CallbackQuery callbackQuery = update.getCallbackQuery();
		String textReply = messagesService.getReplyText("error.notSupport");
		return keyboardBuilder.sendAnswerCallbackQuery(textReply, true, callbackQuery);
	}

	@Override
	public BotState getHandlerName() {
		return BotState.NOT_SUPPORT;
	}
}
