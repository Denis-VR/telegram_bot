package com.example.telegram_bot.botapi;

import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
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
	private final ReplyMessagesService replyMessagesService;

	public SendMessage handleUpdate(Update update) {
		Message message = update.getMessage();
		if (message == null) {
			log.error("Get update without message.");
			return null;
		} else if (message.hasText()) {
			log.info("New message from User: {}, chatId: {}, with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
			return handleInputMessage(message);
		} else {
			log.info("Non-text message received! Message: {}", message);
			return replyMessagesService.getReplyMessage(message.getChatId().toString(), "error.notText");
		}
	}

	private SendMessage handleInputMessage(Message message) {
		String userId = message.getFrom().getId().toString();
		BotState botState = getBotState(message.getText(), userId);
		userDataCache.setUsersCurrentBotState(userId, botState);

		return botStateContext.processInputMessage(botState, message);
	}

	private BotState getBotState(String textMessage, String userId) {
		BotState botState;
		switch (textMessage) {
			case "/start":
				botState = BotState.ASK_DESTINY;
				break;
			case "/go":
				botState = BotState.FILLING_PROFILE;
				break;
//			case "/help": //todo
//				botState = BotState.SHOW_HELP_MENU;
//				break;
//			case "/menu":
//				botState = BotState.SHOW_MAIN_MENU;
//				break;
			default:
				botState = userDataCache.getUsersCurrentBotState(userId);
				break;
		}
		return botState;
	}
}
