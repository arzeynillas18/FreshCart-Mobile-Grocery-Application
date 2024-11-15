package com.example.freshcart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView nameText;
    private TextView emailText;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {

            startActivity(new Intent(getActivity(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return view; // Return early
        }

        initializeViews(view);
        setProfileInfo();
        setLogoutListener();

        return view;
    }

    private void initializeViews(View view) {
        nameText = view.findViewById(R.id.text_name);
        emailText = view.findViewById(R.id.text_email);
        logoutButton = view.findViewById(R.id.button_logout);
    }

    private void setProfileInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String email = document.getString("email");
                            String name = document.getString("name"); // Assuming 'name' field is stored in Firestore
                            emailText.setText(email);
                            nameText.setText(name != null ? name : "No Name Available");
                        } else {
                            Toast.makeText(getContext(), "No user data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setLogoutListener() {
        logoutButton.setOnClickListener(v -> handleLogout());
    }

    private void handleLogout() {
        mAuth.signOut();
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
