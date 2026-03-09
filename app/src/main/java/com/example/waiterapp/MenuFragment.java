package com.example.waiterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.waiterapp.FBref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener, MenuItemAdapter.OnItemClickListener{
    RecyclerView rvCategories;
    RecyclerView rvMenuItems;
    CategoryAdapter categoryAdapter;
    MenuItemAdapter menuAdapter;

    ArrayList<String> categoryList = new ArrayList<>();

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Input: LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
     * Output: View
     * Function creates a new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_fragment, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCategories = view.findViewById(R.id.rvCategories2);
        rvMenuItems = view.findViewById(R.id.rvMenuItems2);
        categoryList.addAll(Arrays.asList("Starters", "Main Courses", "Desserts"));

        // Set the adapter for rvCategories
        categoryAdapter = new CategoryAdapter(categoryList, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false)); // Set rvCategories to the RecyclerView
        rvCategories.setAdapter(categoryAdapter);

        // Set the adapter for rvMenuItems
        rvMenuItems.setLayoutManager(new LinearLayoutManager(this.getContext())); // Set rvMenuItems to the RecyclerView
        menuAdapter = new MenuItemAdapter(new ArrayList<>(), this);
        rvMenuItems.setAdapter(menuAdapter);

        menuAdapter.setItems(FBref.startersList);
    }

    /**
     * Input: String category, int position
     * Output: Void
     * Function handles the category click
     */
    @Override
    public void onCategoryClick(String category, int position) {
        switch (category) {
            case "Starters":
                menuAdapter.setItems(FBref.startersList);
                break;
            case "Main Courses":
                menuAdapter.setItems(FBref.mainsList);
                break;
            case "Desserts":
                menuAdapter.setItems(FBref.dessertslist);
                break;
        }
    }

    /**
     * Input: MenuItem item
     * Output: Void
     * Function adds an item to the cart
     */
    @Override
    public void onItemClick(MenuItem item) {
        if (FBref.cartItems.containsKey(item))
            FBref.cartItems.put(item, FBref.cartItems.get(item) + 1);
        else
            FBref.cartItems.put(item, 1);
    }
}