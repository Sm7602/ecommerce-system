package com.ecommerce;

import com.ecommerce.cart.ShoppingCart;
import com.ecommerce.customers.Customer;
import com.ecommerce.customers.CustomerManager;
import com.ecommerce.orders.Order;
import com.ecommerce.orders.OrderManager;
import com.ecommerce.products.Product;
import com.ecommerce.products.ProductCatalog;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Console entry point. Wires the catalogue, the customer manager and the
 * order manager together and drives the menu loop.
 *
 * Run from the project root so that data/products.csv resolves, or pass a
 * different catalogue path as the first argument.
 */
public class Main {

    private static final String DEFAULT_CATALOG_PATH = "data/products.csv";

    private static Scanner scanner = new Scanner(System.in);
    private static ProductCatalog catalog = new ProductCatalog();
    private static CustomerManager customerManager = new CustomerManager();
    private static OrderManager orderManager = new OrderManager();

    public static void main(String[] args) {
        // Force UTF-8 on the console so the ₹ symbol prints correctly
        // on terminals whose default charset is not UTF-8.
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        String catalogPath = (args.length > 0) ? args[0] : DEFAULT_CATALOG_PATH;

        try {
            catalog.loadFromFile(catalogPath);
        } catch (IOException e) {
            System.out.println("❌ Could not read the product catalogue at: " + catalogPath);
            System.out.println("   Run the program from the project root, or pass the path as an argument.");
            return;
        }

        System.out.println("=".repeat(44));
        System.out.println("     WELCOME TO THE E-COMMERCE STORE");
        System.out.println("=".repeat(44));

        Customer customer = registerCustomer();
        ShoppingCart cart = customer.getCart();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    catalog.displayAll();
                    break;
                case 2:
                    addToCart(cart);
                    break;
                case 3:
                    cart.displayCart();
                    break;
                case 4:
                    updateCart(cart);
                    break;
                case 5:
                    checkout(customer, cart);
                    break;
                case 6:
                    System.out.println("\nThank you for shopping with us, " + customer.getName() + "!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please enter a number between 1 and 6.");
            }
        }

        scanner.close();
    }

    private static Customer registerCustomer() {
        System.out.println("\n--- CUSTOMER REGISTRATION ---");
        System.out.print("Enter your name: ");
        String name = readLine();
        System.out.print("Enter your email: ");
        String email = readLine();
        System.out.print("Enter your phone: ");
        String phone = readLine();

        Customer customer = customerManager.registerCustomer(name, email, phone);
        System.out.println("✅ Registered successfully! Your Customer ID: " + customer.getCustomerId());
        return customer;
    }

    private static void printMenu() {
        System.out.println("\n=== E-COMMERCE SYSTEM ===");
        System.out.println("1. View Products");
        System.out.println("2. Add to Cart");
        System.out.println("3. View Cart");
        System.out.println("4. Update Cart");
        System.out.println("5. Checkout");
        System.out.println("6. Exit");
    }

    private static void addToCart(ShoppingCart cart) {
        System.out.print("\nEnter Product ID to add: ");
        String productId = readLine();

        Product product = catalog.findById(productId);
        if (product == null) {
            System.out.println("❌ No product found with ID: " + productId);
            return;
        }

        int quantity = readInt("Enter Quantity: ");
        if (quantity <= 0) {
            System.out.println("❌ Quantity must be at least 1.");
            return;
        }
        if (quantity > product.getStockQuantity()) {
            System.out.println("❌ Only " + product.getStockQuantity() + " units of "
                    + product.getName() + " are in stock.");
            return;
        }

        cart.addItem(product, quantity);
        System.out.println("✅ " + product.getName() + " added to cart!");
    }

    private static void updateCart(ShoppingCart cart) {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        cart.displayCart();
        System.out.println("\n--- UPDATE CART ---");
        System.out.println("1. Change quantity");
        System.out.println("2. Remove item");
        System.out.println("3. Back");
        int choice = readInt("Enter your choice: ");

        switch (choice) {
            case 1:
                System.out.print("Enter Product ID to update: ");
                String updateId = readLine();
                if (catalog.findById(updateId) == null) {
                    System.out.println("❌ No product found with ID: " + updateId);
                    return;
                }
                int quantity = readInt("Enter new Quantity: ");
                if (quantity <= 0) {
                    System.out.println("❌ Quantity must be at least 1. Use 'Remove item' to delete a line.");
                    return;
                }
                cart.updateQuantity(updateId, quantity);
                System.out.println("✅ Quantity updated!");
                break;
            case 2:
                System.out.print("Enter Product ID to remove: ");
                String removeId = readLine();
                cart.removeItem(removeId);
                System.out.println("✅ Item removed from cart!");
                break;
            case 3:
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    private static void checkout(Customer customer, ShoppingCart cart) {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty! Add a product before checking out.");
            return;
        }

        Order order = orderManager.placeOrder(customer, cart);
        order.displayOrder();
        order.displayConfirmation();
        cart.clear();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    /**
     * Reads one trimmed line. If the input stream closes (Ctrl+D, or a piped
     * script that runs out of lines) the program exits cleanly instead of
     * throwing NoSuchElementException.
     */
    private static String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("\nInput ended. Exiting.");
            scanner.close();
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }
}
