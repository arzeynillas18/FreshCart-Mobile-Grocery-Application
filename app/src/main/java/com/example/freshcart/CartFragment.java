package com.example.freshcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartItemUpdateListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView totalText;
    private Button checkoutButton;
    private CartManager cartManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initializeViews(view);
        setupRecyclerView();
        setupClickListeners();
        updateTotal();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_cart);
        totalText = view.findViewById(R.id.text_total);
        checkoutButton = view.findViewById(R.id.button_checkout);
        cartManager = CartManager.getInstance();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CartAdapter(cartManager.getCartItems(), this);
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        checkoutButton.setOnClickListener(v -> processCheckout());
    }

    @Override
    public void onQuantityChanged() {
        updateTotal();
    }

    private void updateTotal() {
        totalText.setText(String.format(Locale.getDefault(), "Total: ₱%.2f", cartManager.getTotal()));
    }

    private void processCheckout() {
        if (cartManager.getCartItems().isEmpty()) {
            showToast("Cart is empty");
            return;
        }

        showToast("Order placed successfully!");
        cartManager.clearCart();
        adapter.notifyDataSetChanged();
        updateTotal();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}