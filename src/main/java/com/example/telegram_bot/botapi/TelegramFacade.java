package com.example.telegram_bot.botapi;

import com.example.telegram_bot.MyTelegramBot;
import com.example.telegram_bot.cache.UserDataCache;
import com.example.telegram_bot.enums.BotState;
import com.example.telegram_bot.model.UserProfileData;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
		if (update.hasMessage()) {
			Message message = update.getMessage();
			if (message.hasText()) {
				log.info("New message from User: {}, chatId: {}, with text: {}", message.getFrom().getUserName(), message.getChatId(), message.getText());
				String userId = message.getFrom().getId().toString();
				BotState botState = getBotStateTextMessage(message.getText(), userId, message.getChatId().toString());
				userDataCache.setUsersCurrentBotState(userId, botState);
				return botStateContext.processInputTextMessage(botState, update);
			} else {
				log.info("Non-text message received! Message: {}", message);
				return replyMessagesService.getReplyMessage(message.getChatId().toString(), "error.notText");
			}
		} else if (update.hasCallbackQuery()) {
			String userId = update.getCallbackQuery().getFrom().getId().toString();
			log.info("New CallbackQuery from User: {}, userId:{}", update.getCallbackQuery().getFrom().getUserName(), userId);
			BotState botState = getBotStateCallbackQueryData(update.getCallbackQuery().getData(), userId, update.getCallbackQuery().getFrom().getId().toString());
			userDataCache.setUsersCurrentBotState(userId, botState);
			return botStateContext.processInputTextMessage(botState, update);
		}

		log.error("Unknown update: {}", update);
		return null;
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
			case "Скачать анкету":
				myTelegramBot.sendDocument(chatId, replyMessagesService.getReplyText("reply.getProfile"), getUsersProfile(userId));
				botState = BotState.IGNORE;
				break;
			default:
				botState = userDataCache.getUsersCurrentBotState(userId);
		}
		return botState;
	}

	private BotState getBotStateCallbackQueryData(String data, String userId, String chatId) {
		BotState botState;
		switch (data) {
			case "buttonYes":
				botState = BotState.FILLING_PROFILE;
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
				botState = userDataCache.getUsersCurrentBotState(userId);
		}
		return botState;
	}

	@SneakyThrows
	private File getUsersProfile(String userId) {
		UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
		File profileFile = ResourceUtils.getFile("classpath:static/docs/users_profile.txt");

		try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
			 BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(userProfileData.toString());
		}
		return profileFile;
	}
}
