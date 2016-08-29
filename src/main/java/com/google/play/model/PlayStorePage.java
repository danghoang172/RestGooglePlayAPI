//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.play.model;

import com.google.play.model.App;
import com.google.play.model.Rating;
import java.util.Map;

public class PlayStorePage {
    private Rating rating;
    private Map<Integer, String> imageDetails;
    private App app;

    public PlayStorePage(Rating rating, Map<Integer, String> imageDetails, App app) {
        this.rating = rating;
        this.imageDetails = imageDetails;
        this.app = app;
    }

    public Rating getRating() {
        return this.rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Map<Integer, String> getImageDetails() {
        return this.imageDetails;
    }

    public void setImageDetails(Map<Integer, String> imageDetails) {
        this.imageDetails = imageDetails;
    }

    public App getApp() {
        return this.app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public PlayStorePage() {
    }
}
