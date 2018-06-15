package com.example.admins.snaphotel.Model;

/**
 * Created by Admins on 4/6/2018.
 */

public class ReviewModel {
    public UserModel userModel;
    public String date;
    public String review;
    public float ratting;


    public ReviewModel(UserModel userModel, String date, String review, float ratting) {
        this.userModel = userModel;
        this.date = date;
        this.review = review;
        this.ratting = ratting;


    }

    public float getRatting() {
        return ratting;
    }

    public ReviewModel() {

    }

    public UserModel getUserModel() {
        return userModel;
    }

    public String getDate() {
        return date;
    }

    public String getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "ReviewModel{" +
                "userName='" +
                ", date='" + date + '\'' +
                ", review='" + review + '\'' +
                ", ratting=" + ratting +
                '}';
    }
}


