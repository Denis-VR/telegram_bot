package com.example.telegram_bot.enums;

public enum BotState {

	//fillingprofile
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
	NOT_ASK,
	IGNORE,
	WRONG_HANDLE_TYPE;

}
