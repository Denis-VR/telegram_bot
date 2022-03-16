package com.example.telegram_bot.botapi.handlers.message.errors;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class IsCallbackQueryHandler implements InputHandler {
	private final ReplyMessagesService messagesService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());
		String chatId = update.getMessage().getChatId().toString();
		return  messagesService.getReplyMessage(chatId, "error.notMessage");
	}

	@Override
	public BotState getHandlerName() {
		return BotState.IS_CALLBACK_QUERY;
	}
}
