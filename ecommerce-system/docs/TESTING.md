# Testing & Validation

All results below are captured from **real runs** of the compiled program, not hand-written examples.

**Environment:** OpenJDK 21.0.10, compiled with `javac -encoding UTF-8 -d out $(find src -name "*.java")`, run from the project root as `java -cp out com.ecommerce.Main`.

**Compilation result:** 0 errors, 0 warnings. 12 class files produced.

---

## 1. Test Case Summary

| # | Test case | Input | Expected | Result |
|---|---|---|---|---|
| TC-01 | Catalogue loads from CSV | Start app | 9 products across 3 types | ✅ Pass |
| TC-02 | Polymorphic display | Menu `1` | Each type prints its own extra attributes from one loop | ✅ Pass |
| TC-03 | Electronics discount = 10% | Smartphone X, ₹50000 | Discount ₹5000.00, final ₹45000.00 | ✅ Pass |
| TC-04 | Clothing discount = 15% | Cotton T-Shirt, ₹1200 | Discount ₹180.00, final ₹1020.00 | ✅ Pass |
| TC-05 | Book discount = 10% | Java Programming, ₹800 | Discount ₹80.00, final ₹720.00 | ✅ Pass |
| TC-06 | Add item to cart | `2` → `E001` → `1` | Confirmation message, line added | ✅ Pass |
| TC-07 | Cart total across mixed types | E001×1, C001×2, B001×1 | ₹47760.00 | ✅ Pass |
| TC-08 | Invalid product ID rejected | `2` → `X999` | "No product found with ID: X999", no quantity prompt | ✅ Pass |
| TC-09 | Quantity above stock rejected | `2` → `E003` → `99` (stock 25) | "Only 25 units … are in stock", cart unchanged | ✅ Pass |
| TC-10 | Update quantity | `4` → `1` → `C001` → `3` | Line quantity becomes 3 | ✅ Pass |
| TC-11 | Total recalculated after update | After TC-10 | ₹48780.00 | ✅ Pass |
| TC-12 | GST applied to discounted subtotal | Menu `5` | Subtotal ₹48780.00, GST ₹8780.40, final ₹57560.40 | ✅ Pass |
| TC-13 | Order metadata generated | Menu `5` | `ORD1000`, status `Processing`, delivery = order date + 7 days | ✅ Pass |
| TC-14 | Cart cleared after checkout | `3` after checkout | "Your cart is empty!" | ✅ Pass |
| TC-15 | Stock deducted at checkout | `1` after checkout | E001 50→49, C001 100→97, B001 75→74 | ✅ Pass |
| TC-16 | Customer registered with generated ID | Name/email/phone | `CUST001` issued | ✅ Pass |
| TC-17 | Checkout with empty cart blocked | `5` on empty cart | "Add a product before checking out" | ✅ Pass |
| TC-18 | Non-numeric menu input handled | `abc` | "Please enter a valid number", re-prompts, no crash | ✅ Pass |
| TC-19 | Out-of-range menu choice handled | `9` | "Invalid choice. Please enter a number between 1 and 6" | ✅ Pass |
| TC-20 | Duplicate product merges into one line | `E002`×2 then `E002`×3 | Single line, quantity 5, total ₹20250.00 | ✅ Pass |
| TC-21 | Remove item from cart | `4` → `2` → `E002` | Line removed, cart empty | ✅ Pass |
| TC-22 | Clean exit | Menu `6` | Farewell message, program terminates | ✅ Pass |

**22 of 22 passed.**

---

## 2. Calculation Verification

Manual arithmetic against the program's own output for Session 1:

| Product | List price | Rule | Discount | Final price | Qty | Line total |
|---|---|---|---|---|---|---|
| Smartphone X | ₹50000.00 | Electronics 10% | ₹5000.00 | ₹45000.00 | 1 | ₹45000.00 |
| Cotton T-Shirt | ₹1200.00 | Clothing 15% | ₹180.00 | ₹1020.00 | 3 | ₹3060.00 |
| Java Programming | ₹800.00 | Book 10% | ₹80.00 | ₹720.00 | 1 | ₹720.00 |

```
Subtotal      = 45000.00 + 3060.00 + 720.00 = 48780.00
GST (18%)     = 48780.00 × 0.18            =  8780.40
Final Amount  = 48780.00 × 1.18            = 57560.40
```

Program output: `Subtotal: ₹48780.00`, `GST (18%): ₹8780.40`, `Final Amount: ₹57560.40` — **matches**.

Stock after the order — expected 49 / 97 / 74, program reported 49 / 97 / 74 — **matches**.

---

## 3. Session 1 — Full Happy Path + Validation

Covers TC-01 to TC-16. Complete unedited terminal transcript:

