//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.play.rest;

import com.akdeniz.googleplaycrawler.GooglePlayAPI;
import com.google.play.model.Token;
import com.google.play.model.User;
import com.google.play.rest.PlayStore;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Authentication {
    private User user;
    private String LOGGED_TOKEN_FILE = "LoggedTokenFile.txt";
    private String SEPARATE_TOKEN = "&";
    private RoutingContext context;

    public Authentication(User user) {
        this.user = user;
    }

    public Authentication(RoutingContext context) {
        this.context = context;
    }

    Token signIn() throws Exception {
        Token accessToken;
        if(this.getTokenFromFile(this.user) != null) {
            accessToken = new Token(this.getTokenFromFile(this.user), 3600);
        } else {
            GooglePlayAPI userAccount = new GooglePlayAPI(this.user.getEmail(), this.user.getPassword(), this.user.getAndroidID());
            userAccount.checkin();
            userAccount.login();
            accessToken = (new PlayStore(userAccount)).getToken();
            this.logInfo(accessToken.getAccessToken());
        }

        return accessToken;
    }

    private void logInfo(String token) throws Exception {
        FileWriter fw = new FileWriter(this.LOGGED_TOKEN_FILE, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.write(token);
        out.write(this.SEPARATE_TOKEN);
        out.write(this.user.toString());
        out.println();
        out.close();
    }

    PlayStore authenticateRequest() throws Exception {
        String token = this.context.request().getHeader("Authorization");
        String tokenQ = this.context.request().getParam("token");
        if(!this.isValidatedToken(token) && !this.isValidatedToken(tokenQ)) {
            return null;
        } else {
            String correctToken = this.chooseToken(token, tokenQ);
            User usr = this.getUserFromFile(correctToken);
            GooglePlayAPI gg = new GooglePlayAPI(usr.getEmail(), usr.getPassword(), usr.getAndroidID());
            gg.checkin();
            gg.login(correctToken);
            PlayStore userPlay = new PlayStore(gg);
            if(userPlay.getToken() == null) {
                userPlay.getApi().checkin();
                userPlay.getApi().login();
            }

            return userPlay;
        }
    }

    private boolean isValidatedToken(String token) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(this.LOGGED_TOKEN_FILE));
        if(token != null) {
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                if(line.contains(token)) {
                    br.close();
                    return true;
                }
            }

            br.close();
            return false;
        } else {
            return false;
        }
    }

    private String chooseToken(String token1, String token2) throws Exception {
        return this.isValidatedToken(token1)?token1:token2;
    }

    private User getUserFromFile(String token) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(this.LOGGED_TOKEN_FILE));

        for(String line = br.readLine(); line != null; line = br.readLine()) {
            if(line.contains(token)) {
                String[] parts = line.split(this.SEPARATE_TOKEN);
                return (User)Json.decodeValue(parts[1], User.class);
            }
        }

        br.close();
        return null;
    }

    private String getTokenFromFile(User user) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(this.LOGGED_TOKEN_FILE));

        for(String line = br.readLine(); line != null; line = br.readLine()) {
            if(line.contains(user.toString())) {
                String[] parts = line.split(this.SEPARATE_TOKEN);
                return parts[0];
            }
        }

        br.close();
        return null;
    }
}
