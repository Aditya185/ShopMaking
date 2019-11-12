package com.adityaprakash.shopmaking.invoice;



import com.google.firebase.database.Exclude;

public class Items {
    public String ItemName,quantity,price;

    public Items(){

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



    public Items(String ItemName, String quantity, String price) {
        this.ItemName = ItemName;
        this.quantity = quantity;
        this.price = price;

    }
}

