package com.zstu.foodwiki;

import java.io.Serializable;

public class FoodLikeEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**********Base Var*************/
    private int pk_like_id;
    private int fk_user_id;
    private int fk_food_id;


    /**********Extend Var***************/
    private UserEntity userEntity;
    private FoodEntity foodEntity;

    public FoodLikeEntity(int pk_like_id, int fk_user_id, int fk_food_id) {
        this.pk_like_id = pk_like_id;
        this.fk_user_id = fk_user_id;
        this.fk_food_id = fk_food_id;
    }



    public int getPk_like_id() {
        return pk_like_id;
    }

    public void setPk_like_id(int pk_like_id) {
        this.pk_like_id = pk_like_id;
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
