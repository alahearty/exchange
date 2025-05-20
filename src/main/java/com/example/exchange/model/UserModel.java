package com.example.exchange.model;

import com.example.exchange.entity.UserEntity;

public class UserModel {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public UserModel(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserModel from(UserEntity user) {
        return new UserModel(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
