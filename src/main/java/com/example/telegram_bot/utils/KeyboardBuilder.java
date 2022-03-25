package com.example.telegram_bot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
//todo компонент не готов, не продуман и не соответсвует паттерну
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

	public InlineKeyboardMarkup getInlineMessageButtons() {
		InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
		//todo сделать перевод для языков
		InlineKeyboardButton buttonYes = buildButton("Да", "buttonYes");
		InlineKeyboardButton buttonNo = buildButton("Нет", "buttonNo");
		InlineKeyboardButton buttonThink = buildButton("Я подумаю", "buttonIwillThink");
		InlineKeyboardButton buttonOther = buildButton("Другое", "buttonOther");

		List<InlineKeyboardButton> keyboardButtonRow1 = buildKeyboardButtonRow(buttonYes, buttonNo);
		List<InlineKeyboardButton> keyboardButtonRow2 = buildKeyboardButtonRow(buttonThink, buttonOther);

		List<List<InlineKeyboardButton>> rowList = buildRowList(keyboardButtonRow1, keyboardButtonRow2);

		inlineMarkup.setKeyboard(rowList);
		return inlineMarkup;
	}

	public ReplyKeyboardMarkup getMainMenuKeyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow row1 = new KeyboardRow();
		KeyboardRow row2 = new KeyboardRow();
		KeyboardRow row3 = new KeyboardRow();
		row1.add(new KeyboardButton("Получить предсказание"));
		row2.add(new KeyboardButton("Моя анкета"));
		row2.add(new KeyboardButton("Скачать анкету"));
		row3.add(new KeyboardButton("Помощь"));
		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		replyKeyboardMarkup.setKeyboard(keyboard);
		return replyKeyboardMarkup;
	}
}
