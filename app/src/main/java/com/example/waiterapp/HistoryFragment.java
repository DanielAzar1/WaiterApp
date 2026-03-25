package com.example.waiterapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.waiterapp.KitchenApp.OrderItemAdapter;
import com.example.waiterapp.ManagerApp.ManagerUserItemAdapter;

import java.util.ArrayList;
import java.util.Map;

public class HistoryFragment extends Fragment{
    OrderItemAdapter adapter;
    RecyclerView rvHistory;
    TextView tvHistory;
    ArrayList<OrderFragment.Order> orders = new ArrayList<>();

    AlertDialog.Builder adb;
    OrderFragment.Order currentSelectedOrder;
    int currentSelectedIndex;

    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Input: Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Input: LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
     * Output: View
     * Function creates a new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHistory = view.findViewById(R.id.rvHistory);
        tvHistory = view.findViewById(R.id.tvHistory);

        String text = FBref.currentUser.getName()+"'s Order History";
        tvHistory.setText(text);

        rvHistory.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new OrderItemAdapter(orders, v->onViewOrder(v));
        rvHistory.setAdapter(adapter);

        adb = new AlertDialog.Builder(this.getContext());
        adb.setTitle("Order Details");

        FBref.refDoneOrders.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                for (com.google.firebase.database.DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(FBref.currentUser.getUID())) {
                        com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>> t =
                                new com.google.firebase.database.GenericTypeIndicator<Map<String, Integer>>() {};
                        for (com.google.firebase.database.DataSnapshot child : data.getChildren()) {
                            OrderFragment.Order order = new OrderFragment.Order();
                            order.time = child.child("time").getValue(String.class);
                            order.Order = child.child("Order").getValue(t);
                            order.status = child.child("status").getValue(Boolean.class);
                            order.waiterUID = child.child("waiterUID").getValue(String.class);
                            order.tableNum = child.child("tableNum").getValue(Integer.class);
                            order.deadline = child.child("deadline").getValue(String.class);
                            order.TimeToLive = child.child("TimeToLive").getValue(Integer.class);

                            Log.d("OrderData", order.time + " " + order.Order + " " + order.status + " " + order.waiterUID);

                            orders.add(order);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull com.google.firebase.database.DatabaseError error) {
                Log.d("Firebase Error", "Couldnt get orders");
            }
        });
    }

    /**
     * Input: OrderFragment.Order order
     * Output: Void
     * Function shows the order details
     */
    private void onViewOrder(OrderFragment.Order order)
    {
        this.currentSelectedOrder = order;
        this.currentSelectedIndex = orders.indexOf(order);
        String fullOrder = "";
        for (Map.Entry<String, Integer> entry : order.Order.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();

            fullOrder += itemName + ": " + quantity + "\n";;
        }
        adb.setMessage(fullOrder);
        adb.show();
    }
}