package com.google.play.model;
import com.google.gson.Gson;

public class User {

    private String email;
    private String password;
    private String androidID;

    public User(String email, String password, String androidID) {
        this.email = email;
        this.password = password;
        this.androidID = androidID;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAndroidID() {
        return androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }

    public String toString(){
        User user = new User(this.email,this.password, this.androidID);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return json;
    }
}