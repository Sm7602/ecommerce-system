package com.ecommerce.products;

/**
 * Books. Carry author, ISBN and page count,
 * and receive a flat 10% discount.
 */
public class BookProduct extends Product {

    private String author;
    private String isbn;
    private int pages;

    public BookProduct(String id, String name, double price, String description,
                       int stockQuantity, String author, String isbn, int pages) {
        super(id, name, price, description, stockQuantity);
        this.author = author;
        this.isbn = isbn;
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public double calculateDiscount() {
        // Books get 10% discount
        return price * 0.10;
    }

    @Override
    public String getType() {
        return "Book";
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: " + getType());
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Pages: " + pages);
        System.out.println("-".repeat(40));
    }
}
