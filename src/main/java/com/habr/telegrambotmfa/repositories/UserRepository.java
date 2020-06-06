package com.habr.telegrambotmfa.repositories;

import com.habr.telegrambotmfa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.telegramChatId = ?2 WHERE u.id = ?1")
    void connectBot(int userId, Long chatId);
}
