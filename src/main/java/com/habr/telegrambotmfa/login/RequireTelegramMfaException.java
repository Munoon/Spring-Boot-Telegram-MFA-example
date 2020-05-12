package com.habr.telegrambotmfa.login;

import org.springframework.security.core.AuthenticationException;

public class RequireTelegramMfaException extends AuthenticationException {
    public RequireTelegramMfaException(String msg) {
        super(msg);
    }
}
