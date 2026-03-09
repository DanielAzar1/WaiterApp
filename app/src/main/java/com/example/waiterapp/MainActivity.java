package com.example.waiterapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MenuFragment()).commit();

    }

    /**
     * Input: Void
     * Output: Void
     * Function sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected_fragment = null;

                int id = menuItem.getItemId();

                if (id == R.id.nav_home)
                {
                    selected_fragment = new MenuFragment();
                }
                if (id == R.id.nav_cart)
                {
                    selected_fragment = new OrderFragment();
                }

                if (selected_fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selected_fragment).commit();
                }
                return true;
            }
        });
    }
}