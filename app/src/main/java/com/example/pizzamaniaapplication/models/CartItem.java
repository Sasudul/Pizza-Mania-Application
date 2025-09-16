package com.example.pizzamaniaapplication.models;

public class CartItem {
    private int cartId;
    private String pizzaName;
    private double price;
    private int quantity;
    private String imageUrl;


    public CartItem(int cartId, String pizzaName, double price, int quantity) {
        this.cartId = cartId;
        this.pizzaName = pizzaName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = null;
    }

    // Constructor with image
    public CartItem(int cartId, String pizzaName, double price, int quantity, String imageUrl) {
        this.cartId = cartId;
        this.pizzaName = pizzaName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getCartId() {
        return cartId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", pizzaName='" + pizzaName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
