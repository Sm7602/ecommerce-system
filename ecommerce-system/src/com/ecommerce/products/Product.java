package com.ecommerce.products;

/**
 * Abstract base class for every product sold by the system.
 * Holds the attributes common to all product types and leaves the
 * discount rule abstract so each concrete type defines its own.
 */
public abstract class Product {

    protected String id;
    protected String name;
    protected double price;
    protected String description;
    protected int stockQuantity;

    public Product(String id, String name, double price, String description, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    // Abstract method - each product type calculates discount differently
    public abstract double calculateDiscount();

    // Abstract method - each product type reports its own category label
    public abstract String getType();

    // Concrete method - same for every product type
    public double getFinalPrice() {
        return price - calculateDiscount();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int quantity) {
        this.stockQuantity = quantity;
    }

    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.printf("Price: ₹%.2f%n", price);
        System.out.println("Description: " + description);
        System.out.println("Stock: " + stockQuantity);
        System.out.printf("Discount: ₹%.2f%n", calculateDiscount());
        System.out.printf("Final Price: ₹%.2f%n", getFinalPrice());
    }
}