```text
============================================
     WELCOME TO THE E-COMMERCE STORE
============================================

--- CUSTOMER REGISTRATION ---
Enter your name: Rahul Sharma
Enter your email: rahul.sharma@example.com
Enter your phone: 9876543210
✅ Registered successfully! Your Customer ID: CUST001

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 1

=== AVAILABLE PRODUCTS ===

📱 ELECTRONICS:
ID: E001
Name: Smartphone X
Price: ₹50000.00
Description: Latest smartphone with 5G
Stock: 50
Discount: ₹5000.00
Final Price: ₹45000.00
Type: Electronics
Brand: TechBrand
Warranty: 24 months
----------------------------------------
ID: E002
Name: Wireless Earbuds
Price: ₹4500.00
Description: Noise cancelling earbuds
Stock: 120
Discount: ₹450.00
Final Price: ₹4050.00
Type: Electronics
Brand: SoundMax
Warranty: 12 months
----------------------------------------
ID: E003
Name: Laptop Pro 14
Price: ₹95000.00
Description: 14 inch laptop for developers
Stock: 25
Discount: ₹9500.00
Final Price: ₹85500.00
Type: Electronics
Brand: TechBrand
Warranty: 36 months
----------------------------------------

👕 CLOTHING:
ID: C001
Name: Cotton T-Shirt
Price: ₹1200.00
Description: 100% Cotton T-Shirt
Stock: 100
Discount: ₹180.00
Final Price: ₹1020.00
Type: Clothing
Size: M
Color: Blue
Material: Cotton
----------------------------------------
ID: C002
Name: Denim Jacket
Price: ₹3200.00
Description: Regular fit denim jacket
Stock: 40
Discount: ₹480.00
Final Price: ₹2720.00
Type: Clothing
Size: L
Color: Black
Material: Denim
----------------------------------------
ID: C003
Name: Running Shorts
Price: ₹899.00
Description: Lightweight training shorts
Stock: 80
Discount: ₹134.85
Final Price: ₹764.15
Type: Clothing
Size: S
Color: Grey
Material: Polyester
----------------------------------------

📚 BOOKS:
ID: B001
Name: Java Programming
Price: ₹800.00
Description: Learn Java from scratch
Stock: 75
Discount: ₹80.00
Final Price: ₹720.00
Type: Book
Author: John Doe
ISBN: 978-3-16-148410-0
Pages: 400
----------------------------------------
ID: B002
Name: Data Structures Made Easy
Price: ₹650.00
Description: DSA concepts with examples
Stock: 60
Discount: ₹65.00
Final Price: ₹585.00
Type: Book
Author: Jane Roy
ISBN: 978-1-23-456789-7
Pages: 320
----------------------------------------
ID: B003
Name: Clean Code Basics
Price: ₹1100.00
Description: Writing readable maintainable code
Stock: 45
Discount: ₹110.00
Final Price: ₹990.00
Type: Book
Author: A K Sharma
ISBN: 978-0-11-223344-5
Pages: 280
----------------------------------------

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: E001
Enter Quantity: 1
✅ Smartphone X added to cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: C001
Enter Quantity: 2
✅ Cotton T-Shirt added to cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: B001
Enter Quantity: 1
✅ Java Programming added to cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: X999
❌ No product found with ID: X999

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: E003
Enter Quantity: 99
❌ Only 25 units of Laptop Pro 14 are in stock.

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 3

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E001            Smartphone X         ₹45000.00  1          ₹45000.00   
C001            Cotton T-Shirt       ₹1020.00   2          ₹2040.00    
B001            Java Programming     ₹720.00    1          ₹720.00     
----------------------------------------------------------------------
Total Amount: ₹47760.00

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 4

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E001            Smartphone X         ₹45000.00  1          ₹45000.00   
C001            Cotton T-Shirt       ₹1020.00   2          ₹2040.00    
B001            Java Programming     ₹720.00    1          ₹720.00     
----------------------------------------------------------------------
Total Amount: ₹47760.00

--- UPDATE CART ---
1. Change quantity
2. Remove item
3. Back
Enter your choice: 1
Enter Product ID to update: C001
Enter new Quantity: 3
✅ Quantity updated!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 3

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E001            Smartphone X         ₹45000.00  1          ₹45000.00   
C001            Cotton T-Shirt       ₹1020.00   3          ₹3060.00    
B001            Java Programming     ₹720.00    1          ₹720.00     
----------------------------------------------------------------------
Total Amount: ₹48780.00

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 5

=== ORDER DETAILS ===
Order ID: ORD1000
Order Date: Sat Sep 12 17:24:58 UTC 2026
Customer: Rahul Sharma (CUST001)

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E001            Smartphone X         ₹45000.00  1          ₹45000.00   
C001            Cotton T-Shirt       ₹1020.00   3          ₹3060.00    
B001            Java Programming     ₹720.00    1          ₹720.00     
----------------------------------------------------------------------
Total Amount: ₹48780.00

Order Summary:
Subtotal: ₹48780.00
GST (18%): ₹8780.40
Final Amount: ₹57560.40
Thank you for your order!

🎉 ORDER CONFIRMED!
Order ID: ORD1000
Status: Processing
Estimated Delivery: 2026-09-19
You will receive email confirmation shortly.

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 3

=== SHOPPING CART ===
Your cart is empty!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 1

=== AVAILABLE PRODUCTS ===

📱 ELECTRONICS:
ID: E001
Name: Smartphone X
Price: ₹50000.00
Description: Latest smartphone with 5G
Stock: 49
Discount: ₹5000.00
Final Price: ₹45000.00
Type: Electronics
Brand: TechBrand
Warranty: 24 months
----------------------------------------
ID: E002
Name: Wireless Earbuds
Price: ₹4500.00
Description: Noise cancelling earbuds
Stock: 120
Discount: ₹450.00
Final Price: ₹4050.00
Type: Electronics
Brand: SoundMax
Warranty: 12 months
----------------------------------------
ID: E003
Name: Laptop Pro 14
Price: ₹95000.00
Description: 14 inch laptop for developers
Stock: 25
Discount: ₹9500.00
Final Price: ₹85500.00
Type: Electronics
Brand: TechBrand
Warranty: 36 months
----------------------------------------

👕 CLOTHING:
ID: C001
Name: Cotton T-Shirt
Price: ₹1200.00
Description: 100% Cotton T-Shirt
Stock: 97
Discount: ₹180.00
Final Price: ₹1020.00
Type: Clothing
Size: M
Color: Blue
Material: Cotton
----------------------------------------
ID: C002
Name: Denim Jacket
Price: ₹3200.00
Description: Regular fit denim jacket
Stock: 40
Discount: ₹480.00
Final Price: ₹2720.00
Type: Clothing
Size: L
Color: Black
Material: Denim
----------------------------------------
ID: C003
Name: Running Shorts
Price: ₹899.00
Description: Lightweight training shorts
Stock: 80
Discount: ₹134.85
Final Price: ₹764.15
Type: Clothing
Size: S
Color: Grey
Material: Polyester
----------------------------------------

📚 BOOKS:
ID: B001
Name: Java Programming
Price: ₹800.00
Description: Learn Java from scratch
Stock: 74
Discount: ₹80.00
Final Price: ₹720.00
Type: Book
Author: John Doe
ISBN: 978-3-16-148410-0
Pages: 400
----------------------------------------
ID: B002
Name: Data Structures Made Easy
Price: ₹650.00
Description: DSA concepts with examples
Stock: 60
Discount: ₹65.00
Final Price: ₹585.00
Type: Book
Author: Jane Roy
ISBN: 978-1-23-456789-7
Pages: 320
----------------------------------------
ID: B003
Name: Clean Code Basics
Price: ₹1100.00
Description: Writing readable maintainable code
Stock: 45
Discount: ₹110.00
Final Price: ₹990.00
Type: Book
Author: A K Sharma
ISBN: 978-0-11-223344-5
Pages: 280
----------------------------------------

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 6

Thank you for shopping with us, Rahul Sharma!
```

