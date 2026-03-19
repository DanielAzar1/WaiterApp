package com.example.waiterapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.waiterapp.User;

public class FBref {
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();

    // DB References
    public static DatabaseReference refDesserts = FBDB.getReference("MenuItems/Desserts");
    public static DatabaseReference refStarters = FBDB.getReference("MenuItems/Starters");
    public static DatabaseReference refMains = FBDB.getReference("MenuItems/MainCourses");
    public static DatabaseReference refOrders = FBDB.getReference("Orders/Active");
    public static DatabaseReference refManagers = FBDB.getReference("Users/Managers");
    public static DatabaseReference refWaiters = FBDB.getReference("Users/Waiters");

    // Storage Refrences
    public static StorageReference storageRefDesserts = storageRef.child("images/FoodItems/Desserts");
    public static StorageReference storageRefStarters = storageRef.child("images/FoodItems/Starters");
    public static StorageReference storageRefMains = storageRef.child("images/FoodItems/MainCourses");

    // Lists here for preload
    public static final long MAX_SIZE  = 1024 * 1024;
    public static ArrayList<MenuItem> dessertslist = new ArrayList<>();
    public static ArrayList<MenuItem> mainsList = new ArrayList<>();
    public static ArrayList<MenuItem> startersList = new ArrayList<>();

    public static ArrayList<User> userList = new ArrayList<>();

    public static Map<MenuItem, Integer> cartItems = new HashMap<>();
    public static User currentUser = null;
}