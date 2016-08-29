package com.google.play.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class App {

    private String title;
    private String creator;
    private List <String> categories;
    private String description;

    @JsonProperty("total_rating")
    private long totalRating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getCategory() {
        return categories;
    }

    public void setCategory(List<String> category) {
        this.categories = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public App(String title, String creator, List<String> categories, String description, long totalRating) {

        this.title = title;
        this.creator = creator;
        this.categories = categories;
        this.description = description;
        this.totalRating = totalRating;
    }

    public App() {
    }
}
