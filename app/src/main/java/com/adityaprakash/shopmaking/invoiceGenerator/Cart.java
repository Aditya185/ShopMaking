package com.adityaprakash.shopmaking.invoiceGenerator;

import com.google.firebase.database.Exclude;

public class Cart {

    public String ItemName,price,quantity;

    public Cart(String ItemName, String quantity, String price) {
        this.ItemName = ItemName;
        this.quantity = quantity;
        this.price = price;
    }
    @Exclude
    public String getItemName() {
        return ItemName;
    }
    @Exclude
    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public String getItemQty() {
        return quantity;
    }

    public void setItemQty(String quantity) {
        this.quantity = quantity;
    }

    public String getItemPrice() {
        return price;
    }

    public void setItemPrice(String price) {
        this.price = price;
    }

    public Cart(){

  }

}
