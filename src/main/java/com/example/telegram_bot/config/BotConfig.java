package com.example.telegram_bot.config;

import com.example.telegram_bot.MyTelegramBot;
import com.example.telegram_bot.botapi.TelegramFacade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;


@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

	private String webHookPath;
	private String botUsername;
	private String botToken;

	@Bean
	public MyTelegramBot myTelegramBot(TelegramFacade telegramFacade) {
		MyTelegramBot myTelegramBot = new MyTelegramBot(telegramFacade);
		myTelegramBot.setWebHookPath(webHookPath);
		myTelegramBot.setBotUsername(botUsername);
		myTelegramBot.setBotToken(botToken);
		return myTelegramBot;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource
				= new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
