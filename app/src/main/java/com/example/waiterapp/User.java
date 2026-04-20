package com.example.waiterapp;

import com.google.firebase.database.PropertyName;

/**
 * Class represents a user
 *
 * @author Daniel Azar*/
public class User {
    private String FullName;
    private String Email;
    private String UID;
    private String Role;

    /**
     * Constructor for a user
     * @param FullName the name of the user
     * @param Email the email of the user
     * @param UID the UID of the user
     * @param Role the role of the user
     * */
    public User(String FullName, String Email, String UID, String Role) {
        this.FullName = FullName;
        this.Email = Email;
        this.UID = UID;
        this.Role = Role;
    }

    /**
     * returns name
     * @return name*/
    @PropertyName("FullName")
    public String getName() { return FullName;}

    /**
     * returns email
     * @return email*/
    @PropertyName("Email")
    public String getEmail() { return Email;}

    /**
     * returns UID
     * @return UID*/
    @PropertyName("UID")
    public String getUID() { return UID;}

    /**
     * returns role
     * @return role*/
    @PropertyName("Role")
    public String getRole() { return Role;}

    /**
     * sets name
     * @param name the name to set*/
    @PropertyName("FullName")
    public void setName(String name) { this.FullName = name;}

    /**
     * sets email
     * @param email the email to set*/
    @PropertyName("Email")
    public void setEmail(String email) { this.Email = email;}

    /**
     * sets UID
     * @param UID the UID to set*/
    @PropertyName("UID")
    public void setUID(String UID) { this.UID = UID;}

    /**
     * sets role
     * @param role the role to set*/
    @PropertyName("Role")
    public void setRole(String role) { this.Role = role;}
}
