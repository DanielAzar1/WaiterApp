package com.example.waiterapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class is a fragment that displays the order
 *
 * @author Daniel Azar*/
public class OrderFragment extends Fragment {
    private RecyclerView rvOrder;
    private CartAdapter adapter;
    private double totalPrice;
    private TextView price;
    private String request;
    private int tableNumber;

    /**
     * Class that represents an order
     * */
    public static class Order
    {
        public String fullTime;
        public String time;
        public String deadline;
        public int TimeToLive;
        public Integer tableNum = 200; // PLACEHOLDER
        public String waiterUID;
        public Map<String, Integer> Order;
        public Boolean status; // 0 = InProgress, 1 = Done
        public String request = "";

        public Order() {}
        public Order(String t, Map<String, Integer> o, String u, boolean s, int ttl) {
            this.time = t;
            this.Order = o;
            this.status = s;
            this.waiterUID = u;
            this.TimeToLive = ttl;
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned.
     * Initializes all views.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOrder = view.findViewById(R.id.rvCartItems);
        price = view.findViewById(R.id.tvTotalPrice);

        View btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(this::onPlaceOrder);

        rvOrder.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new CartAdapter(FBref.cartItems, new CartAdapter.OnCartItemInteractionListener() {

            @Override
            public void onRemoveItemClick(MenuItem item, int position) {
                FBref.cartItems.remove(item);
                adapter.updateData(FBref.cartItems);
                totalPrice = adapter.getTotalPrice();
                price.setText(String.format("$%.2f", totalPrice));
            }

            @Override
            public void onIncreaseQuantityClick(MenuItem item, int position) {
                FBref.cartItems.put(item, FBref.cartItems.get(item) + 1);
                adapter.updateData(FBref.cartItems);
                totalPrice = adapter.getTotalPrice();
                price.setText(String.format("$%.2f", totalPrice));
            }

            @Override
            public void onDecreaseQuantityClick(MenuItem item, int position) {
                int count = FBref.cartItems.get(item) - 1;
                if (count == 0)
                    FBref.cartItems.remove(item);
                else
                    FBref.cartItems.put(item, count);
                adapter.updateData(FBref.cartItems);
                totalPrice = adapter.getTotalPrice();
                price.setText(String.format("$%.2f", totalPrice));
            }
        });

        rvOrder.setAdapter(adapter);
        totalPrice = adapter.getTotalPrice();
        price.setText(String.format("$%.2f", totalPrice));
    }

    /**
     * Function places the order
     */
    public void placeOrder() {
        if (!isAdded() || getContext() == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        int maxPrepTime = 0;
        for (MenuItem item : FBref.cartItems.keySet()) {
            Log.d("ItemTTL", item.getName() + ", " + item.getTimeToLive());
            if (item.getTimeToLive() > maxPrepTime) {
                maxPrepTime = item.getTimeToLive();
            }
        }
        Log.d("MaxTTL", String.valueOf(maxPrepTime));

        Order newOrder = new Order(currentTime, GetUploadableMap(FBref.cartItems), FBref.currentUser.getUID(), false, maxPrepTime);
        newOrder.request = request;
        newOrder.tableNum = tableNumber;

        FBref.refOrders.child(newOrder.waiterUID).child(currentTime).setValue(newOrder)
            .addOnSuccessListener(aVoid -> {
                if (isAdded() && getContext() != null)
                    Toast.makeText(getContext(), "Order Sent!", Toast.LENGTH_LONG).show();

                FBref.cartItems.clear();
                adapter.updateData(FBref.cartItems);
                totalPrice = 0;
                price.setText("$0.00");
            })
            .addOnFailureListener(e -> {
                if (isAdded() && getContext() != null)
                    Toast.makeText(requireContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            });
    }

    /**
     * Function converts a map of MenuItem and Integer to a map of String and Integer
     *
     * @param m the map to convert
     * @return uploadable map
     */
    public static Map<String, Integer> GetUploadableMap(Map<MenuItem, Integer> m)
    {
        Map<String, Integer> uploadableMap = new HashMap<>();

        m.forEach((item, quantity) -> {  // Iterate over map
            uploadableMap.put(item.getName(), quantity);
        });

        return uploadableMap;
    }

    /**
     * Function puts up a dialog to ask for special requests and table number
     *
     * @param view the view clicked
     */
    public void onPlaceOrder(View view)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.getContext());
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.order_alert_dialog, null);
        EditText etSpecial = layout.findViewById(R.id.etSpecial);
        EditText etTable = layout.findViewById(R.id.etTable);


        adb.setView(layout);
        adb.setTitle("Special Requests");
        adb.setPositiveButton("Submit Order", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                request = etSpecial.getText().toString();
                tableNumber = Integer.parseInt(etTable.getText().toString());
                Log.d("Special Request", request);
                placeOrder();
            }
        });

        adb.setNegativeButton("Cancel", null);
        adb.show();
    }
}