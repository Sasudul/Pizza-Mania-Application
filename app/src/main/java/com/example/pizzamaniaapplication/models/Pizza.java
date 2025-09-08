package com.example.pizzamaniaapplication.models;

public class Pizza {
    private int pizzaId;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private boolean available;
    private String category;

    public Pizza() {}

    public Pizza(int pizzaId, String name, String description, double price, String imageUrl, boolean available, String category) {
        this.pizzaId = pizzaId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
        this.category = category;
    }

    // Getters & Setters
    public int getPizzaId() { return pizzaId; }
    public void setPizzaId(int pizzaId) { this.pizzaId = pizzaId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return "Pizza{" +
                "pizzaId=" + pizzaId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", available=" + available +
                ", category='" + category + '\'' +
                '}';
    }
}
