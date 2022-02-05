package com.example.telegram_bot.botapi;

import com.example.telegram_bot.botapi.handlers.fillingprofile.UserProfileData;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.MainMenuService;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final ReplyMessagesService replyMessagesService;
    private final MainMenuService mainMenuService;

    public BotApiMethod<?> handleUpdate(Update update) {
        log.trace("New update: {}", update);
        if (update.hasCallbackQuery()) {
            return processCallbackQuery(update.getCallbackQuery());
        }
        return handleMessage(update.getMessage());
    }

    private SendMessage handleMessage(Message message) {
        if (message == null) {
            log.error("Get update without message.");
            return null;
        } else if (message.hasText()) {
            log.info("New message from User: {}, chatId: {}, with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
            return processTextMessage(message);
        } else {
            log.info("Non-text message received! Message: {}", message);
            return replyMessagesService.getReplyMessage(message.getChatId().toString(), "error.notText");
        }
    }

    private SendMessage processTextMessage(Message message) {
        String userId = message.getFrom().getId().toString();
        BotState botState = getBotState(message.getText(), userId);
        userDataCache.setUsersCurrentBotState(userId, botState);
        return botStateContext.processInputMessage(botState, message);
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        String chatId = buttonQuery.getMessage().getChatId().toString();
        String userId = buttonQuery.getFrom().getId().toString();
        log.info("Process callbackQuery from User: {}, userId: {}, with data: {}", buttonQuery.getFrom().getUserName(), userId, buttonQuery.getData());

        BotApiMethod<?> callbackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");

        //From Destiny chose buttons
        if (buttonQuery.getData().equals("buttonYes")) {
            callbackAnswer = new SendMessage(chatId, "Как тебя зовут?");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callbackAnswer = sendAnswerCallbackQuery("Возвращайся, когда будешь готов", false, buttonQuery);
        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callbackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);
        }

        //From Gender choose buttons
        if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("M");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callbackAnswer = new SendMessage(chatId, "Твоя любимая цифра");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callbackAnswer = new SendMessage(chatId, "Твоя любимая цифра");
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return callbackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    private BotState getBotState(String textMessage, String userId) {
        BotState botState;
        switch (textMessage) {
            case "/start":
                botState = BotState.ASK_DESTINY;
                break;
            case "Получить предсказание":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Моя анкета":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }
        return botState;
    }
}
