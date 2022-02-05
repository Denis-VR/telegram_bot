package com.example.telegram_bot.botapi.handlers.askdestiny;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputMessageHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.telegram_bot.utils.KeyboardBuilder.buildButton;
import static com.example.telegram_bot.utils.KeyboardBuilder.buildKeyboardButtonRow;
import static com.example.telegram_bot.utils.KeyboardBuilder.buildRowList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskDestinyHandler implements InputMessageHandler {

    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String userId = inputMsg.getFrom().getId().toString();
        String chatId = inputMsg.getChatId().toString();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askDestiny");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        userDataCache.setUsersCurrentBotState(userId, BotState.FILLING_PROFILE);
        return replyToUser;
    }

    //todo Создать бин для создания клавиатуры. Почитать про InlineKeyboardMarkupBuilder или применить паттерн Builder
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
