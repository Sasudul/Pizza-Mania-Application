package com.example.pizzamaniaapplication.models;

public class Order {
    private int orderId;
    private int userId;
    private int branchId;
    private String items;             // JSON or CSV of ordered pizzas
    private double totalPrice;
    private String currentStatus;
    private String estimatedDelivery;

    public Order() {}

    public Order(int orderId, int userId, int branchId, String items,
                 double totalPrice, String currentStatus, String estimatedDelivery) {
        this.orderId = orderId;
        this.userId = userId;
        this.branchId = branchId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.currentStatus = currentStatus;
        this.estimatedDelivery = estimatedDelivery;
    }

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public String getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(String estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", branchId=" + branchId +
                ", items='" + items + '\'' +
                ", totalPrice=" + totalPrice +
                ", currentStatus='" + currentStatus + '\'' +
                ", estimatedDelivery='" + estimatedDelivery + '\'' +
                '}';
    }
}
