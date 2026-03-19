package com.example.waiterapp.KitchenApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.CategoryAdapter;
import com.example.waiterapp.OrderFragment;
import com.example.waiterapp.R;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>
{
    private final ArrayList<OrderFragment.Order> orders;
    private final OrderItemAdapter.OnOrderClickListener listener;

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
        holder.bind(currentOrder.time.substring(8, 12), String.valueOf(currentOrder.tableNum));
        holder.itemView.setOnClickListener(v -> {
            listener.onOrderClick(currentOrder);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Interface to send click events back to the Activity
    public interface OnOrderClickListener {
        void onOrderClick(OrderFragment.Order order);
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
