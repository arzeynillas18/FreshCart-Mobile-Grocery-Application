package com.example.freshcart;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CartManager {
    private static final AtomicReference<CartManager> instance = new AtomicReference<>();
    private final List<CartItem> cartItems;

    private CartManager() {
        this.cartItems = Collections.synchronizedList(new ArrayList<>());
    }

    public static CartManager getInstance() {
        CartManager result = instance.get();
        if (result == null) {
            synchronized (CartManager.class) {
                result = instance.get();
                if (result == null) {
                    result = new CartManager();
                    instance.set(result);
                }
            }
        }
        return result;
    }

    public synchronized void addToCart(@NonNull Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    @NonNull
    public List<CartItem> getCartItems() {
        synchronized (cartItems) {
            return new ArrayList<>(cartItems);
        }
    }

    public double getTotal() {
        synchronized (cartItems) {
            return cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
        }
    }

    public void clearCart() {
        synchronized (cartItems) {
            cartItems.clear();
        }
    }

    public int getItemCount() {
        synchronized (cartItems) {
            return cartItems.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        }
    }

    public boolean isEmpty() {
        synchronized (cartItems) {
            return cartItems.isEmpty();
        }
    }
}