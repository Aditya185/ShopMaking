package com.adityaprakash.shopmaking.DayWise;

public class Customer {

    public String name,phone;

    public Customer(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customer(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
}
