package com.habr.telegrambotmfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class TelegramBotMfaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramBotMfaApplication.class, args);
    }
}
