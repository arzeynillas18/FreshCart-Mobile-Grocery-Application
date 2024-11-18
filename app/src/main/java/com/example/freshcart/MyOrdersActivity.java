package com.example.freshcart;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private RecyclerView ordersRecyclerView;
    private MyOrdersAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextView noOrdersText;
    private ImageButton backButton;  // Declare the back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        initializeViews();
        checkPlayServices();
        setupFirebase();
        setupRecyclerView();
        setupBackButton();  // Initialize the back button functionality
    }

    private void initializeViews() {
        ordersRecyclerView = findViewById(R.id.recycler_orders);
        progressBar = findViewById(R.id.progress_bar);
        noOrdersText = findViewById(R.id.text_no_orders);
        backButton = findViewById(R.id.button_back);  // Initialize the back button
    }

    private void setupRecyclerView() {
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyOrdersAdapter(new ArrayList<>());
        ordersRecyclerView.setAdapter(adapter);
    }

    private void setupFirebase() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please sign in to view orders", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Enable Firestore offline persistence
        db.enableNetwork()
                .addOnSuccessListener(aVoid -> loadOrders())
                .addOnFailureListener(e -> handleError("Network error: " + e.getMessage()));
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != com.google.android.gms.common.ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void loadOrders() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();
        showLoading(true);

        // Add retry mechanism for failed queries
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    showLoading(false);
                    List<Order> orders = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        try {
                            Order order = document.toObject(Order.class);
                            order.setOrderId(document.getId()); // Save document ID
                            orders.add(order);
                        } catch (Exception e) {
                            handleError("Error parsing order: " + e.getMessage());
                        }
                    }

                    updateUI(orders);
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    handleError("Error loading orders: " + e.getMessage());
                    // Implement offline data access
                    loadOfflineOrders();
                });
    }

    private void loadOfflineOrders() {
        // Implementation for offline data access
        db.collection("orders")
                .whereEqualTo("userId", auth.getCurrentUser().getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get(com.google.firebase.firestore.Source.CACHE)
                .addOnSuccessListener(querySnapshot -> {
                    List<Order> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        orders.add(document.toObject(Order.class));
                    }
                    updateUI(orders);
                    if (!orders.isEmpty()) {
                        Toast.makeText(this, "Showing offline data", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> handleError("No offline data available"));
    }

    private void updateUI(List<Order> orders) {
        if (orders.isEmpty()) {
            ordersRecyclerView.setVisibility(View.GONE);
            noOrdersText.setVisibility(View.VISIBLE);
        } else {
            ordersRecyclerView.setVisibility(View.VISIBLE);
            noOrdersText.setVisibility(View.GONE);
            adapter.updateOrders(orders);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        ordersRecyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        noOrdersText.setVisibility(View.GONE);
    }

    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> onBackPressed()); // Handle the back button click
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (db != null) {
            db.disableNetwork();
        }
    }
}
