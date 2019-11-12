package com.adityaprakash.shopmaking;

import com.google.firebase.database.Exclude;

public class Item {
    public String ItemName,quantity,price,location;

    public Item(){

    }

    @Exclude
    public String getItemName() {
        return ItemName;
    }

    @Exclude
    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public Item(String ItemName, String quantity, String price, String location) {
        this.ItemName = ItemName;
        this.quantity = quantity;
        this.price = price;
        this.location = location;
    }
}
