package com.example.waiterapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waiterapp.KitchenApp.OrderItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Map;

public class HistoryFragment extends Fragment{
    public static class DoneOrder
    {
        OrderFragment.Order order;
        Integer rating = 0;
        String comment = "";
        Boolean isRated = false; // Default
    }

    OrderItemAdapter adapter;
    RecyclerView rvHistory;
    TextView tvHistory;
    ArrayList<OrderFragment.Order> orders = new ArrayList<>();
    ArrayList<DoneOrder> doneOrders = new ArrayList<>();

    AlertDialog.Builder adb;
    DoneOrder currentSelectedOrder;
    int currentSelectedIndex;
    int rating;
    String comment;

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
                            DoneOrder doneOrder = new DoneOrder();
                            doneOrder.order = order;
                            doneOrders.add(doneOrder);
                            getRating(doneOrder);
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
        this.currentSelectedOrder = doneOrders.get(orders.indexOf(order));
        this.currentSelectedIndex = orders.indexOf(order);
        String fullOrder = "";
        for (Map.Entry<String, Integer> entry : order.Order.entrySet()) {
            String itemName = entry.getKey();
            Integer quantity = entry.getValue();

            fullOrder += itemName + ": " + quantity + "\n";;
        }
        if (order.request != null)
            fullOrder += '\n' + order.request;

        if (currentSelectedOrder.isRated)
        {
            fullOrder += '\n' + "Rating: " + currentSelectedOrder.rating + "\n" + "Comment: " + currentSelectedOrder.comment;
            adb.setPositiveButton("OK", null);
        }
        else
            adb.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    rate(currentSelectedOrder);
                }
            });
        adb.setMessage(fullOrder);
        adb.show();
    }

    /**
     * Input: DoneOrder order
     * Output: Void
     * Function gets the rating from the database
     */
    private void getRating(DoneOrder order)
    {
        FBref.refRatings.child(String.valueOf(order.order.tableNum))
                .child(order.order.waiterUID)
                .child(order.order.time).addValueEventListener(new com.google.firebase.database.ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    order.rating = snapshot.child("Rating").getValue(Integer.class);
                    order.comment = snapshot.child("Comment").getValue(String.class);
                    order.isRated = snapshot.child("isRated").getValue(Boolean.class);
                }
                else
                    order.isRated = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Couldn't get rating", order.order.waiterUID);
            }
        });
    }

    /**
     * Input: DoneOrder order
     * Output: Void
     * Function Creates a alertDialog for rating
     */
    private void rate(DoneOrder order)
    {
        androidx.appcompat.app.AlertDialog.Builder adb2 = new androidx.appcompat.app.AlertDialog.Builder(this.getContext());
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.rate_alert_dialog, null);
        EditText etRate = layout.findViewById(R.id.etRate);
        EditText etComment = layout.findViewById(R.id.etComment);


        adb2.setView(layout);
        adb2.setTitle("Special Requests");
        adb2.setPositiveButton("Submit Order", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rating = Integer.valueOf(etRate.getText().toString());
                if (rating > 5 || rating < 1)
                    Toast.makeText(getContext(), "Rating must be between 1 and 5", Toast.LENGTH_SHORT).show();
                else
                {
                    comment = etComment.getText().toString();
                    Log.d("Special Request", comment);
                    uploadRating(order);
                }
            }
        });

        adb2.setNegativeButton("Cancel", null);
        adb2.show();
    }

    /**
     * Input: DoneOrder order
     * Output: Void
     * Function uploads the rating to the database
     */
    private void uploadRating(DoneOrder order)
    {
        com.google.firebase.database.DatabaseReference ref = FBref.refRatings
                .child(String.valueOf(order.order.tableNum))
                .child(order.order.waiterUID)
                .child(order.order.time);

        ref.child("Rating").setValue(rating);
        ref.child("Comment").setValue(comment);
        ref.child("isRated").setValue(true);
    }
}