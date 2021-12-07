package com.example.telegram_bot.config;

import com.example.telegram_bot.MyTelegramBot;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

	private String webHookPath;
	private String botUsername;
	private String botToken;

	@Bean
	public MyTelegramBot myTelegramBot() {
		MyTelegramBot myTelegramBot = new MyTelegramBot();
		myTelegramBot.setWebHookPath(webHookPath);
		myTelegramBot.setBotUsername(botUsername);
		myTelegramBot.setBotToken(botToken);
		return myTelegramBot;
	}

}
