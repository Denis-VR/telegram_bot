package com.example.telegram_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BotStateContext {
	private final Map<BotState, InputHandler> messageHandlers = new HashMap<>();

	public BotStateContext(List<InputHandler> messageHandlers) {
		messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
	}

	public BotApiMethod<?> processInputTextMessage(BotState currentState, Update update) {
		InputHandler currentMessageHandler = findMessageHandler(currentState);
		return currentMessageHandler.handle(update);
	}

	private InputHandler findMessageHandler(BotState currentState) {
		if (isFillingProfileState(currentState)) return messageHandlers.get(BotState.FILLING_PROFILE);
		return messageHandlers.get(currentState);
	}

	private boolean isFillingProfileState(BotState currentState) {
		switch (currentState) {
			case ASK_NAME:
			case ASK_AGE:
			case ASK_GENDER:
			case ASK_WRONG_GENDER:
			case ASK_NUMBER:
			case ASK_MOVIE:
			case ASK_SONG:
			case ASK_COLOR:
			case GENDER_M:
			case GENDER_W:
			case FILLING_PROFILE:
			case PROFILE_FILLED:
				return true;
			default:
				return false;
		}
	}

}
