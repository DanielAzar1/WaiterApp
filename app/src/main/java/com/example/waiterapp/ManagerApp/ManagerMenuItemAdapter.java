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

/**
 * @author Daniel Azar
 * @version 1.0
 *
 * Class is an adapter for the Manager to view the menu
 */
public class ManagerMenuItemAdapter extends RecyclerView.Adapter<ManagerMenuItemAdapter.MenuItemViewHolder>{
    private List<MenuItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(MenuItem item);
    }

    /**
     * Function initializes the adapter
     *
     * @param listener The listener for the item click events
     * @param items The list of items to be displayed
     */
    public ManagerMenuItemAdapter(OnItemClickListener listener, List<MenuItem> items) {
        this.listener = listener;
        this.items = items;
    }

    /**
     * Function creates a new view
     *
     * @param parent The parent view group
     * @param viewType The view type
     */
    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_menu_item_layout, parent, false);
        return new MenuItemViewHolder(view);
    }

    /**
     * Function binds the data to the views
     *
     * @param holder The view holder
     * @param position The position of the item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    /**
     * Function returns the number of items in the list
     */
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * Function updates the data in the adapter
     *
     * @param items The new list of items to be displayed
     */
    public void setItems(List<MenuItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * @author Daniel Azar
     * @version 1.0
     *
     * Class is a view holder for the Manager to view the menu
     */
    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName;
        private final TextView tvItemDescription;
        private final TextView tvItemPrice;
        private final ImageView ivItemImage;
        private final ImageButton ibDelete;

        /**
         * Function initializes the views
         *
         * @param itemView The view for the item
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
         * Function binds the data to the views
         *
         * @param item The item to be displayed
         * @param listener  The listener for the item click events
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