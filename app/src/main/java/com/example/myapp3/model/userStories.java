package com.example.myapp3.model;

public class userStories {
    private String image;
    private long storyAt;


    public userStories(String image, long storyAt) {
        this.image = image;
        this.storyAt = storyAt;
    }

    public userStories() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }
}
