package com.example.black.gotankdriver.model;

import com.google.gson.annotations.SerializedName;

public class Driver {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;
    @SerializedName("api_token")
    private String api_token;
    @SerializedName("avatar")
    private String avatar;

    public Driver() {
    }

    public Driver(int id, String name, String email, String password, String phone, String api_token, String avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.api_token = api_token;
        this.avatar = avatar;
    }

    public Driver(String name, String email, String password, String phone, String api_token, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.api_token = api_token;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
