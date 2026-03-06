package com.example.waiterapp;import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    public MenuItemAdapter(ArrayList<MenuItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view by inflating the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_layout, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        // Get the item at the current position and bind its data to the holder's views
        MenuItem currentItem = items.get(position);
        holder.bind(currentItem, listener);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return items.size();
    }

    public void setItems(List<MenuItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    // Method to update the data in the adapter
    public void updateData(List<MenuItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // Refresh the RecyclerView
    }


    /**
     * ViewHolder holds the views for a single item in the list.
     */
    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        // Declare the views from your menu_item_layout.xml
        private final TextView tvItemName;
        private final TextView tvItemDescription;
        private final TextView tvItemPrice;
        private final ImageView ivItemImage;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their ID
            tvItemName = itemView.findViewById(R.id.tvFoodName);
            tvItemDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivItemImage = itemView.findViewById(R.id.ivFoodImage);
        }

        public void bind(MenuItem item, final OnItemClickListener listener) {
            tvItemName.setText(item.getName());
            tvItemDescription.setText(item.getDescription());
            tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ivItemImage.setImageBitmap(item.getImage());

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

    }
}
