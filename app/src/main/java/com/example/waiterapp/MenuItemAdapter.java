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

/**
 * Class is the adapter for the menu items
 *
 * @author Daniel Azar
 * */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> items;
    private final OnItemClickListener listener;

    /**
     * Interface for handling item clicks
     * */
    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    /**
     * Function initializes the adapter
     *
     * @param items the list of items to display
     * @param listener the listener to handle item clicks
     */
    public MenuItemAdapter(ArrayList<MenuItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /**
     * Function creates a new view
     *
     * @param parent the parent view
     * @param viewType the view type
     * @return the new view
     */
    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view by inflating the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_layout, parent, false);
        return new MenuItemViewHolder(view);
    }

    /**
     * Function binds the data to the views
     * @param holder the view holder
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        // Get the item at the current position and bind its data to the holder's views
        MenuItem currentItem = items.get(position);
        holder.bind(currentItem, listener);
    }

    /**
     * Function returns the number of items in the list
     * @return the number of items in the list
     */
    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return items.size();
    }

    /**
     * Function updates the data in the adapter
     *
     * @param items new list
     */
    public void setItems(List<MenuItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
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

        /**
         * Function initializes the views
         *
         * @param itemView the view to initialize
         */
        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their ID
            tvItemName = itemView.findViewById(R.id.tvFoodName);
            tvItemDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivItemImage = itemView.findViewById(R.id.ivFoodImage);
        }

        /**
         * Function binds the data to the views
         *
         * @param item the item
         * @param listener the listener
         */
        public void bind(MenuItem item, final OnItemClickListener listener) {
            tvItemName.setText(item.getName());
            tvItemDescription.setText(item.getDescription());
            tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ivItemImage.setImageBitmap(item.getImage());

            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

    }
}
