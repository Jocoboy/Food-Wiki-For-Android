package com.zstu.foodwiki;

import java.io.Serializable;

public class CommentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**********Base Var*************/
    private int pk_id;
    private int fk_user_id;
    private int fk_food_id;
    private int fk_targer_user_id;
    private String comment;
    private String dt;


    /**********Extend Var***************/
    //private byte[] figure_bin;
    //private byte[] food_bin;
    private UserEntity commentUser;
    private FoodEntity commentFood;

    public CommentEntity(int pk_id, int fk_user_id, int fk_food_id, int fk_targer_user_id, String comment, String dt) {
        this.pk_id = pk_id;
        this.fk_user_id = fk_user_id;
        this.fk_food_id = fk_food_id;
        this.fk_targer_user_id = fk_targer_user_id;
        this.comment = comment;
        this.dt = dt;
    }

    public int getPk_id() {
        return pk_id;
    }

    public void setPk_id(int pk_id) {
        this.pk_id = pk_id;
    }

    public int getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(int fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public int getFk_food_id() {
        return fk_food_id;
    }

    public void setFk_food_id(int fk_food_id) {
        this.fk_food_id = fk_food_id;
    }

    public int getFk_targer_user_id() {
        return fk_targer_user_id;
    }

    public void setFk_targer_user_id(int fk_targer_user_id) {
        this.fk_targer_user_id = fk_targer_user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

   /*public byte[] getFigure_bin() {
        return figure_bin;
    }*/

   /* public void setFigure_bin(byte[] figure_bin) {
        this.figure_bin = figure_bin;
    }*/

   /* public byte[] getFood_bin() {
        return food_bin;
    }*/

   /* public void setFood_bin(byte[] food_bin) {
        this.food_bin = food_bin;
    }*/

    public UserEntity getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserEntity commentUser) {
        this.commentUser = commentUser;
    }

    public FoodEntity getCommentFood() {
        return commentFood;
    }

    public void setCommentFood(FoodEntity commentFood) {
        this.commentFood = commentFood;
    }
}
