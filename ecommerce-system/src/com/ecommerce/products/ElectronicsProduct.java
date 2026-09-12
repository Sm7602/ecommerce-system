package com.ecommerce.products;

/**
 * Electronics items. Carry a brand and a warranty period,
 * and receive a flat 10% discount.
 */
public class ElectronicsProduct extends Product {

    private String brand;
    private int warrantyMonths;

    public ElectronicsProduct(String id, String name, double price, String description,
                              int stockQuantity, String brand, int warrantyMonths) {
        super(id, name, price, description, stockQuantity);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public double calculateDiscount() {
        // Electronics get 10% discount
        return price * 0.10;
    }

    @Override
    public String getType() {
        return "Electronics";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: " + getType());
        System.out.println("Brand: " + brand);
        System.out.println("Warranty: " + warrantyMonths + " months");
        System.out.println("-".repeat(40));
    }
}
