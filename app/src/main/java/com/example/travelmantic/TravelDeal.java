package com.example.travelmantic;

public class TravelDeal {
    public String id;
    public String title;
    public String price;
    public String description;
    public TravelDeal(){}
    public TravelDeal(String title, String price, String description){
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
