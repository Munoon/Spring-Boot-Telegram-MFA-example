package com.habr.telegrambotmfa.login;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private final HttpServletRequest request;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
