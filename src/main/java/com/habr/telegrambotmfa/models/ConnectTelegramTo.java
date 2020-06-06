package com.habr.telegrambotmfa.models;

public class ConnectTelegramTo {
    private String checkString;
    private int userId;

    public ConnectTelegramTo() {
    }

    public String getCheckString() {
        return checkString;
    }

    public void setCheckString(String checkString) {
        this.checkString = checkString;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
