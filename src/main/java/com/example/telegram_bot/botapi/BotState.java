package com.example.telegram_bot.botapi;

import lombok.Getter;

public enum BotState {

	//fillingprofile
	FILLING_PROFILE(BotType.MESSAGE),
	ASK_NAME(BotType.MESSAGE_AND_CALLBACK),
	ASK_AGE(BotType.MESSAGE),
	ASK_GENDER(BotType.MESSAGE),
	ASK_WRONG_GENDER(BotType.MESSAGE),
	ASK_COLOR(BotType.MESSAGE),
	ASK_NUMBER(BotType.MESSAGE),
	ASK_MOVIE(BotType.MESSAGE),
	ASK_SONG(BotType.MESSAGE),
	PROFILE_FILLED(BotType.MESSAGE),
	FILLING_PROFILE_CALLBACK(BotType.CALLBACK_QUERY),
	GENDER_M(BotType.CALLBACK_QUERY),
	GENDER_W(BotType.CALLBACK_QUERY),

	//askdestiny
	ASK_DESTINY(BotType.MESSAGE),

	//menu
	SHOW_USER_PROFILE(BotType.MESSAGE),
	SHOW_MAIN_MENU(BotType.CALLBACK_QUERY),
	SHOW_HELP_MENU(BotType.MESSAGE),

	//errors
	NOT_SUPPORT(BotType.CALLBACK_QUERY),
	NOT_ASK(BotType.CALLBACK_QUERY),
	IS_CALLBACK_QUERY(BotType.MESSAGE),
	IS_MESSAGE(BotType.CALLBACK_QUERY);

	@Getter
	private final BotType botType;

	BotState(BotType type) {
		botType = type;
	}
}
