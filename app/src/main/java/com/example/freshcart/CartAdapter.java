package com.example.freshcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItem> cartItems;
    private final OnCartItemUpdateListener listener;

    public interface OnCartItemUpdateListener {
        void onQuantityChanged();
    }

    public CartAdapter(List<CartItem> cartItems, OnCartItemUpdateListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameText;
        private final TextView priceText;
        private final TextView quantityText;
        private final ImageButton decreaseButton;
        private final ImageButton increaseButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cart_item_image);
            nameText = itemView.findViewById(R.id.cart_item_name);
            priceText = itemView.findViewById(R.id.cart_item_price);
            quantityText = itemView.findViewById(R.id.text_quantity);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
            increaseButton = itemView.findViewById(R.id.button_increase);
        }

        void bind(final CartItem cartItem) {
            Product product = cartItem.getProduct();
            nameText.setText(product.getName());
            priceText.setText(String.format(Locale.getDefault(), "₱%.2f", product.getPrice()));
            imageView.setImageResource(product.getImageResourceId());
            quantityText.setText(String.valueOf(cartItem.getQuantity()));

            decreaseButton.setOnClickListener(v -> {
                if (cartItem.getQuantity() > 1) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    quantityText.setText(String.valueOf(cartItem.getQuantity()));
                    listener.onQuantityChanged();
                }
            });

            increaseButton.setOnClickListener(v -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                quantityText.setText(String.valueOf(cartItem.getQuantity()));
                listener.onQuantityChanged();
            });
        }
    }
}