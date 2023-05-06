package com.example.thechshop;

public class ShopItem {
    private String title;
    private String description;
    private int price_ft;
    private int imageResource;

    public ShopItem() {
    }

    public ShopItem(String title, String description, int price_ft, int imageResource) {
        this.title = title;
        this.description = description;
        this.price_ft = price_ft;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice_ft() {
        return price_ft;
    }

    public void setPrice_ft(int price_ft) {
        this.price_ft = price_ft;
    }

    public int getImageResource() {
        return imageResource;
    }

}
