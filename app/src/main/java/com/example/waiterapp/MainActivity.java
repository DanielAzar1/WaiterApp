package com.example.waiterapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        setupBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, new MenuFragment()).commit();

    }

    /**
     * Input: Void
     * Output: Void
     * Function sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {
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
                if (id == R.id.nav_history)
                {
                    selected_fragment = new HistoryFragment();
                }

                if (selected_fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer, selected_fragment).commit();
                }
                return true;
            }
        });
    }
}