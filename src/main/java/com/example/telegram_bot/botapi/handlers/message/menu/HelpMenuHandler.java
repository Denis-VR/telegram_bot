package com.example.telegram_bot.botapi.handlers.message.menu;

import com.example.telegram_bot.botapi.BotState;
import com.example.telegram_bot.botapi.InputHandler;
import com.example.telegram_bot.service.MainMenuService;
import com.example.telegram_bot.service.ReplyMessagesService;
import com.example.telegram_bot.utils.Emojis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class HelpMenuHandler implements InputHandler {
	private final MainMenuService mainMenuService;
	private final ReplyMessagesService messagesService;

	@Override
	public BotApiMethod<?> handle(Update update) {
		log.trace("Call {}", getHandlerName());

		Message message = update.getMessage();
		String chatId = message.getChatId().toString();
		String answer = messagesService.getReplyText("reply.showHelpMenu", Emojis.SCREAM);
		return mainMenuService.getMainMenuMessage(chatId, answer);
	}

	@Override
	public BotState getHandlerName() {
		return BotState.SHOW_HELP_MENU;
	}
}