package com.example.waiterapp.ManagerApp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waiterapp.MenuFragment;
import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ManagerMenu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

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

                if (selected_fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer2, selected_fragment).commit();
                }
                return true;
            }
        });
    }
}