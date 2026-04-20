package com.example.waiterapp;

import android.graphics.Bitmap;

/**
 * Class is the class that represents a menu item
 *
 * @author Daniel Azar
 * */
public class MenuItem {
    private String Name;
    private String Description;
    private double Price;
    private Bitmap image;
    private String Category;
    private int TimeToLive;

    /**
     * Empty Constructor
     * */
    public MenuItem() {
        // Default constructor
    }

    /**
     * Function initializes the menu item
     *
     * @param Name the name of the menu item
     * @param Description the description of the menu item
     * @param Price the price of the menu item
     * @param Category the category of the menu item
     * @param TimeToLive the time to live of the menu item
     */
    public MenuItem(String Name, String Description, double Price, String Category, int TimeToLive) {
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.Category = Category;
        this.TimeToLive = TimeToLive;
    }

    /**
     * Function returns the name of the menu item
     * @return name
     */
    public String getName() {
        return Name;
    }

    /**
     * Function sets the name of the menu item
     * @param name the name of the menu item
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * Function returns the description of the menu item
     * @return description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Function sets the description of the menu item
     * @param description the description of the menu item
     */
    public void setDescription(String description) {
        this.Description = description;
    }

    /**
     * Function returns the price of the menu item
     * @return price
     */
    public double getPrice() {
        return Price;
    }

    /**
     * Function sets the price of the menu item
     * @param price the price of the menu item
     */
    public void setPrice(double price) {
        this.Price = price;
    }

    /**
     * Function returns the category of the menu item
     * @return category
     */
    public String getCategory() {return Category;}

    /**
     * Function sets the category of the menu item
     * @param category the category of the menu item
     */
    public void setCategory(String category) {this.Category = category;}

    /**
     * Function returns the image of the menu item
     * @return image
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Function sets the image of the menu item
     * @param bitmap the image of the menu item
     */
    public void setImage(Bitmap bitmap) {
        this.image = bitmap;
    }

    /**
     * Function returns the ttl of the menu item
     * @param TimeToLive the ttl of the menu item
     */
    public void setTimeToLive(int TimeToLive) { this.TimeToLive = TimeToLive; }

    /**
     * Function returns the ttl of the menu item
     * @return ttl
     */
    public int getTimeToLive() { return TimeToLive; }


    /**
     * Function uploads the dish to the database
     *
     * @param type the type of the dish
     */
    public void uploadDish(String type)
    {
        switch (type)
        {
            case "Starters":
                FBref.refStarters.child(Category).child(Name).setValue(this);
                break;
            case "MainCourses":
                FBref.refMains.child(Category).child(Name).setValue(this);
                break;
            case "Desserts":
                FBref.refDesserts.child(Category).child(Name).setValue(this);
                break;
        }
    }
}
