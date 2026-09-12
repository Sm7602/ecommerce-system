package com.ecommerce.products;

/**
 * Apparel items. Carry size, colour and material,
 * and receive a flat 15% discount.
 */
public class ClothingProduct extends Product {

    private String size;
    private String color;
    private String material;

    public ClothingProduct(String id, String name, double price, String description,
                           int stockQuantity, String size, String color, String material) {
        super(id, name, price, description, stockQuantity);
        this.size = size;
        this.color = color;
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    @Override
    public double calculateDiscount() {
        // Clothing gets 15% discount
        return price * 0.15;
    }

    @Override
    public String getType() {
        return "Clothing";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: " + getType());
        System.out.println("Size: " + size);
        System.out.println("Color: " + color);
        System.out.println("Material: " + material);
        System.out.println("-".repeat(40));
    }
}
