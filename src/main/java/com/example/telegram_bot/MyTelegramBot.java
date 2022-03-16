package com.example.telegram_bot;

import com.example.telegram_bot.botapi.TelegramFacade;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

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

	@SneakyThrows
	public void sendPhoto(String chatId, String imageCaption, String imagePath) {
		File file = ResourceUtils.getFile("classpath:" + imagePath);
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setPhoto(new InputFile().setMedia(file));
		sendPhoto.setChatId(chatId);
		sendPhoto.setCaption(imageCaption);
		execute(sendPhoto);
	}

	@SneakyThrows
	public void sendDocument(String chatId, String caption, File sendFile) {
		SendDocument sendDocument = new SendDocument();
		sendDocument.setChatId(chatId);
		sendDocument.setCaption(caption);
		sendDocument.setDocument(new InputFile().setMedia(sendFile));
		execute(sendDocument);
	}
}
