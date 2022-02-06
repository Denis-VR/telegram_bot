package com.example.telegram_bot.botapi.handlers.callback_query.fillingprofile;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.botapi.handlers.message.fillingprofile.UserProfileData;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component(value = "fillingProfile")
@Slf4j
@RequiredArgsConstructor
public class FillingProfileHandler implements InputHandler {
	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		CallbackQuery callbackQuery = update.getCallbackQuery();
		String id = callbackQuery.getFrom().getId().toString();
		if (userDataCache.getUsersCurrentBotState(id).equals(BotState.FILLING_PROFILE_CALLBACK)) {
			userDataCache.setUsersCurrentBotState(id, BotState.ASK_NAME);
		}
		return processUsersInput(callbackQuery);
	}

	@Override
	public BotState getHandlerName() {
		return BotState.FILLING_PROFILE_CALLBACK;
	}

	private BotApiMethod<?> processUsersInput(CallbackQuery callbackQuery) {
		String userId = callbackQuery.getFrom().getId().toString();
		String chatId = callbackQuery.getMessage().getChatId().toString();

		UserProfileData profileData = userDataCache.getUserProfileData(userId);
		BotState botState = userDataCache.getUsersCurrentBotState(userId);

		SendMessage replyToUser = messagesService.getReplyMessage(chatId, "error.unknown");

		if (botState.equals(BotState.ASK_NAME)) {
			replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
			userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
		}

		if (botState.equals(BotState.GENDER_M)) {
			profileData.setGender("M");
			userDataCache.saveUserProfileData(userId, profileData);
			replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
			userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
		}
		if (botState.equals(BotState.GENDER_W)) {
			profileData.setGender("W");
			userDataCache.saveUserProfileData(userId, profileData);
			replyToUser = messagesService.getReplyMessage(chatId, "reply.askNumber");
			userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
		}
		return replyToUser;
	}
}
