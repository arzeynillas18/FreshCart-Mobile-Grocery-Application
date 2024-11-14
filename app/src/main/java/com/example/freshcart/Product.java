package com.example.freshcart;

public class Product {
    private int id;
    private String name;
    private double price;
    private String category;
    private int imageResourceId;

    public Product(int id, String name, double price, String category, int imageResourceId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageResourceId = imageResourceId;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getImageResourceId() { return imageResourceId; }
}
