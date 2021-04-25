package org.telegram.bot.scrapingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrapingbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrapingbotApplication.class, args);
	}

}
