package com.example.telegram_bot.sheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class PingScheduled {

	@Value("${schedule.pingURL}")
	private String pingURL;

	@Scheduled(fixedDelayString = "${schedule.pingDelay}")
	public void ping() {
		try {
			URL url = new URL(pingURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());
			connection.disconnect();
		} catch (IOException e) {
			log.error("Ping FAILED");
			e.printStackTrace();
		}
	}
}
