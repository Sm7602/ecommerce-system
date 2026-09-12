package com.ecommerce.customers;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers customers, generates their IDs and looks them up
 * for returning shoppers.
 */
public class CustomerManager {

    private static int customerCounter = 1;

    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    public Customer registerCustomer(String name, String email, String phone) {
        String customerId = String.format("CUST%03d", customerCounter++);
        Customer customer = new Customer(customerId, name, email, phone);
        customers.add(customer);
        return customer;
    }

    public Customer findById(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public Customer findByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
