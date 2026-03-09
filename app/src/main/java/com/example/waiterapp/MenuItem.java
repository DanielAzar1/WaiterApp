package com.example.waiterapp;

import android.graphics.Bitmap;

public class MenuItem {
    private String Name;
    private String Description;
    private double Price;
    private Bitmap image;
    private String Category;

    public MenuItem() {
        // Default constructor required for calls to DataSnapshot.getValue(MenuItem.class)
    }

    /**
     * Input: String Name, String Description, double Price, String Category
     * Output: Void
     * Function initializes the menu item
     */
    public MenuItem(String Name, String Description, double Price, String Category) {
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.Category = Category;
    }

    /**
     * Input: Void
     * Output: String
     * Function returns the name of the menu item
     */
    public String getName() {
        return Name;
    }

    /**
     * Input: String name
     * Output: Void
     * Function sets the name of the menu item
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * Input: Void
     * Output: String
     * Function returns the description of the menu item
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Input: String description
     * Output: Void
     * Function sets the description of the menu item
     */
    public void setDescription(String description) {
        this.Description = description;
    }

    /**
     * Input: Void
     * Output: double
     * Function returns the price of the menu item
     */
    public double getPrice() {
        return Price;
    }

    /**
     * Input: double price
     * Output: Void
     * Function sets the price of the menu item
     */
    public void setPrice(double price) {
        this.Price = price;
    }

    /**
     * Input: Void
     * Output: String
     * Function returns the category of the menu item
     */
    public String getCategory() {return Category;}

    /**
     * Input: String category
     * Output: Void
     * Function sets the category of the menu item
     */
    public void setCategory(String category) {this.Category = category;}

    /**
     * Input: Void
     * Output: Bitmap
     * Function returns the image of the menu item
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Input: Bitmap bitmap
     * Output: Void
     * Function sets the image of the menu item
     */
    public void setImage(Bitmap bitmap) {
        this.image = bitmap;
    }
}
