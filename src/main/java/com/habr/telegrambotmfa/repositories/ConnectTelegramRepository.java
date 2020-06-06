package com.habr.telegrambotmfa.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ConnectTelegramRepository {
    private Map<Integer, Long> repository = new HashMap<>(); // user id, chat id

    public void register(int userId, long chatId) {
        repository.put(userId, chatId);
    }

    public Long getChatIdByUserId(int userId) {
        return repository.get(userId);
    }

    public void removeByUserId(int userId) {
        repository.remove(userId);
    }
}
