package com.example.backend.dto;

public class FeedbackStats {

    private long totalFeedback;
    private double averageRating;
    private long fiveStar;
    private long fourStar;
    private long threeStar;
    private long twoStar;
    private long oneStar;
    public FeedbackStats() {
    }

    public long getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(long totalFeedback) {
        this.totalFeedback = totalFeedback;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(long fiveStar) {
        this.fiveStar = fiveStar;
    }

    public long getFourStar() {
        return fourStar;
    }

    public void setFourStar(long fourStar) {
        this.fourStar = fourStar;
    }

    public long getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(long threeStar) {
        this.threeStar = threeStar;
    }

    public long getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(long twoStar) {
        this.twoStar = twoStar;
    }

    public long getOneStar() {
        return oneStar;
    }

    public void setOneStar(long oneStar) {
        this.oneStar = oneStar;
    }
}