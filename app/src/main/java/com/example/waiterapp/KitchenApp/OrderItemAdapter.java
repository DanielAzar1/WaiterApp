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

/**
 * @author  Daniel Azar
 * @version 1.0
 *
 * adapter for the order list
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>
{
    private final ArrayList<OrderFragment.Order> orders;
    private final OrderItemAdapter.OnOrderClickListener listener;

    public OrderItemAdapter(ArrayList<OrderFragment.Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }


    /**
     * Function creates a new OrderItemViewHolder
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return new OrderItemViewHolder that holds a view of the given view type.
     */
    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_layout, parent, false);
        return new OrderItemViewHolder(view);
    }

    /**
     * Function binds the data to the views
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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


    /**
     * Function returns the number of items in the list
     *
     * @return number of items in the list
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }


    public interface OnOrderClickListener {
        void onOrderClick(OrderFragment.Order order);
    }


    /**
     * Function Forces  a refresh of the adapter every 1 minute
     */
    public void startTimer() {
        android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sortByDeadline();
                handler.postDelayed(this, 60000);
            }
        }, 60000);
    }


    /**
     * Function sorts the orders by deadline
     */
    public void sortByDeadline() {
        java.util.Collections.sort(orders, new java.util.Comparator<OrderFragment.Order>() {
            @Override
            public int compare(OrderFragment.Order o1, OrderFragment.Order o2) {
                if (o1.deadline == null && o2.deadline == null) return 0;
                if (o1.deadline == null) return 1;
                if (o2.deadline == null) return -1;

                return o1.deadline.compareTo(o2.deadline);
            }
        });
        notifyDataSetChanged();
    }

    /**
     * @author  Daniel Azar
     * @version 1.0
     *
     * ViewHolder for the order list
     */
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTableID;


        /**
         * Function initializes the views
         *
         * @param itemView the view of the item
         */
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTableID = itemView.findViewById(R.id.tvTable);
        }

        /**
         * Function binds the data to the views
         *
         * @param Time the time of the order
         * @param tableID the table number of the order
         */
        void bind(String Time, String tableID) {
            tvTime.setText(Time);
            tvTableID.setText(tableID);
        }

    }
}
