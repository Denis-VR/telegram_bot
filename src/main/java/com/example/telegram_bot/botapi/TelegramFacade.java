package com.example.telegram_bot.botapi;

import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramFacade {
	private final BotStateContext botStateContext;
	private final UserDataCache userDataCache;
	private final ReplyMessagesService replyMessagesService;

	public BotApiMethod<?> handleUpdate(Update update) {
		if (update.hasCallbackQuery()) {
			log.info("New CallbackQuery from User: {}", update.getCallbackQuery());
			return processCallbackQuery(update);
		}
		if (update.hasMessage()) {
			return handleMessage(update);
		}

		log.error("Unknown update: {}", update);
		return null;
	}

	private BotApiMethod<?> handleMessage(Update update) {
		Message message = update.getMessage();
		if (message.hasText()) {
			log.info("New message from User: {}, chatId: {}, with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
			return processMessage(update);
		} else {
			log.info("Non-text message received! Message: {}", message);
			return replyMessagesService.getReplyMessage(message.getChatId().toString(), "error.notText");
		}
	}

	private BotApiMethod<?> processMessage(Update update) {
		String userId = update.getMessage().getFrom().getId().toString();
		BotState botState = getBotStateTextMessage(update.getMessage().getText(), userId);
		userDataCache.setUsersCurrentBotState(userId, botState);
		return botStateContext.processInputTextMessage(botState, update);
	}

	private BotApiMethod<?> processCallbackQuery(Update update) {
		String userId = update.getCallbackQuery().getFrom().getId().toString();
		BotState botState = getBotStateCallbackQueryData(update.getCallbackQuery().getData());
		userDataCache.setUsersCurrentBotState(userId, botState);
		return botStateContext.processInputTextMessage(botState, update);
	}

	private BotState getBotStateTextMessage(String textMessage, String userId) {
		BotState botState;
		switch (textMessage) {
			case "/start":
				botState = BotState.ASK_DESTINY;
				break;
			case "Получить предсказание":
				botState = BotState.FILLING_PROFILE;
				break;
			case "Моя анкета":
				botState = BotState.SHOW_USER_PROFILE;
				break;
			case "Помощь":
				botState = BotState.SHOW_HELP_MENU;
				break;
			default:
				botState = userDataCache.getUsersCurrentBotState(userId);
		}
		return botState;
	}

	private BotState getBotStateCallbackQueryData(String data) {
		BotState botState;
		switch (data) {
			case "buttonYes":
				botState = BotState.FILLING_PROFILE_CALLBACK;
				break;
			case "buttonNo":
				botState = BotState.NOT_ASK;
				break;
			case "buttonIwillThink":
				botState = BotState.NOT_SUPPORT;
				break;
			case "buttonMan":
				botState = BotState.GENDER_M;
				break;
			case "buttonWoman":
				botState = BotState.GENDER_W;
				break;
			case "buttonOther":
				botState = BotState.SHOW_MAIN_MENU;
				break;
			default:
				botState = BotState.SHOW_MAIN_MENU;
		}
		return botState;
	}
}
