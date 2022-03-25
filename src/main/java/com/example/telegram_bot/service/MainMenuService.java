package com.example.telegram_bot.service;

import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainMenuService {

	private final KeyboardBuilder keyboardBuilder;

	public SendMessage getMainMenuMessage(String chatId, String textMessage) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.getMainMenuKeyboard();
		return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
	}


	private SendMessage createMessageWithKeyboard(String chatId, String textMessage, final ReplyKeyboardMarkup replyKeyboardMarkup) {
		final SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(chatId);
		sendMessage.setText(textMessage);
		if (replyKeyboardMarkup != null) {
			sendMessage.setReplyMarkup(replyKeyboardMarkup);
		}
		return sendMessage;
	}
}
