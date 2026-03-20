package com.example.waiterapp.ManagerApp;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.waiterapp.CategoryAdapter;
import com.example.waiterapp.FBref;
import com.example.waiterapp.ImageHelper;
import com.example.waiterapp.MenuItem;
import com.example.waiterapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class ManagerAddFragment extends Fragment {
    Spinner categorySpinner, typeSpinner;
    EditText nameET, descriptionET, priceET;
    ImageView iv;
    Button btnAddDish,btnChoosePhoto;
    MenuItem item;

    AlertDialog.Builder adb;
    AlertDialog ad;
    Uri uri;

    final int MAX_NAME_LENGTH = 50;
    final int MAX_DESCRIPTION_LENGTH = 200;
    private final int REQUEST_CODE_PICK_IMAGE = 200;

    final String[] types = {"Starters", "MainCourses", "Desserts"};
    final String[] categories= {"Dairy", "Meat", "Vegan", "Vegetarian"};


    public ManagerAddFragment() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_manager_add, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categorySpinner = view.findViewById(R.id.CategorySpinner);
        typeSpinner = view.findViewById(R.id.TypeSpinner);
        nameET = view.findViewById(R.id.editTextText);
        descriptionET = view.findViewById(R.id.editTextText3);
        iv = view.findViewById(R.id.imageView);
        priceET = view.findViewById(R.id.editTextNumberDecimal);

        btnChoosePhoto = view.findViewById(R.id.button2);
        btnAddDish = view.findViewById(R.id.button3);

        btnChoosePhoto.setOnClickListener(v -> onChoosePhoto(v));
        btnAddDish.setOnClickListener(v -> onAddDish(v));

        categorySpinner.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, categories));
        typeSpinner.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, types));

        adb = new AlertDialog.Builder(this.getContext());

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "Grant result for read external storage:" + grantResults);
    }

    /**
     * onClickFunction for the choose photo button, sends a request to access the gallery
     * input: View view
     * output: none
     * */
    public void onChoosePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * Function Handles The data that returns from the request
     * input: int requestCode, int resultCode, Intent Data
     * output: none
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            this.uri = uri;
            iv.setImageBitmap(ImageHelper.getBitmapFromUri(getContext(), uri));
        }
    }

    /**
     * Parses the input and Adds a new dish to The database and List, OnClick method for add dish button
     * Input: View view
     * Output: none
     * */
    public void onAddDish(View view) {
        String category = categorySpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();
        Bitmap photo = iv.getDrawingCache();
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String price = priceET.getText().toString();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty())
        {
            adb.setMessage("Please fill all the fields!");
            adb.setTitle("Error!");
            adb.show();
        }
        else if (name.length() > MAX_NAME_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH)
        {
            adb.setMessage("Name or Description is too long!");
            adb.setTitle("Error!");
            ad = adb.create();
            ad.show();
        }
        else if (price == "-" || price == "." || price == "-." )
        {
            adb.setMessage("Please enter a valid price!");
            adb.setTitle("Error!");
            ad = adb.create();
            ad.show();
        }
        else
        {
            float priceFloat = Float.parseFloat(price);
            MenuItem item = new MenuItem(name, description, priceFloat, category);
            item.setImage(photo);

            item.uploadDish(type);
            this.item = item;
            uploadImage(type);
            adb.setMessage("Dish added successfully!");
            adb.setTitle("Success!");
            ad = adb.create();
            ad.show();
        }
    }

    /**
     * Helper function for onAddDish that adds the Photo to the storage and the item to the list
     * Input: String type
     * Output: none
     * */
    private void uploadImage(String type)
    {
        switch (type)
        {
            case "Starters":
                FBref.startersList.add(item);
                ImageHelper.uploadImageToFirebase(uri, "images/FoodItems/Starters/" + item.getName() + ".jpg");
                break;
            case "MainCourses":
                FBref.mainsList.add(item);
                ImageHelper.uploadImageToFirebase(uri, "images/FoodItems/MainCourses/" + item.getName() + ".jpg");
                break;
            case "Desserts":
                FBref.dessertslist.add(item);
                ImageHelper.uploadImageToFirebase(uri, "images/FoodItems/Desserts/" + item.getName() + ".jpg");
                break;
        }
    }
}