package com.example.waiterapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Class handles the wifi connection changes and notifies the user
 *
 * @author Daniel Azar*/
public class wifiReciever extends BroadcastReceiver {
    AlertDialog.Builder adb;

    /**
     * Function handles the wifi connection changes and notifies the user
     *
     * @param context the context it was called from
     * @param intent the intent that was received from the system
     */
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
            adb.setMessage("You Are Not Connected To WiFi Or Cellular Data!\nConnect To A Network And Relaunch The App.");
            adb.setNegativeButton("OK", null);
            adb.show();
        }
    }
}
