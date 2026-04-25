package com.example.waiterapp.ManagerApp;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waiterapp.FBref;
import com.example.waiterapp.MenuFragment;
import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;
import com.example.waiterapp.wifiReciever;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * @author Daniel Azar
 * @version 1.0
 *
 * This class is the main activity for the Manager app.
 */
public class ManagerMenu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    /**
     * Function initializes the view
     *
     * @param savedInstanceState The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_menu);

        bottomNavigationView = findViewById(R.id.btmNav2);
        bottomNavigationView.setSelectedItemId(R.id.nav_menu);

        setupBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer2, new ManagerMenuFragment()).commit();

        IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new wifiReciever(), filter);

        LoadRatingAndComments();

    }

    /**
     * Function sets up the bottom navigation
     **/
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected_fragment = null;

                int id = menuItem.getItemId();

                if (id == R.id.nav_menu)
                {
                    selected_fragment = new ManagerMenuFragment();
                }
                if (id == R.id.nav_user)
                {
                    selected_fragment = new ManagerUserFragment();
                }
                if (id == R.id.nav_plus)
                {
                    selected_fragment = new ManagerAddFragment();
                }
                if (id  == R.id.nav_add_waiter)
                {
                    selected_fragment = new ManagerAddUserFragment();
                }
                if (id == R.id.nav_get_rating)
                {
                    selected_fragment = new ManagerRatingFragment();
                }

                if (selected_fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer2, selected_fragment).commit();
                }
                return true;
            }
        });
    }

    /**
     * Function loads the rating and comments from the database
     */
    public void LoadRatingAndComments()
    {
        FBref.rating = 0.0;
        FBref.comments.clear();
        //Load all ratings and make average
        FBref.refRatings.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double tempSum = 0;
                int count = 0;
                for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren())
                {
                    for (com.google.firebase.database.DataSnapshot child : data.getChildren())
                    {
                        for (com.google.firebase.database.DataSnapshot child2 : child.getChildren())
                        {
                            Integer val = child2.child("Rating").getValue(Integer.class);
                            if (val != null) {
                                tempSum += val;
                                count++;
                            }
                        }
                    }
                }
                if (count > 0)
                    FBref.rating = tempSum / count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ManagerMenuError", "Couldn't get ratings");
            }
        });

        FBref.refRatings.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot tableSnapshot, @Nullable String previousChildName) {
                String currentTable = tableSnapshot.getKey();
                for (DataSnapshot waiterSnapshot : tableSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : waiterSnapshot.getChildren()) {
                        String rawComment = orderSnapshot.child("Comment").getValue(String.class);
                        if (rawComment != null && !rawComment.isEmpty()) {
                            if (!FBref.comments.contains(rawComment + " - Table " + currentTable))
                                FBref.comments.add(rawComment + " - Table " + currentTable);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String currentTable = snapshot.getKey();
                for (DataSnapshot waiterSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : waiterSnapshot.getChildren()) {
                        String rawComment = orderSnapshot.child("Comment").getValue(String.class);
                        if (rawComment != null && !rawComment.isEmpty()) {
                            if (!FBref.comments.contains(rawComment + " - Table " + currentTable))
                                FBref.comments.add(rawComment + " - Table " + currentTable);
                        }
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ManagerMenuError", "Comment listener cancelled: " + error.getMessage());
            }
        });

    }
}