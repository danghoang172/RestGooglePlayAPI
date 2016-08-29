//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.play.rest;

import com.google.play.rest.GooglePlayVerticle;
import io.vertx.core.Vertx;

public class MainServer {
    private Vertx vertx;

    public MainServer() {
    }

    public static void main(String[] arg) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GooglePlayVerticle.class.getName());
    }
}
