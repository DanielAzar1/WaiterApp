package com.example.waiterapp.ManagerApp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.waiterapp.CategoryAdapter;
import com.example.waiterapp.FBref;
import com.example.waiterapp.MenuItem;
import com.example.waiterapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel Azar
 * @version 1.0
 *
 * Fragment for the Manager to view the rating and comments
 */
public class ManagerRatingFragment extends Fragment {
    TextView rating;
    TextView comments;
    Button refreshBtn;

    /**
     * Empty Constructor
     */
    public ManagerRatingFragment() {
        // Required empty public constructor
    }

    /**
     * Function creates a new instance of the fragment
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Function creates a new view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manager_rating, container, false);
    }

    /**
     * Function initializes the view
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rating = view.findViewById(R.id.textView9);
        comments = view.findViewById(R.id.tvSummary);
        refreshBtn = view.findViewById(R.id.button7);

        refreshBtn.setOnClickListener(this::onRefresh);
        rating.setText(String.format("%.2f", FBref.rating));
        comments.setText(String.join("\n", FBref.comments));


    }

    /**
     * Function refreshes the data when pressing on the button
     *
     * @param view The view that was clicked
     */
    public void onRefresh(View view)
    {
        rating.setText(String.format("%.2f", FBref.rating));
        comments.setText(String.join("\n", FBref.comments));

    }
}