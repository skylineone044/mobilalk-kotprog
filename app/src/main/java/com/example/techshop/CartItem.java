package com.example.techshop;

public class CartItem {
    private String userID;
    private String itemTitle;
    private int amount;

    public CartItem() {
    }

    public CartItem(String userID, String itemTitle, int amount) {
        this.userID = userID;
        this.itemTitle = itemTitle;
        this.amount = amount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
