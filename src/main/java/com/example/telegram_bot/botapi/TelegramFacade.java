package com.example.telegram_bot.botapi;

import com.example.telegram_bot.cache.UserDataCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramFacade {
	private final BotStateContext botStateContext;
	private final UserDataCache userDataCache;

	public SendMessage handleUpdate(Update update) {
		SendMessage replyMessage = null;
		Message message = update.getMessage();
		if (update.getMessage() != null && update.getMessage().hasText()) {
			log.info("New message from User: {}, chatId: {}, with text: {}",
					message.getFrom().getUserName(), message.getChatId(), message.getText());
			replyMessage = handleInputMessage(message);
			//replyMessage = new SendMessage(String.valueOf(message.getChatId()), message.getText());
		}
		return replyMessage;
	}

	private SendMessage handleInputMessage(Message message) {
		String inputMsg = message.getText();
		Long userId = message.getFrom().getId();
		BotState botState;
		SendMessage replyMessage;

		switch (inputMsg) {
			case "/start":
				botState = BotState.ASK_DESTINY;
				break;
			case "Получить предсказание":
				botState = BotState.FILLING_PROFILE;
				break;
			case "Помощь":
				botState = BotState.SHOW_HELP_MENU;
				break;
			default:
				botState = userDataCache.getUsersCurrentBotState(userId);
				break;
		}

		userDataCache.setUsersCurrentBotState(userId, botState);
		return botStateContext.processInputMessage(botState, message);
	}
}
