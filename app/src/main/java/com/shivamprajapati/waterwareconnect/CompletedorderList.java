package com.shivamprajapati.waterwareconnect;

public class CompletedorderList {
    private String name,address,phone,date,review;
    private float rating;

    public String getReview() {
        return review;
    }

    void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    String getPhone() {
        return phone;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }
}
