package com.example.freshcart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private TextView nameText;
    private TextView emailText;
    private Button editProfileButton;
    private Button myOrdersButton;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Log.e(TAG, "User is not authenticated.");
            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return view; // Return early to avoid null pointer issues
        }

        initializeViews(view);

        if (isNetworkAvailable()) {
            setProfileInfo();
        } else {
            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        setButtonListeners();

        return view;
    }

    private void initializeViews(View view) {
        nameText = view.findViewById(R.id.text_name);
        emailText = view.findViewById(R.id.text_email);
        editProfileButton = view.findViewById(R.id.button_edit_profile);
        myOrdersButton = view.findViewById(R.id.button_my_orders);
        logoutButton = view.findViewById(R.id.button_logout);
    }

    private void setProfileInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "Fetching data for User ID: " + userId);

        // Changed the path to directly access the users collection
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Get user data
                            String email = document.getString("email");
                            String name = document.getString("name");

                            // If email is not in Firestore, use the one from Firebase Auth
                            if (email == null && mAuth.getCurrentUser() != null) {
                                email = mAuth.getCurrentUser().getEmail();
                            }

                            // Update UI
                            emailText.setText(email != null ? email : "No Email Available");
                            nameText.setText(name != null ? name : "No Name Available");
                        } else {
                            // If document doesn't exist, create it with basic info
                            createUserDocument(userId);
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load user data.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error fetching document: ", task.getException());
                    }
                });
    }

    private void createUserDocument(String userId) {
        // Create a new user document if it doesn't exist
        String email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "";
        String name = email != null ? email.substring(0, email.indexOf('@')) : "User";

        User user = new User(userId, name, email);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User document created successfully");
                    // Update UI after creating document
                    emailText.setText(email);
                    nameText.setText(name);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating user document", e);
                    Toast.makeText(getContext(), "Failed to create user profile.", Toast.LENGTH_SHORT).show();
                });
    }

    private void setButtonListeners() {
        editProfileButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EditProfileActivity.class))
        );

        myOrdersButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), MyOrdersActivity.class))
        );

        logoutButton.setOnClickListener(v -> handleLogout());
    }

    private void handleLogout() {
        mAuth.signOut();
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null &&
                cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }
}
