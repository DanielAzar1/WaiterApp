package com.example.waiterapp;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Map<MenuItem, Integer> cartItems;
    private final OnCartItemInteractionListener listener;

    public void updateData(Map<MenuItem, Integer> cartMap) {
        this.cartItems = cartMap;
        notifyDataSetChanged();
    }

    public interface OnCartItemInteractionListener {
        void onRemoveItemClick(MenuItem item, int position);

        void onIncreaseQuantityClick(MenuItem item, int position);

        void onDecreaseQuantityClick(MenuItem item, int position);
    }

    public CartAdapter(Map<MenuItem, Integer> cartItems, OnCartItemInteractionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your new cart_item_layout.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        List<MenuItem> items = new ArrayList<>(cartItems.keySet());
        MenuItem currentItem = items.get(position);
        Integer quantity = cartItems.get(currentItem);

        holder.bind(currentItem, quantity, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public double getTotalPrice()
    {
        double totalPrice = 0.0;
        for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return totalPrice;
    }

    public void setItems(List<MenuItem> newItems) {
        this.cartItems.clear();
        for (MenuItem item : newItems) {
            this.cartItems.put(item, 1);
        }
        notifyDataSetChanged(); // This is the simplest way to refresh
    }

    /**
     * ViewHolder holds the views for a single cart item.
     */
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        // Declare the views from your cart_item_layout.xml
        private final TextView tvCartItemName;
        private final TextView tvCartItemPrice;
        private final ImageView ivCartItemImage;
        private final TextView tvCartItemQuantity;
        private final ImageButton MinusBtn;
        private final ImageButton PlusBtn;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            ivCartItemImage = itemView.findViewById(R.id.ivCartItemImage);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            MinusBtn = itemView.findViewById(R.id.MinusBtn);
            PlusBtn = itemView.findViewById(R.id.PlusBtn);
        }

        public void bind(final MenuItem item, Integer amount, final OnCartItemInteractionListener listener) {
            tvCartItemName.setText(item.getName());
            tvCartItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice() * amount));

            tvCartItemQuantity.setText(String.valueOf(amount));

            if (item.getImage() != null) {
                ivCartItemImage.setImageBitmap(item.getImage());
            }

            MinusBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onDecreaseQuantityClick(item, getAdapterPosition());
                }
            });

            PlusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onIncreaseQuantityClick(item, getAdapterPosition());
                }
            });
        }
    }
}
