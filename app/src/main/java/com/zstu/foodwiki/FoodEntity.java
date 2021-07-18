package com.zstu.foodwiki;

import java.io.Serializable;

public class FoodEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pk_food_id;
    private int fk_user_id;
    private int fk_file_id;

    private String title;
    private String contentdetails;
    private String selfcomment;
    private String phonenumber;
    private int likes;
    private int stars;
    private int shares;
    private int reads;

    private String bloger;
    private byte[] foodbin;


    public FoodEntity(int pk_food_id, int fk_user_id, int fk_file_id, String title, String contentdetails, String selfcomment, String phonenumber, int likes, int stars, int shares, int reads) {
        this.pk_food_id = pk_food_id;
        this.fk_user_id = fk_user_id;
        this.fk_file_id = fk_file_id;
        this.title = title;
        this.contentdetails = contentdetails;
        this.selfcomment = selfcomment;
        this.phonenumber = phonenumber;
        this.likes = likes;
        this.stars = stars;
        this.shares = shares;
        this.reads = reads;
    }

    public int getPk_food_id() {
        return pk_food_id;
    }

    public void setPk_food_id(int pk_food_id) {
        this.pk_food_id = pk_food_id;
    }

    public int getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(int fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public int getFk_file_id() {
        return fk_file_id;
    }

    public void setFk_file_id(int fk_file_id) {
        this.fk_file_id = fk_file_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentdetails() {
        return contentdetails;
    }

    public void setContentdetails(String contentdetails) {
        this.contentdetails = contentdetails;
    }

    public String getSelfcomment() {
        return selfcomment;
    }

    public void setSelfcomment(String selfcomment) {
        this.selfcomment = selfcomment;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getReads() {
        return reads;
    }

    public void setReads(int reads) {
        this.reads = reads;
    }

    public String getBloger() {
        return bloger;
    }

    public void setBloger(String bloger) {
        this.bloger = bloger;
    }

    public byte[] getFoodbin() {
        return foodbin;
    }

    public void setFoodbin(byte[] foodbin) {
        this.foodbin = foodbin;
    }

}
