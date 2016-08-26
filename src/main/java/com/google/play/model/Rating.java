package com.google.play.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Rating {

    private long fiveStar;
    private long fourStar;
    private long threeStar;
    private long twoStar;
    private long oneStar;

    @JsonProperty("average_rating")
    private float averageRating;


    public Rating() {

    }

    public Rating(long fiveStar, long fourStar, long threeStar, long twoStar, long oneStar, float averageRating) {
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStar = threeStar;
        this.twoStar = twoStar;
        this.oneStar = oneStar;
        this.averageRating = averageRating;
    }

    public long calculateTotalRating(){
        long totalRating = fiveStar+ fourStar+ threeStar
                + twoStar+oneStar;
        return totalRating;
    }

    public long getFiveStar() {
        return fiveStar;
    }

    public long getFourStar() {
        return fourStar;
    }

    public long getThreeStar() {
        return threeStar;
    }

    public long getTwoStar() {
        return twoStar;
    }

    public long getOneStar() {
        return oneStar;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(long averageRating) {
        this.averageRating = averageRating;
    }

    public void setFiveStar(long fiveStar) {
        this.fiveStar = fiveStar;
    }

    public void setFourStar(long fourStar) {
        this.fourStar = fourStar;
    }

    public void setThreeStar(long threeStar) {
        this.threeStar = threeStar;
    }

    public void setTwoStar(long twoStar) {
        this.twoStar = twoStar;
    }

    public void setOneStar(long oneStar) {
        this.oneStar = oneStar;
    }
}
