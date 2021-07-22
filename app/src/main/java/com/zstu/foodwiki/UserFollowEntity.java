package com.zstu.foodwiki;

import java.io.Serializable;

public class UserFollowEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**********Base Var*************/
    private int pk_userfollow_id;
    private int fk_user_id;
    private int fk_follow_id;

    private boolean isCancelFollow;

    /**********Extend Var***************/
    private UserEntity userEntity;

    public UserFollowEntity(int pk_userfollow_id, int fk_user_id, int fk_follow_id,boolean isCancelFollow) {
        this.pk_userfollow_id = pk_userfollow_id;
        this.fk_user_id = fk_user_id;
        this.fk_follow_id = fk_follow_id;
        this.isCancelFollow = isCancelFollow;
    }

    public int getPk_userfollow_id() {
        return pk_userfollow_id;
    }

    public void setPk_userfollow_id(int pk_userfollow_id) {
        this.pk_userfollow_id = pk_userfollow_id;
    }

    public int getFk_user_id() {
        return fk_user_id;
    }

    public void setFk_user_id(int fk_user_id) {
        this.fk_user_id = fk_user_id;
    }

    public int getFk_follow_id() {
        return fk_follow_id;
    }

    public void setFk_follow_id(int fk_follow_id) {
        this.fk_follow_id = fk_follow_id;
    }

    public boolean getIsCancelFollow() {
        return isCancelFollow;
    }

    public void setIsCancelFollow(boolean cancelFollow) {
        isCancelFollow = cancelFollow;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
