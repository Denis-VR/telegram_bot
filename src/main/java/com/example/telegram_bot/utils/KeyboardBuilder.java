package com.example.telegram_bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
