package com.habr.telegrambotmfa.controllers;

import com.habr.telegrambotmfa.models.User;
import com.habr.telegrambotmfa.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/ajax/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        log.info("Register user {}", user);
        user.setRoles(Set.of(User.Role.ROLE_USER));
        return userService.create(user);
    }
}
