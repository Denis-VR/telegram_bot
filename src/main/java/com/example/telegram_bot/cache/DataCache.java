package com.example.telegram_bot.cache;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.handlers.UserProfileData;

public interface DataCache {

	void setUsersCurrentBotState(Long userId, BotState botState);

	BotState getUsersCurrentBotState(Long userId);

	UserProfileData getUserProfileData(Long userId);
}
