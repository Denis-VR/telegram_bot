package com.example.telegram_bot.controller;

import com.example.telegram_bot.MyTelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebHookController {
	private final MyTelegramBot telegramBot;

	@PostMapping("/")
	public BotApiMethod<?> cnUpdateReceived(@RequestBody Update update) {
		return telegramBot.onWebhookUpdateReceived(update);
	}
}
