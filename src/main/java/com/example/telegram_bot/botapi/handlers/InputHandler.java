package com.example.telegram_bot.botapi.handlers;

import com.example.telegram_bot.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface InputHandler {
	BotApiMethod<?> handle(Update update);

	BotState getHandlerName();
}
