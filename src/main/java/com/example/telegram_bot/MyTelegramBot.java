package com.example.telegram_bot;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@NoArgsConstructor
@Setter
public class MyTelegramBot extends TelegramWebhookBot {

	private String webHookPath;
	private String botUsername;
	private String botToken;

	public MyTelegramBot(DefaultBotOptions options) { //todo read for options
		super(options);
	}

	@Override
	public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
		if (update.getMessage() != null && update.getMessage().hasText()) {
			String chat_id = update.getMessage().getChatId().toString();

			try {
				execute(new SendMessage(chat_id, "Hi " + update.getMessage().getText()));
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		return null;
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
