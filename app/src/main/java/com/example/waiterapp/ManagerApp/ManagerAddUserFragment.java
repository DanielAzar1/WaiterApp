package com.example.waiterapp.ManagerApp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.waiterapp.FBref;
import com.example.waiterapp.R;
import com.example.waiterapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * @author Daniel Azar
 * @version 1.0
 *
 * Fragment for the Manager to add a new waiter
 */
public class ManagerAddUserFragment extends Fragment {
    EditText ETname, ETemail, ETpass;

    final int MAX_NAME_LENGTH = 20;
    final int MAX_EMAIL_LENGTH = 50;
    final int MAX_PASS_LENGTH = 50;


    /**
     * Empty Constructor
     */
    public ManagerAddUserFragment() {
        // Required empty public constructor
    }

    /**
     * Function initializes the view
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Function creates a new view
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_add_user, container, false);
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

        ETname = view.findViewById(R.id.eTWaiterName);
        ETemail = view.findViewById(R.id.eTwaiterMail);
        ETpass = view.findViewById(R.id.eTWaiterPass);

        View btnAddWaiter = view.findViewById(R.id.buttonAddWaiter);
        btnAddWaiter.setOnClickListener(v -> onAddWaiter(v));

    }

    /**
     * Function handles the add waiter button click
     *
     * @param view The view that was clicked.
     */
    public void onAddWaiter(View view)
    {
        String FullName = ETname.getText().toString();
        String Email = ETemail.getText().toString();
        String pass = ETpass.getText().toString();

        AlertDialog.Builder adb = new AlertDialog.Builder(this.getContext());
        adb.setIcon(R.drawable.user);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        if (Email.isEmpty() || pass.isEmpty() || FullName.isEmpty())
        {
            adb.setTitle("Error!");
            adb.setMessage("Please fill all the fields!");
            AlertDialog ad = adb.create();
            ad.show();
        }
        else if (pass.length() > MAX_PASS_LENGTH)
        {
            adb.setTitle("Error!");
            adb.setMessage("Password too long!");
            AlertDialog ad = adb.create();
            ad.show();
        }
        else if (FullName.length() > MAX_NAME_LENGTH || Email.length() > MAX_EMAIL_LENGTH)
        {
            adb.setTitle("Error!");
            adb.setMessage("Name Cannot exceed 20 characters and Email Cannot exceed 50 characters!");
            AlertDialog ad = adb.create();
            ad.show();
        }
        else
        {
            ProgressDialog pd = new ProgressDialog(this.getContext());
            pd.setTitle("Connecting");
            pd.setMessage("Creating User...");
            pd.show();

            FBref.refAuth.createUserWithEmailAndPassword(Email, pass)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                String PID = task.getResult().getUser().getUid();
                                User newUser = new User(FullName, Email, PID, "Waiter");

                                FBref.userList.add(newUser);
                                FBref.refWaiters.child(PID).setValue(newUser);

                                adb.setTitle("Success!");
                                adb.setMessage("User added successfully!");

                                AlertDialog ad = adb.create();
                                ad.show();
                            } else {
                                Exception exp = task.getException();
                                if (exp instanceof FirebaseAuthWeakPasswordException) {
                                    adb.setMessage("Password too weak.\nTry a stronger password.");
                                } else if (exp instanceof FirebaseAuthUserCollisionException) {
                                    adb.setMessage("User already exists.");
                                } else if (exp instanceof FirebaseAuthInvalidCredentialsException) {
                                    adb.setMessage("Invalid email\nCheck for missing Domain or '@'.");
                                } else if (exp instanceof FirebaseNetworkException) {
                                    adb.setMessage("Network error. Please check your connection.");
                                } else {
                                    adb.setMessage("An error occurred. Please try again later.");
                                }
                                adb.setTitle("Error!");
                                AlertDialog ad = adb.create();
                                ad.show();
                            }
                        }
                    });

            //TODO: Fix when going back to the login page the app crashes
        }
    }
}