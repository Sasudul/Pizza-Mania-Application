package com.example.pizzamaniaapplication.models;

public class Branch {
    private int branchId;
    private String name;
    private String location;
    private String contact;

    public Branch() {}

    public Branch(int branchId, String name, String location, String contact) {
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    // Getters & Setters
    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
