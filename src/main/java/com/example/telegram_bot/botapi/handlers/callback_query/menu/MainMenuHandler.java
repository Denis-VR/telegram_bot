package com.example.telegram_bot.botapi.handlers.callback_query.menu;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.service.MainMenuService;
import com.example.telegram_bot.service.ReplyMessagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class MainMenuHandler implements InputHandler {
	private final ReplyMessagesService messagesService;
	private final MainMenuService mainMenuService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());
		String chatId = update.getCallbackQuery().getFrom().getId().toString();
		return mainMenuService.getMainMenuMessage(chatId, messagesService.getReplyText("reply.showMainMenu"));
	}

	@Override
	public BotState getHandlerName() {
		return BotState.SHOW_MAIN_MENU;
	}

}