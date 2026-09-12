package com.ecommerce.customers;

import com.ecommerce.cart.ShoppingCart;

/**
 * A registered shopper. Each customer owns one shopping cart for the
 * duration of the session.
 */
public class Customer {

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private ShoppingCart cart;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cart = new ShoppingCart();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void displayInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }
}
