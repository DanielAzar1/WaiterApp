package com.example.waiterapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderFragment extends Fragment {
    private RecyclerView rvOrder;
    private CartAdapter adapter;
    private double totalPrice;
    private TextView price;

    public static class Order
    {
        public String fullTime;
        public String time;
        public String deadline;
        public Integer tableNum = 200; // PLACEHOLDER
        public String waiterUID;
        public Map<String, Integer> Order;
        public Boolean status; // 0 = InProgress, 1 = Done

        public Order() {}
        public Order(String t, Map<String, Integer> o, String u, boolean s) {
            this.time = t;
            this.Order = o;
            this.status = s;
            this.waiterUID = u;
        }
    }

    /**
     * Input: LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
     * Output: View
     * Function creates a new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
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
     * Input: View view
     * Output: Void
     * Function places the order
     */
    public void onPlaceOrder(View view) {
        Toast.makeText(OrderFragment.this.getContext(), "Order Placed!", Toast.LENGTH_LONG).show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        Log.d("Current time:", currentTime);

        Order newOrder = new Order(currentTime, GetUploadableMap(FBref.cartItems), FBref.currentUser.getUID(), false);

        FBref.refOrders.child(newOrder.waiterUID).child(currentTime).setValue(newOrder)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(), "Order Sent!", Toast.LENGTH_LONG).show();

                FBref.cartItems.clear();
                adapter.updateData(FBref.cartItems);
                totalPrice = 0;
                price.setText("$0.00");
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    /**
     * Input: Map<MenuItem, Integer> m
     * Output: Map<String, Integer>
     * Function converts a map of MenuItem and Integer to a map of String and Integer
     */
    public static Map<String, Integer> GetUploadableMap(Map<MenuItem, Integer> m)
    {
        Map<String, Integer> uploadableMap = new HashMap<>();

        m.forEach((item, quantity) -> {  // Iterate over map
            uploadableMap.put(item.getName(), quantity);
        });

        return uploadableMap;
    }
}