package com.example.muchbeer.thebestway;

/**
 * Created by muchbeer on 21/07/2017.
 */

public class ItemPojo {

    private String title, price, location, thumbnailUrl;

    public ItemPojo() {

    }
    public ItemPojo(String title, String price, String location, String thumbnailUrl) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String setPrice(String price) {
        this.price = price;
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String setLocation(String location) {
        this.location = location;
        return location;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return thumbnailUrl;
    }

}