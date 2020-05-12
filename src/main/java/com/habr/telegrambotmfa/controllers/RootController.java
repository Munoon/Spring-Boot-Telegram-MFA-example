package com.habr.telegrambotmfa.controllers;

import com.habr.telegrambotmfa.AuthorizedUser;
import com.habr.telegrambotmfa.TelegramBot;
import com.habr.telegrambotmfa.models.User;
import com.habr.telegrambotmfa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Controller
public class RootController {
    private UserService userService;
    private TelegramBot telegramBot;

    @Autowired
    public RootController(UserService userService, TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) throws TelegramApiException {
        AuthorizedUser auth = (AuthorizedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = auth.getUser().getId();
        User user = userService.getById(userId);

        model.addAttribute("useTelegram", user.getTelegramChatId() != null);
        if (user.getTelegramChatId() != null) {
            model.addAttribute("telegram", telegramBot.getUsernameByTelegramChatId(user.getTelegramChatId()));
        }

        return "index";
    }
}
