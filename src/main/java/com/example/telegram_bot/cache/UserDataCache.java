package com.example.telegram_bot.cache;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.handlers.UserProfileData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
	private Map<Long, BotState> userBotStates = new HashMap<>();
	private Map<Long, UserProfileData> usersProfileData = new HashMap<>();

	@Override
	public void setUsersCurrentBotState(Long userId, BotState botState) {
		userBotStates.put(userId, botState);
	}

	@Override
	public BotState getUsersCurrentBotState(Long userId) {
		BotState botState = userBotStates.get(userId);
		if (botState == null) {
			botState = BotState.ASK_DESTINY;
		}
		return botState;
	}

	@Override
	public UserProfileData getUserProfileData(Long userId) {
		UserProfileData userProfileData = usersProfileData.get(userId);
		if (userProfileData == null) {
			userProfileData = new UserProfileData();
		}
		return userProfileData;
	}
}
