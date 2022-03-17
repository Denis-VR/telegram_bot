package com.example.telegram_bot.botapi.handlers.errors;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class WrongTypeHandler implements InputHandler {
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		if (update.hasMessage()) {
			String chatId = update.getMessage().getChatId().toString();
			return messagesService.getReplyMessage(chatId, "error.notMessage");
		}

		if (update.hasMessage()) {
			String textReply = messagesService.getReplyText("error.notCallback");
			return keyboardBuilder.sendAnswerCallbackQuery(textReply, true, update.getCallbackQuery());
		}

		log.error("Unidentified update: " + update);
		return null;
	}

	@Override
	public BotState getHandlerName() {
		return BotState.WRONG_HANDLE_TYPE;
	}
}
