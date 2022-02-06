package com.example.telegram_bot.botapi.handlers.message.menu;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.botapi.handlers.message.fillingprofile.UserProfileData;
import com.example.telegram_bot.cache.UserDataCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShowProfileHandler implements InputHandler {
	private final UserDataCache userDataCache;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		Message message = update.getMessage();
		String userId = message.getFrom().getId().toString();
		UserProfileData profileData = userDataCache.getUserProfileData(userId);

		userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
		return new SendMessage(message.getChatId().toString(), String.format("%s%n -------------------%nИмя: %s%nВозраст: %d%nПол: %s%nЛюбимая цифра: %d%n" +
						"Цвет: %s%nФильм: %s%nПесня: %s%n", "Данные по вашей анкете", profileData.getName(), profileData.getAge(), profileData.getGender(), profileData.getNumber(),
				profileData.getColor(), profileData.getMovie(), profileData.getSong()));
	}

	@Override
	public BotState getHandlerName() {
		return BotState.SHOW_USER_PROFILE;
	}
}
