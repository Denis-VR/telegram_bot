package com.example.telegram_bot.cache;

import com.example.telegram_bot.enums.BotState;
import com.example.telegram_bot.model.UserProfileData;
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
		if (!botState.equals(BotState.WRONG_HANDLE_TYPE)
				&& !botState.equals(BotState.NOT_SUPPORT)
				&& !botState.equals(BotState.SHOW_MAIN_MENU)
				&& !botState.equals(BotState.NOT_ASK)
				&& !botState.equals(BotState.SHOW_HELP_MENU)
				&& !botState.equals(BotState.SHOW_USER_PROFILE)
				&& !botState.equals(BotState.IGNORE)) {

			log.trace("User with id = {} set botstate: {}", userId, botState);
			userBotStates.put(userId, botState);
		}
	}

	@Override
	public BotState getUsersCurrentBotState(String userId) {
		BotState botState = userBotStates.get(userId);
		if (botState == null) {
			botState = BotState.ASK_DESTINY;
		}
		return botState;
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
