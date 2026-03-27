package com.example.waiterapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class wifiReciever extends BroadcastReceiver {
    AlertDialog.Builder adb;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ConnectionManager", "Connectivity changed");
        //Connectivity manager works for both WIFI and cellular data - so better than regular wifi Manager
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.d("isConnected", String.valueOf(isConnected));
        if (!isConnected) {
            adb = new AlertDialog.Builder(context);
            adb.setTitle("No Internet Connection");
            adb.setMessage("You are not connected to WiFi or Cellular Data!\nConnect to receive and send data.");
            adb.setNegativeButton("OK", null);
            adb.show();
        }
    }
}
