package com.habr.telegrambotmfa.services;

import com.habr.telegrambotmfa.TelegramBot;
import com.habr.telegrambotmfa.repositories.ConnectTelegramRepository;
import com.habr.telegrambotmfa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramService {
    private UserRepository userRepository;
    private ConnectTelegramRepository connectTelegramRepository;
    private TelegramBot telegramBot;

    @Autowired
    public TelegramService(UserRepository userRepository, ConnectTelegramRepository connectTelegramRepository, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.connectTelegramRepository = connectTelegramRepository;
        this.telegramBot = telegramBot;
    }

    public long connectBot(int userId, int telegramUserId) {
        long chatId = connectTelegramRepository.getChatIdByUserId(telegramUserId);
        userRepository.connectBot(userId, chatId);
        connectTelegramRepository.removeByUserId(telegramUserId);
        return chatId;
    }

    public void sendMessage(String text, long chatId) throws TelegramApiException {
        var message = new SendMessage()
                .setText(text)
                .setChatId(chatId);
        telegramBot.execute(message);
    }
}
