package com.example.pizzamaniaapplication.models;

public class Pizza {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    public Pizza(int id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
