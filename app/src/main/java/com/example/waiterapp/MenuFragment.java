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

/**
 * Class is a fragment that displays the menu
 *
 * @author Daniel Azar*/
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
     * Function initializes the view
     *
     * @param savedInstanceState if the fragment is being re-created from a previous state,
     *                           this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_fragment, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned.
     * Initializes all views.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
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
     * Function handles the category click
     *
     * @param category the category that was clicked
     * @param position the position of the category in the list
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
     * Function adds an item to the cart
     *
     * @param item the item to clicked
     */
    @Override
    public void onItemClick(MenuItem item) {
        if (FBref.cartItems.containsKey(item))
            FBref.cartItems.put(item, FBref.cartItems.get(item) + 1);
        else
            FBref.cartItems.put(item, 1);
    }
}