package com.example.telegram_bot.cache;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.model.UserProfileData;

public interface DataCache {

	void setUsersCurrentBotState(String userId, BotState botState);

	BotState getUsersCurrentBotState(String userId);

	UserProfileData getUserProfileData(String userId);

	void saveUserProfileData(String userId, UserProfileData userProfileData);
}
