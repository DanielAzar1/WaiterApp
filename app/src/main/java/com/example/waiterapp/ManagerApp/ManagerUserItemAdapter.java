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

/**
 * Adapter class for managing and displaying a list of {@link User} objects in a RecyclerView.
 * This adapter handles user data binding and provides an interface for handling deletion events.
 *
 * @author Daniel Azar
 * @version 1.0
 */
public class ManagerUserItemAdapter extends RecyclerView.Adapter<ManagerUserItemAdapter.UserViewHolder> {
    private List<User> items;
    private final OnUserClickListener listener;

    /**
     * Interface definition for a callback to be invoked when a user item is clicked.
     */
    public interface OnUserClickListener {
        /**
         * Called when the delete button for a specific user is clicked.
         *
         * @param user The user object associated with the clicked item.
         */
        void onDeleteClick(User user);
    }

    /**
     * Constructs a new ManagerUserItemAdapter.
     *
     * @param listener The listener for user click events.
     * @param items    The initial list of users to display.
     */
    public ManagerUserItemAdapter(OnUserClickListener listener, List<User> items) {
        this.listener = listener;
        this.items = items;
    }

    /**
     * Called when RecyclerView needs a new {@link UserViewHolder} of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new UserViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ManagerUserItemAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_menu_user_layout, parent, false);
        return new ManagerUserItemAdapter.UserViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ManagerUserItemAdapter.UserViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Updates the data set of the adapter and notifies it of the change.
     *
     * @param items The new list of users to be displayed.
     */
    public void setItems(List<User> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Removes a user from the adapter at the specified position.
     *
     * @param position The position of the item to be removed.
     */
    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Adds a user to the adapter at the specified position.
     *
     * @param item     The user object to be added.
     * @param position The position at which to insert the item.
     */
    public void addItem(User item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * ViewHolder class for user items in the RecyclerView.
     * Holds references to the UI components for each user row.
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUserName;
        private final TextView tvUserDescription;
        private final ImageButton ibDelete;

        /**
         * Constructs a new UserViewHolder.
         *
         * @param itemView The view representing a single row in the RecyclerView.
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
            this.tvUserDescription = itemView.findViewById(R.id.tvUserDescription);
            this.ibDelete = itemView.findViewById(R.id.btnDelete2);
        }

        /**
         * Binds the user data to the views and sets up the click listener for the delete button.
         *
         * @param user     The user data to display.
         * @param listener The listener to handle delete events.
         */
        public void bind(User user, final ManagerUserItemAdapter.OnUserClickListener listener) {
            tvUserName.setText(user.getName());
            String description = "Email: " + user.getEmail() + "\n" + "Role: " + user.getRole();
            tvUserDescription.setText(description);
            ibDelete.setOnClickListener(v -> listener.onDeleteClick(user));
        }
    }
}
