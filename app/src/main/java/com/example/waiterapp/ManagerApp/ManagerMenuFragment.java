package com.example.waiterapp.ManagerApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waiterapp.CategoryAdapter;
import com.example.waiterapp.FBref;
import com.example.waiterapp.MenuItem;
import com.example.waiterapp.MenuItemAdapter;
import com.example.waiterapp.R;

import java.util.ArrayList;
import java.util.Arrays;


public class ManagerMenuFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener, ManagerMenuItemAdapter.OnItemClickListener{
    RecyclerView rvCategories;
    RecyclerView rvMenuItems;
    CategoryAdapter categoryAdapter;
    ManagerMenuItemAdapter menuAdapter;

    String currCategory = "Starters";

    ArrayList<String> categoryList = new ArrayList<>();
    AlertDialog.Builder adb;

    public ManagerMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_menu, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCategories = view.findViewById(R.id.rvManagerCategories);
        rvMenuItems = view.findViewById(R.id.rvManagerMenuItems);
        categoryList.addAll(Arrays.asList("Starters", "Main Courses", "Desserts"));

        // Set the adapter for rvCategories
        categoryAdapter = new CategoryAdapter(categoryList, this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false)); // Set rvCategories to the RecyclerView
        rvCategories.setAdapter(categoryAdapter);

        // Set the adapter for rvMenuItems
        rvMenuItems.setLayoutManager(new LinearLayoutManager(this.getContext())); // Set rvMenuItems to the RecyclerView
        menuAdapter = new ManagerMenuItemAdapter(this, new ArrayList<MenuItem>());
        rvMenuItems.setAdapter(menuAdapter);

        menuAdapter.setItems(FBref.startersList);

        adb = new AlertDialog.Builder(this.getContext());
        adb.setTitle("Remove Item");
        adb.setIcon(R.drawable.trashcan);
    }

    @Override
    public void onCategoryClick(String category, int position) {
        currCategory = category;
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

    @Override
    public void onDeleteClick(MenuItem item) {
        adb.setMessage("Are you sure you want to remove " + item.getName() + "?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (currCategory) {
                    case "Starters":
                        FBref.refStarters.child(item.getCategory()).child(item.getName()).removeValue();
                        FBref.startersList.remove(item);
                        menuAdapter.setItems(FBref.startersList);
                        break;
                    case "Main Courses":
                        FBref.refMains.child(item.getCategory()).child(item.getName()).removeValue();
                        FBref.mainsList.remove(item);
                        menuAdapter.setItems(FBref.mainsList);
                        break;
                    case "Desserts":
                        FBref.refDesserts.child(item.getCategory()).child(item.getName()).removeValue();
                        FBref.dessertslist.remove(item);
                        menuAdapter.setItems(FBref.dessertslist);
                        break;
                }
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        AlertDialog ad = this.adb.create();
        ad.show();
    }
}