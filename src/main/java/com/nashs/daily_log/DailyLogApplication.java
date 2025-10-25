package com.nashs.daily_log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DailyLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyLogApplication.class, args);
	}

}
