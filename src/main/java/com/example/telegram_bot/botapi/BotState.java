package com.example.telegram_bot.botapi;

public enum BotState {

	//todo улучшить enum, разделив на категории
	//fillingprofile message
	FILLING_PROFILE,
	ASK_NAME,
	ASK_AGE,
	ASK_GENDER,
	ASK_WRONG_GENDER,
	ASK_COLOR,
	ASK_NUMBER,
	ASK_MOVIE,
	ASK_SONG,
	PROFILE_FILLED,
	//fillingprofile callback
	FILLING_PROFILE_CALLBACK,
	GENDER_M,
	GENDER_W,

	//askdestiny
	ASK_DESTINY,

	//menu
	SHOW_USER_PROFILE,
	SHOW_MAIN_MENU,
	SHOW_HELP_MENU,

	//errors
	NOT_SUPPORT,
	NOT_ASK
}
