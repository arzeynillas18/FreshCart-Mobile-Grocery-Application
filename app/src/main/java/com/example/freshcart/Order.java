package com.example.freshcart;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Order {
    private String orderId;  // Firestore document ID
    private String userId;
    private Timestamp timestamp; // Firestore Timestamp for order date
    private String status;
    private double totalAmount;
    private List<OrderItem> items;
    private String deliveryAddress;
    private String paymentMethod;

    // No-argument constructor required for Firestore
    public Order() {
    }

    public Order(String userId, Timestamp timestamp, String status, double totalAmount,
                 List<OrderItem> items, String deliveryAddress, String paymentMethod) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getter and setter for orderId
    @Exclude  // This field won't be stored in Firestore
    public String getOrderId() {
        return orderId;
    }

    @Exclude
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // Regular getters and setters
    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @PropertyName("timestamp")
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("totalAmount")
    public double getTotalAmount() {
        return totalAmount;
    }

    @PropertyName("totalAmount")
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @PropertyName("items")
    public List<OrderItem> getItems() {
        return items;
    }

    @PropertyName("items")
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @PropertyName("deliveryAddress")
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    @PropertyName("deliveryAddress")
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @PropertyName("paymentMethod")
    public String getPaymentMethod() {
        return paymentMethod;
    }

    @PropertyName("paymentMethod")
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Inner class for order items
    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private double price;
        private String imageUrl;

        // No-argument constructor required for Firestore
        public OrderItem() {
        }

        public OrderItem(String productId, String productName, int quantity,
                         double price, String imageUrl) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        @PropertyName("productId")
        public String getProductId() {
            return productId;
        }

        @PropertyName("productId")
        public void setProductId(String productId) {
            this.productId = productId;
        }

        @PropertyName("productName")
        public String getProductName() {
            return productName;
        }

        @PropertyName("productName")
        public void setProductName(String productName) {
            this.productName = productName;
        }

        @PropertyName("quantity")
        public int getQuantity() {
            return quantity;
        }

        @PropertyName("quantity")
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @PropertyName("price")
        public double getPrice() {
            return price;
        }

        @PropertyName("price")
        public void setPrice(double price) {
            this.price = price;
        }

        @PropertyName("imageUrl")
        public String getImageUrl() {
            return imageUrl;
        }

        @PropertyName("imageUrl")
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
