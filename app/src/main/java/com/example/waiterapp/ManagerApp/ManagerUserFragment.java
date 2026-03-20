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

public class ManagerUserFragment extends Fragment implements ManagerUserItemAdapter.OnUserClickListener{
    ManagerUserItemAdapter adapter;
    RecyclerView rvManagerUsers;

    AlertDialog.Builder adb;

    public ManagerUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Input: LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
     * Output: View
     * Function creates a new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_user, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
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
     * Input: User user
     * Output: Void
     * Function handles the delete button click*/
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}