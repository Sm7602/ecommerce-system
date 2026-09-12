package com.ecommerce.products;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the product catalogue from data/products.csv and serves it to the
 * rest of the system. Every row is turned into the matching concrete
 * Product subclass, then stored as the abstract type - this is where the
 * polymorphic hierarchy is assembled.
 *
 * CSV format:
 * type,id,name,price,description,stock,attr1,attr2,attr3
 *   ELECTRONICS -> attr1 = brand, attr2 = warrantyMonths
 *   CLOTHING    -> attr1 = size,  attr2 = color, attr3 = material
 *   BOOK        -> attr1 = author, attr2 = isbn, attr3 = pages
 */
public class ProductCatalog {

    private List<Product> products;

    public ProductCatalog() {
        this.products = new ArrayList<>();
    }

    public void loadFromFile(String filePath) throws IOException {
        products.clear();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // skip header row
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                Product product = parseLine(line);
                if (product != null) {
                    products.add(product);
                }
            }
        }
    }

    private Product parseLine(String line) {
        String[] f = line.split(",", -1);
        if (f.length < 9) {
            System.out.println("Skipping malformed row: " + line);
            return null;
        }

        String type = f[0].trim().toUpperCase();
        String id = f[1].trim();
        String name = f[2].trim();
        double price = Double.parseDouble(f[3].trim());
        String description = f[4].trim();
        int stock = Integer.parseInt(f[5].trim());

        switch (type) {
            case "ELECTRONICS":
                return new ElectronicsProduct(id, name, price, description, stock,
                        f[6].trim(), Integer.parseInt(f[7].trim()));
            case "CLOTHING":
                return new ClothingProduct(id, name, price, description, stock,
                        f[6].trim(), f[7].trim(), f[8].trim());
            case "BOOK":
                return new BookProduct(id, name, price, description, stock,
                        f[6].trim(), f[7].trim(), Integer.parseInt(f[8].trim()));
            default:
                System.out.println("Unknown product type: " + type);
                return null;
        }
    }

    public Product findById(String productId) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getProducts() {
        return products;
    }

    /**
     * Prints the whole catalogue grouped by product type. The call to
     * displayInfo() is resolved at runtime to the subclass override -
     * one loop renders three different layouts.
     */
    public void displayAll() {
        System.out.println("\n=== AVAILABLE PRODUCTS ===");

        displaySection("\n📱 ELECTRONICS:", "Electronics");
        displaySection("\n👕 CLOTHING:", "Clothing");
        displaySection("\n📚 BOOKS:", "Book");
    }

    private void displaySection(String heading, String type) {
        System.out.println(heading);
        for (Product product : products) {
            if (product.getType().equals(type)) {
                product.displayInfo();
            }
        }
    }
}
