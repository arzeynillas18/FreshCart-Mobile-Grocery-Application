package com.example.freshcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_products);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadProducts();

        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product(1, "Fresh Apples", 4.99, "Fruits", R.drawable.apple));
        productList.add(new Product(2, "Bananas", 2.99, "Fruits", R.drawable.banana));
        productList.add(new Product(3, "Milk", 3.99, "Dairy", R.drawable.milk));
        productList.add(new Product(4, "Bread", 2.49, "Bakery", R.drawable.bread));
        productList.add(new Product(5, "Eggs", 5.99, "Dairy", R.drawable.eggs));
        productList.add(new Product(6, "Chicken", 8.99, "Meat", R.drawable.chicken));
        productList.add(new Product(7, "Tomato", 8.99, "Vegetables", R.drawable.tomato));
    }

    @Override
    public void onAddToCartClick(Product product) {
        CartManager.getInstance().addToCart(product);
        Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
    }
}

