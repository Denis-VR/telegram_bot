package com.example.telegram_bot;

import com.example.telegram_bot.botapi.TelegramFacade;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@RequiredArgsConstructor
public class MyTelegramBot extends TelegramWebhookBot {

	private String webHookPath;
	private String botUsername;
	private String botToken;

	private final TelegramFacade telegramFacade;

	@Override
	public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
		return telegramFacade.handleUpdate(update);
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public String getBotPath() {
		return webHookPath;
	}
}
