package com.example.bookworld.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import com.google.gson.annotations.SerializedName;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    @SerializedName("objectId")
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private boolean loginStatus;
    private String sessionToken;

    @Ignore
    public User() {
    }

    @Ignore
    public User(@NonNull String id,String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public User(@NonNull String id, String username, String password, String firstName, String lastName, String email,
                String gender, boolean loginStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.loginStatus = loginStatus;
    }

    @Ignore
    public User(String username, String password, String firstName, String lastName, String email,
                String gender, boolean loginStatus) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.loginStatus = loginStatus;
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void changeStatus() {
        this.loginStatus = !loginStatus;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", loginStatus=" + loginStatus +
                '}';
    }
}
