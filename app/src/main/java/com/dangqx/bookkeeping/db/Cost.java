package com.dangqx.bookkeeping.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by dang on 2020-12-15.
 * Time will tell.
 *
 * @description
 */
public class Cost extends LitePalSupport implements Serializable {
    private int id;
    private int money;
    private String category;
    private String date;
    private String description;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
