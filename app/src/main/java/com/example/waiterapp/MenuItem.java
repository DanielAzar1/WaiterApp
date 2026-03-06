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

    public MenuItem(String Name, String Description, double Price, String Category) {
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.Category = Category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public String getCategory() {return Category;}

    public void setCategory(String category) {this.Category = category;}


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap bitmap) {
        this.image = bitmap;
    }
}
