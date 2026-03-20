package com.example.waiterapp.ManagerApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterapp.MenuItem;
import com.example.waiterapp.R;

import java.util.List;
import java.util.Locale;

public class ManagerMenuItemAdapter extends RecyclerView.Adapter<ManagerMenuItemAdapter.MenuItemViewHolder>{
    private List<MenuItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(MenuItem item);
    }

    /**
     * Input: OnItemClickListener listener, List<MenuItem> items
     * Output: Void
     * Function initializes the adapter
     */
    public ManagerMenuItemAdapter(OnItemClickListener listener, List<MenuItem> items) {
        this.listener = listener;
        this.items = items;
    }

    /**
     * Input: ViewGroup parent, int viewType
     * Output: MenuItemViewHolder
     * Function creates a new view
     */
    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_menu_item_layout, parent, false);
        return new MenuItemViewHolder(view);
    }

    /**
     * Input: MenuItemViewHolder holder, int position
     * Output: Void
     * Function binds the data to the views
     */
    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    /**
     * Input: Void
     * Output: int
     * Function returns the number of items in the list
     */
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Input: List<MenuItem> items
     * Output: Void
     * Function updates the data in the adapter
     */
    public void setItems(List<MenuItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName;
        private final TextView tvItemDescription;
        private final TextView tvItemPrice;
        private final ImageView ivItemImage;
        private final ImageButton ibDelete;

        /**
         * Input: View itemView
         * Output: Void
         * Function initializes the views
         */
        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvFoodName);
            tvItemDescription = itemView.findViewById(R.id.tvFoodDescription);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            ivItemImage = itemView.findViewById(R.id.ivFoodImage);
            ibDelete = itemView.findViewById(R.id.btnDelete);
        }

        /**
         * Input: MenuItem item, final OnItemClickListener listener
         * Output: Void
         * Function binds the data to the views
         */
        public void bind(MenuItem item, final OnItemClickListener listener) {
            tvItemName.setText(item.getName());
            tvItemDescription.setText(item.getDescription());
            tvItemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
            ivItemImage.setImageBitmap(item.getImage());

            ibDelete.setOnClickListener(v -> listener.onDeleteClick(item));
        }
    }
}