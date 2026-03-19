package com.example.waiterapp;

import com.google.firebase.database.PropertyName;

public class User {
    private String FullName;
    private String Email;
    private String UID;
    private String Role;

    public User(String FullName, String Email, String UID, String Role) {
        this.FullName = FullName;
        this.Email = Email;
        this.UID = UID;
        this.Role = Role;
    }

    @PropertyName("FullName")
    public String getName() { return FullName;}

    @PropertyName("Email")
    public String getEmail() { return Email;}

    @PropertyName("UID")
    public String getUID() { return UID;}

    @PropertyName("Role")
    public String getRole() { return Role;}


    @PropertyName("FullName")
    public void setName(String name) { this.FullName = name;}

    @PropertyName("Email")
    public void setEmail(String email) { this.Email = email;}

    @PropertyName("UID")
    public void setUID(String UID) { this.UID = UID;}

    @PropertyName("Role")
    public void setRole(String role) { this.Role = role;}
}
