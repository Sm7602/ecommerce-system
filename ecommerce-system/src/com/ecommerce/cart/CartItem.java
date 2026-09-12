package com.ecommerce.cart;

import com.ecommerce.products.Product;

/**
 * One line in the shopping cart: a product plus the quantity ordered.
 * The product is held as the abstract type, so the line total works
 * for electronics, clothing and books alike.
 */
public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return product.getFinalPrice() * quantity;
    }
}
