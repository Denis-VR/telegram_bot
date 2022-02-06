package com.example.telegram_bot.botapi.handlers.message.askdestiny;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.telegram_bot.utils.KeyboardBuilder.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskDestinyHandler implements InputHandler {

	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		Message message = update.getMessage();
		String userId = message.getFrom().getId().toString();
		String chatId = message.getChatId().toString();

		SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askDestiny");
		replyToUser.setReplyMarkup(getInlineMessageButtons());

		userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
		return replyToUser;
	}

	@Override
	public BotState getHandlerName() {
		return BotState.ASK_DESTINY;
	}

	//todo использовать KeyboardBuilder
	private InlineKeyboardMarkup getInlineMessageButtons() {
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


}
