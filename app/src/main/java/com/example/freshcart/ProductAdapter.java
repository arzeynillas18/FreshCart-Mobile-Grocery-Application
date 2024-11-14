package com.example.freshcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(@NonNull Product product);
    }

    public ProductAdapter(@NonNull List<Product> products, @NonNull OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameText;
        private final TextView priceText;
        private final Button addToCartButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameText = itemView.findViewById(R.id.product_name);
            priceText = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.button_add_to_cart);
        }

        void bind(@NonNull Product product) {
            nameText.setText(product.getName());
            priceText.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            imageView.setImageResource(product.getImageResourceId());
            addToCartButton.setOnClickListener(v -> listener.onAddToCartClick(product));
        }
    }
}