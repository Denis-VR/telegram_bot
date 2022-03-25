package com.example.telegram_bot.botapi.handlers.errors;

import com.example.telegram_bot.botapi.handlers.InputHandler;
import com.example.telegram_bot.enums.BotState;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class IgnoreHandler implements InputHandler {

	private final ReplyMessagesService messagesService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		String chatId = update.getMessage().getChatId().toString();
		return new SendMessage(chatId, "");
	}

	@Override
	public BotState getHandlerName() {
		return BotState.IGNORE;
	}
}
