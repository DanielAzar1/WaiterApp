package com.example.waiterapp.KitchenApp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>
{
    private final ArrayList<OrderFragment.Order> orders;
    private final OrderItemAdapter.OnOrderClickListener listener;
    private Integer backgroundColor = null;

    public OrderItemAdapter(ArrayList<OrderFragment.Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_layout, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderFragment.Order currentOrder = orders.get(position);
        holder.bind(currentOrder.time, String.valueOf(currentOrder.tableNum));

        String currTime = new java.text.SimpleDateFormat("yyyyMMddHHmm", java.util.Locale.getDefault())
                .format(new java.util.Date());

        boolean isExpired = currTime.compareTo(currentOrder.deadline) >= 0;

        androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) holder.itemView;
        if (Boolean.TRUE.equals(currentOrder.status)) {
            card.setCardBackgroundColor(android.graphics.Color.GREEN);
        } else if (isExpired) {
            card.setCardBackgroundColor(android.graphics.Color.parseColor("#FFA500")); // Orange
        } else {
            card.setCardBackgroundColor(android.graphics.Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onOrderClick(currentOrder);
        });
    }

    public void setBackgroundColor(Integer color) {
        this.backgroundColor = color;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public interface OnOrderClickListener {
        void onOrderClick(OrderFragment.Order order);
    }

    /**
     * Input: Void
     * Output: Void
     * Function Forces  a refresh of the adapter every 1 minute
     */
    public void startTimer() {
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();

                handler.postDelayed(this, 60000);
            }
        }, 60000);
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTableID;

        /**
         * Input: View itemView
         * Output: Void
         * Function initializes the views
         */
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTableID = itemView.findViewById(R.id.tvTable);
        }

        /**
         * Input: String category, boolean isSelected
         * Output: Void
         * Function binds the data to the views
         */
        void bind(String Time, String tableID) {
            tvTime.setText(Time);
            tvTableID.setText(tableID);
        }

    }
}
