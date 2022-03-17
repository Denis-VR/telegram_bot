package com.example.telegram_bot.botapi.handlers.fillingprofile;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.model.UserProfileData;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingProfileHandler implements InputHandler {
	private final UserDataCache userDataCache;
	private final ReplyMessagesService messagesService;
	private final KeyboardBuilder keyboardBuilder;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		if (update.hasCallbackQuery()) {
			CallbackQuery callbackQuery = update.getCallbackQuery();
			String id = callbackQuery.getFrom().getId().toString();
			if (userDataCache.getUsersCurrentBotState(id).equals(BotState.FILLING_PROFILE)) {
				userDataCache.setUsersCurrentBotState(id, BotState.ASK_NAME);
			}
			return processUsersInputCallback(callbackQuery);
		} else if (update.hasMessage()) {
			Message message = update.getMessage();
			String id = message.getFrom().getId().toString();
			if (userDataCache.getUsersCurrentBotState(id).equals(BotState.FILLING_PROFILE)) {
				userDataCache.setUsersCurrentBotState(id, BotState.ASK_NAME);
			}
			return processUsersInputMessage(message);
		}

		log.error("Unidentified update: " + update);
		return null;
	}

	@Override
	public BotState getHandlerName() {
		return BotState.FILLING_PROFILE;
	}

	private SendMessage processUsersInputMessage(Message inputMsg) {
		String usersAnswer = inputMsg.getText();
		String userId = inputMsg.getFrom().getId().toString();
		String chatId = inputMsg.getChatId().toString();

		UserProfileData profileData = userDataCache.getUserProfileData(userId);
		BotState botState = userDataCache.getUsersCurrentBotState(userId);

		SendMessage replyToUser = messagesService.getReplyMessage(chatId, "error.unknown");
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
			try {
				profileData.setAge(Integer.parseInt(usersAnswer));
				replyToUser = messagesService.getReplyMessage(chatId, "reply.askGender");
				replyToUser.setReplyMarkup(keyboardBuilder.getGenderButtonsMarkup());
				userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WRONG_GENDER);
			} catch (NumberFormatException e) {
				log.error("Ожидалось число, но ввели не число");
				replyToUser = messagesService.getReplyMessage(chatId, "error.notInt");
			}
		}

		if (botState.equals(BotState.ASK_WRONG_GENDER)) {
			replyToUser = messagesService.getReplyMessage(chatId, "reply.askGenderWrong");
			replyToUser.setReplyMarkup(keyboardBuilder.getGenderButtonsMarkup());
		}

		//далее идет переход в callback_query.fillingprofile.FillingProfileHanlder

		if (botState.equals(BotState.ASK_COLOR)) {
			try {
				replyToUser = messagesService.getReplyMessage(chatId, "reply.askColor");
				profileData.setNumber(Integer.parseInt(usersAnswer));
				userDataCache.setUsersCurrentBotState(userId, BotState.ASK_MOVIE);
			} catch (NumberFormatException e) {
				log.error("Ожидалось число, но ввели не число");
				replyToUser = messagesService.getReplyMessage(chatId, "error.notInt");
			}
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
			replyToUser = messagesService.getReplyMessage(chatId, "reply.profileFilled");
		}

		userDataCache.saveUserProfileData(userId, profileData);
		return replyToUser;
	}

	private BotApiMethod<?> processUsersInputCallback(CallbackQuery callbackQuery) {
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