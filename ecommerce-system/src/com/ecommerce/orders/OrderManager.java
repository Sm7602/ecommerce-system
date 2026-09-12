package com.ecommerce.orders;

import com.ecommerce.cart.CartItem;
import com.ecommerce.cart.ShoppingCart;
import com.ecommerce.customers.Customer;
import com.ecommerce.products.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates orders at checkout, deducts the ordered quantities from stock
 * and keeps the order history for the whole system.
 */
public class OrderManager {

    private List<Order> orders;

    public OrderManager() {
        this.orders = new ArrayList<>();
    }

    public Order placeOrder(Customer customer, ShoppingCart cart) {
        Order order = new Order(customer, cart);
        reduceStock(cart);
        orders.add(order);
        return order;
    }

    private void reduceStock(ShoppingCart cart) {
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
        }
    }

    public Order findByOrderId(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equalsIgnoreCase(orderId)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getCustomerId().equalsIgnoreCase(customerId)) {
                result.add(order);
            }
        }
        return result;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
