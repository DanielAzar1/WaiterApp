package com.example.waiterapp.KitchenApp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.FBref;
import com.example.waiterapp.MenuItem;
import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;

import java.util.ArrayList;
import java.util.Map;

public class KitchenMain extends AppCompatActivity {
    RecyclerView rvActive, rvDone;
    OrderItemAdapter activeAdapter, doneAdapter;
    ArrayList<OrderFragment.Order> activeOrders = new ArrayList<>(), doneOrders = new ArrayList<>();

    AlertDialog.Builder adb1, adb2;
    OrderFragment.Order currentSelectedOrder;
    int currentSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_main);

        rvActive = findViewById(R.id.rvActive);
        rvDone = findViewById(R.id.rvDone);

        activeAdapter = new OrderItemAdapter(activeOrders, v->onViewOrder(v, adb1));
        doneAdapter = new OrderItemAdapter(doneOrders, v->onViewOrder(v, adb2));
        rvActive.setAdapter(activeAdapter);
        rvDone.setAdapter(doneAdapter);

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
                activeOrders.clear();
                com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>> t =
                        new com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>>() {};
                for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren()) {
                    for (com.google.firebase.database.DataSnapshot child : data.getChildren())
                    {
                        OrderFragment.Order order = new OrderFragment.Order();
                        order.time = child.child("time").getValue(String.class).substring(8, 12);
                        order.Order = child.child("Order").getValue(t);
                        order.status = child.child("status").getValue(Boolean.class);
                        order.waiterUID = child.child("waiterUID").getValue(String.class);
                        order.tableNum = child.child("tableNum").getValue(Integer.class);

                        Log.d("OrderData", order.time + " " + order.Order + " " + order.status + " " + order.waiterUID);

                        activeOrders.add(order);
                    }
                }
                activeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Log.d("Firebase Error", "Couldnt get orders");
            }
        });
    }

    public void onViewOrder(OrderFragment.Order order, AlertDialog.Builder adb)
    {
        this.currentSelectedOrder = order;
        this.currentSelectedIndex = activeOrders.indexOf(order);
        String fullOrder = "";
        for (Map.Entry<String, Integer> entry : order.Order.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();

            fullOrder += itemName + ": " + quantity + "\n";;
        }
        adb.setMessage(fullOrder);
        adb.show();
    }

    public void onMarkDone(DialogInterface dialog, int which)
    {
        if (currentSelectedIndex != -1) {
            doneOrders.add(currentSelectedOrder);
            doneAdapter.notifyItemInserted(doneOrders.size() - 1);

            activeOrders.remove(currentSelectedIndex);
            activeAdapter.notifyItemRemoved(currentSelectedIndex);

            FBref.refOrders.child(currentSelectedOrder.waiterUID).child(currentSelectedOrder.time).removeValue();
            FBref.refDoneOrders.child(currentSelectedOrder.waiterUID).child(currentSelectedOrder.time).setValue(currentSelectedOrder);

            currentSelectedIndex = -1;
        }
    }
}