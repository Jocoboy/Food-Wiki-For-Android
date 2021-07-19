package com.zstu.foodwiki;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**********Base Var*************/
    private int pk_user_id;
    private int fk_figure_id;

    private String name;
    private String remark;
    private int follows;
    private int followers;
    private int readers;

    /**********Extend Var***************/
    private byte[] figurebin;


    public UserEntity(int pk_user_id, int fk_figure_id, String name, String remark, int follows, int followers, int readers) {
        this.pk_user_id = pk_user_id;
        this.fk_figure_id = fk_figure_id;
        this.name = name;
        this.remark = remark;
        this.follows = follows;
        this.followers = followers;
        this.readers = readers;
    }

    public int getPk_user_id() {
        return pk_user_id;
    }

    public void setPk_user_id(int pk_user_id) {
        this.pk_user_id = pk_user_id;
    }

    public int getFk_figure_id() {
        return fk_figure_id;
    }

    public void setFk_figure_id(int fk_figure_id) {
        this.fk_figure_id = fk_figure_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getReaders() {
        return readers;
    }

    public void setReaders(int readers) {
        this.readers = readers;
    }

    public byte[] getFigurebin() {
        return figurebin;
    }

    public void setFigurebin(byte[] figurebin) {
        this.figurebin = figurebin;
    }






}
