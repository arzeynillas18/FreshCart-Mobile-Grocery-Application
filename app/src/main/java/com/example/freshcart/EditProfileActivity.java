package com.example.freshcart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText;
    private Button saveButton, backButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadUserProfile();
        setSaveButtonListener();
        setBackButtonListener();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.editText_name);
        emailEditText = findViewById(R.id.editText_email);
        saveButton = findViewById(R.id.button_save);
        backButton = findViewById(R.id.button_back);
    }

    private void loadUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("user").document(userId).get().addOnSuccessListener(document -> {
            if (document.exists()) {
                nameEditText.setText(document.getString("name"));
                emailEditText.setText(document.getString("email"));
            } else {
                createDefaultUserProfile(userId);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void createDefaultUserProfile(String userId) {
        Map<String, Object> defaultProfile = new HashMap<>();
        defaultProfile.put("name", "New User");
        defaultProfile.put("email", mAuth.getCurrentUser().getEmail());

        db.collection("users").document(userId).set(defaultProfile).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Default profile created", Toast.LENGTH_SHORT).show();
            loadUserProfile();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to create default profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setSaveButtonListener() {
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUserProfile(name, email);
        });
    }

    private void updateUserProfile(String name, String email) {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("email", email);

        db.collection("user").document(userId).update(updates).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setBackButtonListener() {
        backButton.setOnClickListener(v -> {
            // Navigate back to ProfileFragment
            onBackPressed();  // Default behavior to go back to previous screen
        });
    }
}
