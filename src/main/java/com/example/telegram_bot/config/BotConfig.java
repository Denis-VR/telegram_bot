package com.example.telegram_bot.config;

import com.example.telegram_bot.MyTelegramBot;
import com.example.telegram_bot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@Configuration
@Setter
@Getter
@Slf4j
@ConfigurationProperties(prefix = "telegrambot")
@RequiredArgsConstructor
public class BotConfig {

    private final RestTemplate restTemplate;

    private String webHookPath;
    private String botUsername;
    private String botToken;

    @Bean
    public MyTelegramBot myTelegramBot(TelegramFacade telegramFacade) {
        setWebHook();
        MyTelegramBot myTelegramBot = new MyTelegramBot(telegramFacade);
        myTelegramBot.setWebHookPath(webHookPath);
        myTelegramBot.setBotUsername(botUsername);
        myTelegramBot.setBotToken(botToken);
        return myTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public void setWebHook() {
        String url = "https://api.telegram.org/bot1866938538:AAFcp-Zlz3obNiZM6q-WlHsMsC6hCS_78Xc/setWebhook?url=" + webHookPath;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("WebHook is set with result: {}", response.getStatusCode());
    }
}
