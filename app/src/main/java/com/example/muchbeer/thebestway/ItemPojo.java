package com.example.muchbeer.thebestway;

/**
 * Created by muchbeer on 21/07/2017.
 */

public class ItemPojo {

    private String itemName, itemPrice, itemLocation;

    public ItemPojo() {
    }

    public ItemPojo(String itemName, String itemPrice, String itemLocation) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }
}