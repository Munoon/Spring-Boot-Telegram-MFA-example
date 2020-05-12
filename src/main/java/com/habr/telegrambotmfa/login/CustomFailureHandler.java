package com.habr.telegrambotmfa.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.CharEncoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.habr.telegrambotmfa.login.AuthenticationInfo.RequiredMfa.TELEGRAM_MFA;

public class CustomFailureHandler implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        AuthenticationInfo info = new AuthenticationInfo();
        info.setSuccess(false);
        info.setErrorMessage(e.getMessage());

        if (e instanceof RequireTelegramMfaException) {
            info.setRequiredMfas(Collections.singleton(TELEGRAM_MFA));
        }

        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), info);
    }
}
