package com.habr.telegrambotmfa.login;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private final HttpSession httpSession;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.httpSession = request.getSession(true);
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }
}
