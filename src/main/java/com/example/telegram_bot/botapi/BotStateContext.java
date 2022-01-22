package com.example.telegram_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BotStateContext {
	private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

	public BotStateContext(List<InputMessageHandler> messageHandlers) {
		messageHandlers.forEach(handler -> {
			System.out.println("	Load handler: " + handler.getHandlerName());
			this.messageHandlers.put(handler.getHandlerName(), handler);
		});
	}

	public SendMessage processInputMessage(BotState currentState, Message message) {
		InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
		return currentMessageHandler.handle(message);
	}

	private InputMessageHandler findMessageHandler(BotState currentState) {
		if (isFillingProfileState(currentState)) {
			return messageHandlers.get(BotState.FILLING_PROFILE);
		}
		return messageHandlers.get(currentState);
	}

	private boolean isFillingProfileState(BotState currentState) {
		switch (currentState) {
			case ASK_NAME:
			case ASK_AGE:
			case ASK_GENDER:
			case ASK_NUMBER:
			case ASK_MOVIE:
			case ASK_SONG:
			case ASK_COLOR:
			case FILLING_PROFILE:
			case PROFILE_FILLED:
				return true;
			default:
				return false;
		}
	}
}