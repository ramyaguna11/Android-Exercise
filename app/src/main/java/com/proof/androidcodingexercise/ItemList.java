package com.proof.androidcodingexercise;

public class ItemList {
    private String heading;
    private String description;
    private String imageUrl;

    public ItemList(String heading, String description, String imageUrl) {
        this.heading = heading;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
