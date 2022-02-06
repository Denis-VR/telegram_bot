package com.example.telegram_bot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
//todo компонент не готов, не продуман и не использует паттерн
public class KeyboardBuilder {

	public static InlineKeyboardButton buildButton(String name, String buttonName) {
		InlineKeyboardButton button = new InlineKeyboardButton();
		button.setText(name);
		button.setCallbackData(buttonName);
		return button;
	}

	public static List<InlineKeyboardButton> buildKeyboardButtonRow(InlineKeyboardButton... buttons) {
		return new ArrayList<>(Arrays.asList(buttons));
	}

	@SafeVarargs
	public static List<List<InlineKeyboardButton>> buildRowList(List<InlineKeyboardButton>... rows) {
		return new ArrayList<>(Arrays.asList(rows));
	}

	public AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
		AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
		answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
		answerCallbackQuery.setShowAlert(alert);
		answerCallbackQuery.setText(text);
		return answerCallbackQuery;
	}

	public InlineKeyboardMarkup getGenderButtonsMarkup() {
		InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
		//todo сделать перевод для языков
		InlineKeyboardButton buttonMan = buildButton("М", "buttonMan");
		InlineKeyboardButton buttonWoman = buildButton("Ж", "buttonWoman");

		List<InlineKeyboardButton> keyboardButtonRow1 = buildKeyboardButtonRow(buttonMan, buttonWoman);

		List<List<InlineKeyboardButton>> rowList = buildRowList(keyboardButtonRow1);

		inlineMarkup.setKeyboard(rowList);
		return inlineMarkup;
	}
}
