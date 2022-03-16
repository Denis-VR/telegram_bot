package com.example.telegram_bot.cache;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.BotType;
import com.example.telegram_bot.botapi.handlers.message.fillingprofile.UserProfileData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
//todo add noSql (redis/mongodb)
public class UserDataCache implements DataCache {
	private final Map<String, BotState> userBotStates = new HashMap<>();
	private final Map<String, UserProfileData> usersProfileData = new HashMap<>();

	@Override
	public void setUsersCurrentBotState(String userId, BotState botState) {
		log.trace("User with id = {} set botstate: {}", userId, botState);
		userBotStates.put(userId, botState);
	}

	@Override
	@Deprecated //Может вызывать ошибку!
	public BotState getUsersCurrentBotState(String userId) {
		BotState botState = userBotStates.get(userId);
		if (botState == null) {
			botState = BotState.ASK_DESTINY;
		}
		return botState;
	}

	@Override
	public BotState getUsersCurrentBotStateForMessage(String userId) {
		BotState botState = userBotStates.get(userId);
		if (botState == null) {
			botState = BotState.ASK_DESTINY;
		} else if (!isMessage(botState)) {
			log.warn("getUsersCurrentBotStateForMessage() did not find state: {} for Message", botState);
			if (isCallback(botState)) {
				botState = BotState.IS_CALLBACK_QUERY;
			} else {
				log.error("There is no handler for state: {}", botState);
				return null; //todo throw exception (need global ExceptionHandler)
			}
		}
		return botState;
	}

	@Override
	public BotState getUsersCurrentBotStateForCallback(String userId) {
		BotState botState = userBotStates.get(userId);
		if (botState == null) {
			botState = BotState.ASK_DESTINY;
		} else if (!isCallback(botState)) {
			log.warn("getUsersCurrentBotStateForCakkback() did not find state: {} for Callback", botState);
			if (isMessage(botState)) {
				botState = BotState.IS_MESSAGE;
			} else {
				log.error("There is no handler for state: {}", botState);
				return null; //todo throw exception (need global ExceptionHandler)
			}
		}
		return botState;
	}

	private boolean isMessage(BotState botState) {
		return botState.getBotType().equals(BotType.MESSAGE) || botState.getBotType().equals(BotType.MESSAGE_AND_CALLBACK);
	}

	private boolean isCallback(BotState botState) {
		return botState.getBotType().equals(BotType.CALLBACK_QUERY) || botState.getBotType().equals(BotType.MESSAGE_AND_CALLBACK);
	}

	@Override
	public UserProfileData getUserProfileData(String userId) {
		UserProfileData userProfileData = usersProfileData.get(userId);
		if (userProfileData == null) {
			userProfileData = new UserProfileData();
		}
		return userProfileData;
	}

	@Override
	public void saveUserProfileData(String userId, UserProfileData userProfileData) {
		usersProfileData.put(userId, userProfileData);
	}
}
