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

/**
 * @author Daniel Azar
 * @version 1.0
 *
 * Fragment for the Manager to add a new dish
 */
public class ManagerAddFragment extends Fragment {
    Spinner categorySpinner, typeSpinner;
    EditText nameET, descriptionET, priceET, ttlEt;
    ImageView iv;
    Button btnAddDish,btnChoosePhoto;
    MenuItem item;

    AlertDialog.Builder adb;
    AlertDialog ad;
    Uri uri;
    Bitmap selectedBitmap;

    final int MAX_NAME_LENGTH = 50;
    final int MAX_DESCRIPTION_LENGTH = 100;
    final int MAX_PRICE_LENGTH = 7; // 9999.99
    final int MAX_TTL_LENGTH = 3;
    private final int REQUEST_CODE_PICK_IMAGE = 200;

    final String[] types = {"Starters", "MainCourses", "Desserts"};
    final String[] categories= {"Dairy", "Meat", "Vegan", "Vegetarian"};


    /**
     * Empty Constructor
     */
    public ManagerAddFragment() {
        // Required empty public constructor
    }

    /**
     * Function creates a new instance of the fragment
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * Function creates a new view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return new view for the fragment UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manager_add, container, false);
    }

    /**
     * Function initializes the views, sets up the adapters and listeners
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle)
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed
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
        ttlEt = view.findViewById(R.id.editTextNumberSigned);

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

    /**
     * Function handles the result of the permission request
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "Grant result for read external storage:" + grantResults);
    }

    /**
     * onClickFunction for the choose photo button, sends a request to access the gallery
     *
     * @param view The view that was clicked
     * */
    public void onChoosePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * Function Handles The data that returns from the request
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                     allowing you to identify who this result came from.
     *
     * @param resultCode The integer result code returned by the child activity.
     *
     * @param data An Intent, which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            if (uri == null) return;

            if (ImageHelper.isImageSizeValid(this.getContext(), uri, 1))
            {
                this.uri = uri;
                selectedBitmap = ImageHelper.getBitmapFromUri(getContext(), uri);
                iv.setImageBitmap(selectedBitmap);
            }
            else
            {
                adb.setMessage("Image is too large!\nImage Size is up to 1MB");
                adb.setTitle("Error!");
                ad = adb.create();
                ad.show();
            }
        }
    }

    /**
     * Parses the input and Adds a new dish to The database and List, OnClick method for add dish button
     *
     * @param view The view that was clicked
     * */
    public void onAddDish(View view) {
        String category = categorySpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();
        String name = nameET.getText().toString();
        String description = descriptionET.getText().toString();
        String price = priceET.getText().toString();
        String ttl = ttlEt.getText().toString();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || selectedBitmap == null || ttl.isEmpty())
        {
            adb.setMessage("Fill all fields!");
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
        else if (price.equals("-")  || price.equals(".") || price.equals("-.") || price.length() > MAX_PRICE_LENGTH)
        {
            adb.setMessage("Please enter a valid price!");
            adb.setTitle("Error!");
            ad = adb.create();
            ad.show();
        }
        else if (ttl.equals("-") || ttl.equals(".") || ttl.equals("-.") || ttl.length() > MAX_TTL_LENGTH)
        {
            adb.setMessage("Please enter a valid time to live!");
            adb.setTitle("Error!");
            ad = adb.create();
            ad.show();
        }
        else
        {
            float priceFloat = Float.parseFloat(price);
            if (priceFloat <= 0)
            {
                adb.setMessage("Price cant be 0 or below!");
                adb.setTitle("Error!");
                ad = adb.create();
                ad.show();
                return;
            }
            int intTtl = Integer.parseInt(ttl);
            if (intTtl <= 0)
            {
                adb.setMessage("Time to live cant be 0 or below!");
                adb.setTitle("Error!");
                ad = adb.create();
                ad.show();
                return;
            }
            MenuItem item = new MenuItem(name, description, priceFloat, category, intTtl);
            item.setImage(selectedBitmap);

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
     *
     * @param type The type of the dish
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