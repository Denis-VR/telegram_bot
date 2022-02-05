package com.example.telegram_bot.botapi;

public enum BotState {

    //fillingprofile
    ASK_NAME,
    ASK_AGE,
    ASK_GENDER,
    ASK_COLOR,
    ASK_NUMBER,
    ASK_MOVIE,
    ASK_SONG,
    FILLING_PROFILE,
    PROFILE_FILLED,

    //askdestiny
    ASK_DESTINY,

    //menu
    SHOW_USER_PROFILE,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU;
}
