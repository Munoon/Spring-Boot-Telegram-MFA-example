package com.habr.telegrambotmfa.controllers;

import com.habr.telegrambotmfa.AuthorizedUser;
import com.habr.telegrambotmfa.models.ConnectTelegramTo;
import com.habr.telegrambotmfa.models.User;
import com.habr.telegrambotmfa.services.TelegramService;
import com.habr.telegrambotmfa.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

@RestController
@RequestMapping("/ajax/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private TelegramService telegramService;

    public UserController(UserService userService, TelegramService telegramService) {
        this.userService = userService;
        this.telegramService = telegramService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        log.info("Register user {}", user);
        user.setRoles(Set.of(User.Role.ROLE_USER));
        return userService.create(user);
    }

    @PostMapping("/telegram")
    public void connectTelegram(@RequestBody ConnectTelegramTo connectTelegram,
                                @AuthenticationPrincipal AuthorizedUser authUser) throws TelegramApiException {
        long chatId = telegramService.connectBot(authUser.getUserId(), connectTelegram.getUserId());

        String userName = authUser.getUser().getName();
        String welcomeMessage = String.format("Добро пожаловать, %s!\nВы успешно подключили аккаунт!", userName);
        telegramService.sendMessage(welcomeMessage, chatId);
    }
}
