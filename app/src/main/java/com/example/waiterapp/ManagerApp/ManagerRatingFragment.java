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

public class ManagerRatingFragment extends Fragment {
    TextView rating;
    TextView comments;
    Button refreshBtn;

    public ManagerRatingFragment() {
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
        return inflater.inflate(R.layout.fragment_manager_rating, container, false);
    }

    /**
     * Input: View view, Bundle savedInstanceState
     * Output: Void
     * Function initializes the view
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
     * Input: View view
     * Output: Void
     * Function refreshes the data when pressing on the button
     */
    public void onRefresh(View view)
    {
        rating.setText(String.format("%.2f", FBref.rating));
        comments.setText(String.join("\n", FBref.comments));

    }
}