package com.ecommerce.orders;

import com.ecommerce.cart.CartItem;
import com.ecommerce.cart.ShoppingCart;
import com.ecommerce.customers.Customer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A placed order. Takes a snapshot of the cart at checkout time so the
 * order stays intact after the live cart is emptied, then applies GST
 * on top of the already discounted cart total.
 */
public class Order {

    private static final double GST_RATE = 0.18;
    private static int orderCounter = 1000;

    private String orderId;
    private Date orderDate;
    private Customer customer;
    private ShoppingCart cart;
    private double finalAmount;
    private String status;
    private Date estimatedDelivery;

    public Order(Customer customer, ShoppingCart cart) {
        this.orderId = "ORD" + (orderCounter++);
        this.orderDate = new Date();
        this.customer = customer;
        this.cart = snapshotOf(cart);
        this.finalAmount = calculateFinalAmount();
        this.status = "Processing";
        this.estimatedDelivery = calculateDeliveryDate();
    }

    /** Copies the cart lines so later changes to the live cart cannot alter this order. */
    private ShoppingCart snapshotOf(ShoppingCart source) {
        ShoppingCart copy = new ShoppingCart();
        for (CartItem item : source.getItems()) {
            copy.addItem(item.getProduct(), item.getQuantity());
        }
        return copy;
    }

    private double calculateFinalAmount() {
        double total = cart.getTotalAmount();
        // Add 18% GST
        return total * (1 + GST_RATE);
    }

    private Date calculateDeliveryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderDate);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        return calendar.getTime();
    }

    public double getGstAmount() {
        return cart.getTotalAmount() * GST_RATE;
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayOrder() {
        System.out.println("\n=== ORDER DETAILS ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order Date: " + orderDate);
        System.out.println("Customer: " + customer.getName() + " (" + customer.getCustomerId() + ")");

        cart.displayCart();

        System.out.println("\nOrder Summary:");
        System.out.printf("Subtotal: ₹%.2f%n", cart.getTotalAmount());
        System.out.printf("GST (18%%): ₹%.2f%n", getGstAmount());
        System.out.printf("Final Amount: ₹%.2f%n", finalAmount);
        System.out.println("Thank you for your order!");
    }

    public void displayConfirmation() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("\n🎉 ORDER CONFIRMED!");
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status);
        System.out.println("Estimated Delivery: " + formatter.format(estimatedDelivery));
        System.out.println("You will receive email confirmation shortly.");
    }
}
