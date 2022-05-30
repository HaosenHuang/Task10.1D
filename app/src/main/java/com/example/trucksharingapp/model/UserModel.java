package com.example.trucksharingapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * com.example.trucksharingapp.model
 * 2022/5/4
 *
 * @author Created on {DATE}
 * Major Function：<b></b>
 * @author mender，Modified Date Modify Content:
 */
@Entity
public class UserModel {
    @PrimaryKey
    @NonNull
    public String account = "";
    public String userId;
    public String password;
    public String fullName;
    public String phoneString;
    public String headPortraitView = "";

    public String getFullName() {
        return fullName;
    }

    public UserModel setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhoneString() {
        return phoneString;
    }

    public UserModel setPhoneString(String phoneString) {
        this.phoneString = phoneString;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserModel setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public UserModel setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHeadPortraitView() {
        return headPortraitView;
    }

    public UserModel setHeadPortraitView(String headPortraitView) {
        this.headPortraitView = headPortraitView;
        return this;
    }

}
