package com.example.waiterapp.KitchenApp;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.FBref;
import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;
import com.example.waiterapp.wifiReciever;

import java.util.ArrayList;
import java.util.Map;

public class KitchenMain extends AppCompatActivity {
    RecyclerView rvActive, rvDone;
    OrderItemAdapter activeAdapter, doneAdapter;
    ArrayList<OrderFragment.Order> activeOrders = new ArrayList<>(), doneOrders = new ArrayList<>();

    AlertDialog.Builder adb1, adb2;
    OrderFragment.Order currentSelectedOrder;
    int currentSelectedIndex;

    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the views, adapters and listeners and gets data from the db and starts the timer
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_main);

        rvActive = findViewById(R.id.rvActive);
        rvDone = findViewById(R.id.rvDone);

        activeAdapter = new OrderItemAdapter(activeOrders, v->onViewOrder(v, adb1));
        doneAdapter = new OrderItemAdapter(doneOrders, v->onViewOrder(v, adb2));
        rvActive.setAdapter(activeAdapter);
        activeAdapter.startTimer();
        rvDone.setAdapter(doneAdapter);

        IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new wifiReciever(), filter);

        adb1 = new AlertDialog.Builder(this);
        adb2 = new AlertDialog.Builder(this);
        adb1.setTitle("Done Order Details");
        adb1.setPositiveButton("Back", (dialog, which) -> dialog.cancel());
        adb1.setNegativeButton("Mark Done", (dialog, which) -> onMarkDone(dialog, which));

        adb2.setTitle("Active Order Details");
        adb2.setPositiveButton("Back", (dialog, which) -> dialog.cancel());
        FBref.refOrders.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>> t =
                        new com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>>() {};
                for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren()) {
                    int count = 0;
                    for (com.google.firebase.database.DataSnapshot child : data.getChildren()) {
                            count++;
                            if (activeOrders.size() < count) {
                                OrderFragment.Order order = new OrderFragment.Order();
                                order.fullTime = child.child("time").getValue(String.class);
                                order.time = order.fullTime.substring(8, 12);
                                order.Order = child.child("Order").getValue(t);
                                order.status = child.child("status").getValue(Boolean.class);
                                order.waiterUID = child.child("waiterUID").getValue(String.class);
                                order.tableNum = child.child("tableNum").getValue(Integer.class);
                                order.TimeToLive = child.child("TimeToLive").getValue(Integer.class);
                                order.request = child.child("request").getValue(String.class);

                                java.util.Calendar cal = java.util.Calendar.getInstance();
                                cal.add(java.util.Calendar.MINUTE, order.TimeToLive);
                                Log.d("SetTimer", "Set Time To Live For: " + order.TimeToLive);
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmm", java.util.Locale.getDefault());
                                order.deadline = sdf.format(cal.getTime());

                                Log.d("OrderData", order.time + " " + order.Order + " " + order.status + " " + order.waiterUID);

                                activeOrders.add(order);
                            }
                        }
                    activeAdapter.sortByDeadline();
                    activeAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Log.d("Firebase Error", "Couldn't get orders");
            }
        });

    }

    /**
     * Input: OrderFragment.Order order, AlertDialog.Builder adb
     * Output: Void
     * Function shows the order details
     */
    public void onViewOrder(OrderFragment.Order order, AlertDialog.Builder adb)
    {
        this.currentSelectedOrder = order;
        this.currentSelectedIndex = activeOrders.indexOf(order);
        String fullOrder = "";
        for (Map.Entry<String, Integer> entry : order.Order.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();

            fullOrder += itemName + ": " + quantity + "\n";
        }
        if (order.request != null)
            fullOrder += '\n' + order.request;
        adb.setMessage(fullOrder);
        adb.show();
    }

    /**
     * Input: DialogInterface dialog, int which
     * Output: Void
     * Function marks the order as done and updates the database
     */
    public void onMarkDone(DialogInterface dialog, int which)
    {
        if (currentSelectedIndex != -1) {

            doneOrders.add(currentSelectedOrder);
            doneAdapter.notifyItemInserted(doneOrders.size() - 1);

            activeOrders.remove(currentSelectedIndex);
            activeAdapter.notifyItemRemoved(currentSelectedIndex);

            currentSelectedOrder.time = currentSelectedOrder.fullTime;
            FBref.refOrders.child(currentSelectedOrder.waiterUID).child(currentSelectedOrder.time).removeValue();
            currentSelectedOrder.status = true;
            FBref.refDoneOrders.child(currentSelectedOrder.waiterUID).child(currentSelectedOrder.time).setValue(currentSelectedOrder);

            currentSelectedIndex = -1;
        }
    }
}