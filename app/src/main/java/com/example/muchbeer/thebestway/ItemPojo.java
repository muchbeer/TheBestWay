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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}