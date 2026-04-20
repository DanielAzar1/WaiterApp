package com.example.waiterapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * Adapter class for managing and displaying food categories in a RecyclerView.
 *
 * @author Daniel Azar
 * @version 1.0
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    /**
     * Interface definition for a callback to be invoked when a category is clicked.
     */
    public interface OnCategoryClickListener {
        /**
         * Called when a category item has been clicked.
         *
         * @param category The name of the category that was clicked.
         * @param position The position of the item in the adapter.
         */
        void onCategoryClick(String category, int position);
    }

    private final ArrayList<String> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0;

    /**
     * Constructs a new CategoryAdapter.
     *
     * @param categories The list of category names to display.
     * @param listener   The listener to handle click events.
     */
    public CategoryAdapter(ArrayList<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new CategoryViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new CategoryViewHolder that holds the inflated layout.
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method also sets up the click listener to handle item selection and UI updates.
     *
     * @param holder   The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        // This line correctly sets the initial selected state (selected or not)
        holder.bind(category, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                notifyItemChanged(selectedPosition);

                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                listener.onCategoryClick(category, position);
            }
        });
    }

    /**
     * Input: Void
     * Output: int
     * Function returns the number of items in the list
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * ViewHolder class for category items.
     * Holds references to the UI components for each category row.
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        /**
         * Constructs a new CategoryViewHolder.
         *
         * @param itemView The view representing a single category item row.
         */
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        /**
         * Binds the category data to the views and updates the selection state.
         *
         * @param category   The category name to display.
         * @param isSelected True if this category is currently selected, false otherwise.
         */
        void bind(String category, boolean isSelected) {
            tvCategoryName.setText(category);
            itemView.setSelected(isSelected);
        }
    }
}
