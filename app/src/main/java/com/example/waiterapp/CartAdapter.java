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

/**
 * Adapter class for managing and displaying a list of items in the shopping cart.
 *
 * @author Daniel Azar
 * @version 1.0
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Map<MenuItem, Integer> cartItems;
    private final OnCartItemInteractionListener listener;

    public interface OnCartItemInteractionListener {
        /**
         * Called when the remove button for a specific item is clicked.
         *
         * @param item     The MenuItem to be removed.
         * @param position The position of the item in the list.
         */
        void onRemoveItemClick(MenuItem item, int position);

        /**
         * Called when the increase quantity button for a specific item is clicked.
         *
         * @param item     The MenuItem whose quantity is to be increased.
         * @param position The position of the item in the list.
         */
        void onIncreaseQuantityClick(MenuItem item, int position);

        /**
         * Called when the decrease quantity button for a specific item is clicked.
         *
         * @param item     The MenuItem whose quantity is to be decreased.
         * @param position The position of the item in the list.
         */
        void onDecreaseQuantityClick(MenuItem item, int position);
    }

    /**
     * Constructs a new CartAdapter.
     *
     * @param cartItems The initial map of MenuItems and their quantities.
     * @param listener  The listener for cart item interactions.
     */
    public CartAdapter(Map<MenuItem, Integer> cartItems, OnCartItemInteractionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    /**
     * Updates the adapter's data with a new cart map and refreshes the view.
     *
     * @param cartMap The new map of MenuItems and their quantities.
     */
    public void updateData(Map<MenuItem, Integer> cartMap) {
        this.cartItems.putAll(cartMap);
        notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new CartViewHolder of the given type to show an item.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new CartViewHolder that holds the inflated layout.
     */
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        List<MenuItem> items = new ArrayList<>(cartItems.keySet());
        MenuItem currentItem = items.get(position);
        Integer quantity = cartItems.get(currentItem);

        holder.bind(currentItem, quantity, listener);
    }

    /**
     * Returns the total number of items in the cart.
     *
     * @return The number of unique items in the cart.
     */
    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    /**
     * Calculates the total price of all items currently in the cart.
     *
     * @return The total price of the cart as a double.
     */
    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (Map.Entry<MenuItem, Integer> entry : cartItems.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return totalPrice;
    }

    /**
     * ViewHolder class for cart items.
     * Holds references to the UI components for each cart row.
     */
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCartItemName;
        private final TextView tvCartItemPrice;
        private final ImageView ivCartItemImage;
        private final TextView tvCartItemQuantity;
        private final ImageButton MinusBtn;
        private final ImageButton PlusBtn;

        /**
         * Constructs a new CartViewHolder.
         *
         * @param itemView The view representing a single cart item row.
         */
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            ivCartItemImage = itemView.findViewById(R.id.ivCartItemImage);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            MinusBtn = itemView.findViewById(R.id.MinusBtn);
            PlusBtn = itemView.findViewById(R.id.PlusBtn);
        }

        /**
         * Binds the menu item data to the views and sets up click listeners for the quantity buttons.
         *
         * @param item     The MenuItem to display.
         * @param amount   The current quantity of the item.
         * @param listener The listener to handle interactions.
         */
        public void bind(final MenuItem item, Integer amount, final OnCartItemInteractionListener listener) {
            tvCartItemName.setText(item.getName());
            tvCartItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice() * amount));
            tvCartItemQuantity.setText(String.valueOf(amount));

            if (item.getImage() != null) {
                ivCartItemImage.setImageBitmap(item.getImage());
            }

            MinusBtn.setOnClickListener(v -> listener.onDecreaseQuantityClick(item, getAdapterPosition()));
            PlusBtn.setOnClickListener(v -> listener.onIncreaseQuantityClick(item, getAdapterPosition()));
        }
    }
}