---

## 4. Session 2 — Error Handling & Cart Editing

Covers TC-17 to TC-22. Complete unedited terminal transcript:

```text
============================================
     WELCOME TO THE E-COMMERCE STORE
============================================

--- CUSTOMER REGISTRATION ---
Enter your name: Priya Nair
Enter your email: priya.nair@example.com
Enter your phone: 9123456780
✅ Registered successfully! Your Customer ID: CUST001

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 5

Your cart is empty! Add a product before checking out.

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: abc
❌ Please enter a valid number.
Enter your choice: 9
❌ Invalid choice. Please enter a number between 1 and 6.

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: E002
Enter Quantity: 2
✅ Wireless Earbuds added to cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 2

Enter Product ID to add: E002
Enter Quantity: 3
✅ Wireless Earbuds added to cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 3

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E002            Wireless Earbuds     ₹4050.00   5          ₹20250.00   
----------------------------------------------------------------------
Total Amount: ₹20250.00

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 4

=== SHOPPING CART ===
Product ID      Name                 Price      Qty        Total       
----------------------------------------------------------------------
E002            Wireless Earbuds     ₹4050.00   5          ₹20250.00   
----------------------------------------------------------------------
Total Amount: ₹20250.00

--- UPDATE CART ---
1. Change quantity
2. Remove item
3. Back
Enter your choice: 2
Enter Product ID to remove: E002
✅ Item removed from cart!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 3

=== SHOPPING CART ===
Your cart is empty!

=== E-COMMERCE SYSTEM ===
1. View Products
2. Add to Cart
3. View Cart
4. Update Cart
5. Checkout
6. Exit
Enter your choice: 6

Thank you for shopping with us, Priya Nair!
```

---

## 5. Notes

- Both sessions were driven through a pseudo-terminal, so the typed input appears inline exactly as it would when a person runs the program by hand.
- The `₹` symbol renders correctly because `Main` forces the output stream to UTF-8. On Windows CMD, run `chcp 65001` first.
- Order and customer IDs restart at `ORD1000` / `CUST001` on each launch — the counters are `static` fields held in memory, not persisted.
- Add your own screenshots of these runs to `docs/screenshots/` if your submission requires image evidence.
