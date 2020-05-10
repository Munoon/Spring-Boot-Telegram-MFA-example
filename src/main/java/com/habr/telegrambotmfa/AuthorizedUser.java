package com.habr.telegrambotmfa;

import com.habr.telegrambotmfa.models.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public AuthorizedUser(User user) {
        super(user.getUsername(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
