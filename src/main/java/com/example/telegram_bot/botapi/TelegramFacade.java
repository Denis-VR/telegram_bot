package com.example.telegram_bot.botapi;

import com.example.telegram_bot.MyTelegramBot;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
	private final BotStateContext botStateContext;
	private final UserDataCache userDataCache;
	private final ReplyMessagesService replyMessagesService;
	private final MyTelegramBot myTelegramBot;

	public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache,
						  ReplyMessagesService replyMessagesService, @Lazy MyTelegramBot myTelegramBot) {
		this.botStateContext = botStateContext;
		this.userDataCache = userDataCache;
		this.replyMessagesService = replyMessagesService;
		this.myTelegramBot = myTelegramBot;
	}

	public BotApiMethod<?> handleUpdate(Update update) {
		if (update.hasCallbackQuery()) {
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
		BotState botState = getBotStateTextMessage(update.getMessage().getText(), userId, update.getMessage().getChatId().toString());
		if (!botState.equals(BotState.IS_CALLBACK_QUERY)) {
			userDataCache.setUsersCurrentBotState(userId, botState);
		}
		return botStateContext.processInputTextMessage(botState, update);
	}

	private BotApiMethod<?> processCallbackQuery(Update update) {
		String userId = update.getCallbackQuery().getFrom().getId().toString();
		log.info("New CallbackQuery from User: {}, userId:{}", update.getCallbackQuery().getFrom().getUserName(), userId);
		BotState botState = getBotStateCallbackQueryData(update.getCallbackQuery().getData(), userId);
		if (!botState.equals(BotState.IS_MESSAGE) && !botState.equals(BotState.NOT_SUPPORT)) {
			userDataCache.setUsersCurrentBotState(userId, botState);
		}
		return botStateContext.processInputTextMessage(botState, update);
	}

	private BotState getBotStateTextMessage(String textMessage, String userId, String chatId) {
		BotState botState;
		switch (textMessage) {
			case "/start":
				botState = BotState.ASK_DESTINY;
				myTelegramBot.sendPhoto(chatId, replyMessagesService.getReplyText("reply.hello"), "static/images/logo.jpg");
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
				botState = userDataCache.getUsersCurrentBotStateForMessage(userId);
		}
		return botState;
	}

	private BotState getBotStateCallbackQueryData(String data, String userId) {
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
				botState = userDataCache.getUsersCurrentBotStateForCallback(userId);
		}
		return botState;
	}
}
