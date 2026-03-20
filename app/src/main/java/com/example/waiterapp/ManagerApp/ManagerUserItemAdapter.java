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
import com.example.waiterapp.User;

import java.util.List;
import java.util.Locale;

public class ManagerUserItemAdapter extends RecyclerView.Adapter<ManagerUserItemAdapter.UserViewHolder>{
    private List<User> items;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onDeleteClick(User user);
    }
    public ManagerUserItemAdapter(OnUserClickListener listener, List<User> items)
    {
        this.listener = listener;
        this.items = items;
    }

    @NonNull
    @Override
    public ManagerUserItemAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_menu_user_layout, parent, false);
        return new ManagerUserItemAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerUserItemAdapter.UserViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Input: List<MenuItem> items
     * Output: Void
     * Function updates the data in the adapter
     */
    public void setItems(List<User> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Input: int position
     * Output: Void
     * Function removes an item from the adapter
     */
    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Input: User item, int position
     * Output: Void
     * Function adds an item to the adapter
     */
    public void addItem(User item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final TextView tvUserDescription;
        private final ImageButton ibDelete;

        /**
         * Input: View itemView
         * Output: Void
         * Function initializes the views
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
            this.tvUserDescription = itemView.findViewById(R.id.tvUserDescription);
            this.ibDelete = itemView.findViewById(R.id.btnDelete2);
        }

        /**
         * Input: User user, final OnUserClickListener listener
         * Output: Void
         * Function binds the data to the views
         */
        public void bind(User user, final ManagerUserItemAdapter.OnUserClickListener listener) {
            tvUserName.setText(user.getName());
            String description = "Email: " + user.getEmail() + "\n" + "Role: " + user.getRole();
            tvUserDescription.setText(description);
            ibDelete.setOnClickListener(v -> listener.onDeleteClick(user));
        }
    }
}
