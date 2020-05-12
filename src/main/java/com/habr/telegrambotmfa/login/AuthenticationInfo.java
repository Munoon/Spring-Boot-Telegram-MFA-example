package com.habr.telegrambotmfa.login;

import java.util.Set;

public class AuthenticationInfo {
    private boolean success;
    private String redirectUrl;
    private String errorMessage;
    private Set<RequiredMfa> requiredMfas;

    public enum RequiredMfa {
        TELEGRAM_MFA
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Set<RequiredMfa> getRequiredMfas() {
        return requiredMfas;
    }

    public void setRequiredMfas(Set<RequiredMfa> requiredMfas) {
        this.requiredMfas = requiredMfas;
    }
}
