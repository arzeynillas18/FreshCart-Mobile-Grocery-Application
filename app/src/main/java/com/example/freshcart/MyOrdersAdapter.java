package com.example.freshcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder> {
    private List<Order> orders;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public MyOrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderDate;
        private final TextView orderItems;
        private final TextView orderTotal;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.text_order_date);
            orderItems = itemView.findViewById(R.id.text_order_items);
            orderTotal = itemView.findViewById(R.id.text_order_total);
        }

        public void bind(Order order) {
            // Convert Firestore Timestamp to Date
            Date orderDateValue = order.getTimestamp().toDate(); // Convert to Date object

            // Format the Date object to String
            String formattedDate = dateFormat.format(orderDateValue); // Format the date

            // Set the formatted date to the TextView
            orderDate.setText(formattedDate); // Display the formatted date in the TextView

            // Build items text
            StringBuilder itemsText = new StringBuilder();
            for (Order.OrderItem item : order.getItems()) { // Use Order.OrderItem here
                itemsText.append(item.getProductName())
                        .append(" (")
                        .append(item.getQuantity())
                        .append(")\n");
            }
            orderItems.setText(itemsText.toString().trim());

            // Set total
            orderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));
        }
    }
}
