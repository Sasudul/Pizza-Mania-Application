package com.example.pizzamaniaapplication.models;

public class Staff {
    private int staffId;
    private String name;
    private String email;
    private String phone;
    // You can add more fields if needed, like position, address, etc.

    public Staff(int staffId, String name, String email, String phone) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters for all fields
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}