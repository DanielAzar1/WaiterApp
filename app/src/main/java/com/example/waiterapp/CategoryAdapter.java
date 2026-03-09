package com.example.waiterapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // Interface to send click events back to the Activity
    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    private final ArrayList<String> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0; // Keep track of the selected item

    /**
     * Input: ArrayList<String> categories, OnCategoryClickListener listener
     * Output: Void
     * Function initializes the adapter
     */
    public CategoryAdapter(ArrayList<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    /**
     * Input: ViewGroup parent, int viewType
     * Output: CategoryViewHolder
     * Function creates a new view
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    /**
     * Input: CategoryViewHolder holder, int position
     * Output: Void
     * Function binds the data to the views
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

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        /**
         * Input: View itemView
         * Output: Void
         * Function initializes the views
         */
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        /**
         * Input: String category, boolean isSelected
         * Output: Void
         * Function binds the data to the views
         */
        void bind(String category, boolean isSelected) {
            tvCategoryName.setText(category);
            // This triggers the state change in your drawable and color selectors
            itemView.setSelected(isSelected);
        }
    }
}
