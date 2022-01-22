package com.example.telegram_bot.botapi.handlers;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputMessageHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        String id = message.getFrom().getId().toString();
        if (userDataCache.getUsersCurrentBotState(id).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(id, BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        String userId = inputMsg.getFrom().getId().toString();
        String chatId = inputMsg.getChatId().toString();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        //todo use pattern (like Abstract Factory)
        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)) {
            profileData.setName(usersAnswer);
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askGender");
            profileData.setAge(Integer.parseInt(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }

        if (botState.equals(BotState.ASK_NUMBER)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
            profileData.setGender(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askColor");
            profileData.setNumber(Integer.parseInt(usersAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MOVIE);
        }

        if (botState.equals(BotState.ASK_MOVIE)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askMovie");
            profileData.setColor(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SONG);
        }

        if (botState.equals(BotState.ASK_SONG)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askSong");
            profileData.setMovie(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setSong(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DESTINY);
            replyToUser = new SendMessage(chatId, String.format("%s %s", "Данные по вашей анкете", profileData));
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }


}