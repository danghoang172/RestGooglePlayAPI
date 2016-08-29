package com.google.play.model;

public class Token {

    private String accessToken;
    private int expireTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public Token() {

    }

    public Token(String accessToken, int expireTime) {

        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }
}
