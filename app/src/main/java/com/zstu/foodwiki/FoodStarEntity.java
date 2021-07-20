package com.zstu.foodwiki;

import java.io.Serializable;

public class FoodStarEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**********Base Var*************/
    private int pk_star_id;
    private int fk_user_id;
    private int fk_food_id;


    /**********Extend Var***************/
    private UserEntity userEntity;
    private FoodEntity foodEntity;


    public FoodStarEntity(int pk_star_id, int fk_user_id, int fk_food_id) {
        this.pk_star_id = pk_star_id;
        this.fk_user_id = fk_user_id;
        this.fk_food_id = fk_food_id;
    }

    public int getPk_star_id() {
        return pk_star_id;
    }

    public void setPk_star_id(int pk_star_id) {
        this.pk_star_id = pk_star_id;
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

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public FoodEntity getFoodEntity() {
        return foodEntity;
    }

    public void setFoodEntity(FoodEntity foodEntity) {
        this.foodEntity = foodEntity;
    }
}
