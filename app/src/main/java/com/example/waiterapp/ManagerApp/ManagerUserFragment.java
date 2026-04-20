package com.example.waiterapp.ManagerApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.waiterapp.FBref;
import com.example.waiterapp.R;
import com.example.waiterapp.User;

import java.util.ArrayList;

/**
 * Fragment for the Manager to view, manage, and remove users from the system.
 * This fragment displays a list of users in a RecyclerView and provides
 * functionality to delete a user with a confirmation dialog.
 *
 * @author Daniel Azar
 * @version 1.0
 */
public class ManagerUserFragment extends Fragment implements ManagerUserItemAdapter.OnUserClickListener {
    ManagerUserItemAdapter adapter;
    RecyclerView rvManagerUsers;

    AlertDialog.Builder adb;

    /**
     * empty constructor
     */
    public ManagerUserFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of the fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_user, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned.
     * Initializes the RecyclerView, adapter, and the delete confirmation dialog builder.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvManagerUsers = view.findViewById(R.id.rvManagerUsers);

        rvManagerUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new ManagerUserItemAdapter(this, FBref.userList);
        rvManagerUsers.setAdapter(adapter);

        adb = new AlertDialog.Builder(this.getContext());
        adb.setTitle("Remove Waiter");
        adb.setIcon(R.drawable.trashcan);
    }

    /**
     * Handles the delete button click for a specific user.
     * Shows a confirmation dialog before removing the user from Firebase and the local list.
     *
     * @param user The {@link User} object to be deleted.
     */
    @Override
    public void onDeleteClick(User user) {
        adb.setMessage("Are you sure you want to remove " + user.getName() + "?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                FBref.refWaiters.child(user.getUID()).removeValue();
                adapter.removeItem(FBref.userList.indexOf(user));
                FBref.userList.remove(user);
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        AlertDialog ad = this.adb.create();
        ad.show();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * Refreshes the adapter to ensure the UI is up to date.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
