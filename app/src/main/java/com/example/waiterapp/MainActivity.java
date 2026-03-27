package com.example.waiterapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    NotificationManager notificationManager;
    private static final String CHANNEL_ID = "My Channel ID";
    private static final String CHANNEL_NAME = "My_Channel_Name";
    private static final int NOTIFICATION_ID = 1;

    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        initializeNotif();

        setupBottomNavigation();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragContainer, new MenuFragment()).commit();

        initializeNotif();

        FBref.refDoneOrders.child(FBref.currentUser.getUID()).limitToLast(1).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot order : snapshot.getChildren()) {
                        String tableNum = String.valueOf(order.child("tableNum").getValue());
                        showOrderNotification("Order for Table " + tableNum + " is ready!");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new wifiReciever(), filter);
    }

    /**
     * Input: String message
     * Output: Void
     * Function shows a notification with a given message
     */
    private void showOrderNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your icon
                .setContentTitle("Kitchen Update")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
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

    /**
     * Input: Void
     * Output: Void
     * Function initializes the notification manager
     */
    private void initializeNotif()
    {
        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        // Request permission for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }
}