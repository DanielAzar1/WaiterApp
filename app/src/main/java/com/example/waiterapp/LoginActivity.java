package com.example.waiterapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import com.example.waiterapp.User;
import com.example.waiterapp.ManagerApp.ManagerMenu;

public class LoginActivity extends AppCompatActivity {
    EditText userET, passET;

    ArrayList<User> managers = new ArrayList<>();
    ArrayList<User> waiters = new ArrayList<>();
    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);

        // Initialize the lists
        pullManagers();
        pullWaiters();
        getFoodItem(FBref.refDesserts, FBref.storageRefDesserts, FBref.dessertslist);
        getFoodItem(FBref.refMains,FBref.storageRefMains, FBref.mainsList);
        getFoodItem(FBref.refStarters,FBref.storageRefStarters, FBref.startersList);
    }

    /**
     * Input: View view
     * Output: Void
     * Function logs in the user
     */
    public void onLogin(View view) {
        for (int i = 0; i < managers.size(); i++)
            Log.d("ManagerName" + i, managers.get(i).getName());

        FBref.refAuth.signInWithEmailAndPassword(userET.getText().toString(), passET.getText().toString())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FBref.refAuth.getCurrentUser();
                    for (int i = 0; i < managers.size(); i++)
                    {
                        if (user.getUid().equals(managers.get(i).getUID()))
                        {
                            FBref.currentUser = managers.get(i);
                            FBref.userList = waiters;
                            Log.d("Manager", "Start Manager Activity");
                            Intent intent = new Intent(LoginActivity.this, ManagerMenu.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    for (int i = 0; i < waiters.size(); i++)
                    {
                        if (user.getUid().equals(waiters.get(i).getUID()))
                        {
                            FBref.currentUser = waiters.get(i);
                            Log.d("Waiter", "Start Waiter Activity");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Input: View view
     * Output: Void
     * Function registers a new user
     */
    public void getFoodItem(DatabaseReference ref, StorageReference storageRef, ArrayList<MenuItem> list) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        String categoryName = categorySnapshot.getKey();
                        for (DataSnapshot itemSnapshot : categorySnapshot.getChildren()) {
                            try {
                                com.example.waiterapp.MenuItem menuItem = itemSnapshot.getValue(com.example.waiterapp.MenuItem.class);
                                if (menuItem != null) {
                                    getMenuItemPhoto(storageRef, menuItem, list);
                                    menuItem.setCategory(categoryName);
                                    list.add(menuItem);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Toast.makeText(LoginActivity.this, "Error reading database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Input: StorageReference storageRef, MenuItem item, ArrayList<MenuItem> list
     * Output: Void
     * Function gets the photo of a menu item
     */
    public void getMenuItemPhoto(StorageReference storageRef, MenuItem item, ArrayList<MenuItem> list)
    {
        StorageReference refFile = storageRef.child(item.getName() + ".jpg");

        refFile.getBytes(FBref.MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                item.setImage(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof com.google.firebase.storage.StorageException) {
                    int errorCode = ((com.google.firebase.storage.StorageException) e).getErrorCode();
                    String errorMessage = e.getMessage();

                    // This will show in your Logcat (bottom of Android Studio)
                    android.util.Log.e("FirebaseStorageError", "Code: " + errorCode + " Message: " + errorMessage);

                    // Show more detail in the Toast
                    Toast.makeText(LoginActivity.this, "Error Code: " + errorCode + " at " + refFile.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Download Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void pullManagers() {
        FBref.refManagers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                managers.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("FullName").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String UID = snapshot.child("UID").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        User manager = new User(name, email, UID, role);
                        if (manager != null) {
                            managers.add(manager);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error loading managers: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pullWaiters() {
        FBref.refWaiters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waiters.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("FullName").getValue(String.class);
                        String email = snapshot.child("Email").getValue(String.class);
                        String UID = snapshot.child("UID").getValue(String.class);
                        String role = snapshot.child("Role").getValue(String.class);

                        User waiter = new User(name, email, UID, role);
                        waiters.add(waiter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error loading managers: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}