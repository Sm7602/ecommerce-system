# E-Commerce System (Java OOP)

A console-based e-commerce application built in core Java to demonstrate **inheritance, abstraction and polymorphism**. It models a product hierarchy (Electronics, Clothing, Books), a shopping cart, order management with GST, type-based discounts, and a customer management system — all organised into proper Java packages.

No external libraries, no build tool required. Plain `javac` + `java`.

---

## Project Goals

| Goal | How it is met |
|---|---|
| Abstract base type with shared state | `Product` holds id, name, price, description, stock and declares `calculateDiscount()` abstract |
| Inheritance | `ElectronicsProduct`, `ClothingProduct`, `BookProduct` extend `Product` |
| Polymorphism | The cart, catalogue and orders store `Product` references; `calculateDiscount()`, `getType()` and `displayInfo()` resolve to the subclass at runtime |
| Shopping cart | `ShoppingCart` + `CartItem` manage lines, quantities and the running total |
| Order management | `Order` snapshots the cart and applies GST; `OrderManager` places orders and keeps history |
| Type-based discounts | Electronics 10%, Clothing 15%, Books 10% — each defined inside its own class |
| Customer management | `Customer` + `CustomerManager` register shoppers and issue customer IDs |
| Package organisation | `com.ecommerce.products`, `.cart`, `.orders`, `.customers` |

---

## Project Structure

```
ecommerce-system/
├── README.md
├── data/
│   └── products.csv              # Product catalogue data source
├── docs/
│   ├── UML_CLASS_DIAGRAM.md      # Class diagram + inheritance hierarchy
│   ├── ARCHITECTURE.md           # System design, algorithms, data structures
│   └── TESTING.md                # Test cases + full run transcript
└── src/
    └── com/
        └── ecommerce/
            ├── Main.java                     # Menu-driven entry point
            ├── products/
            │   ├── Product.java              # Abstract base class
            │   ├── ElectronicsProduct.java   # 10% discount
            │   ├── ClothingProduct.java      # 15% discount
            │   ├── BookProduct.java          # 10% discount
            │   └── ProductCatalog.java       # Loads + serves the catalogue
            ├── cart/
            │   ├── CartItem.java             # One cart line
            │   └── ShoppingCart.java         # Cart operations + total
            ├── orders/
            │   ├── Order.java                # Order + GST + confirmation
            │   └── OrderManager.java         # Places orders, keeps history
            └── customers/
                ├── Customer.java             # Customer + their cart
                └── CustomerManager.java      # Registration + lookup
```

---

## Setup Instructions

### Prerequisites

- **JDK 8 or higher** (developed and tested on OpenJDK 21)
- A UTF-8 capable terminal — the app prints the ₹ symbol

Check your installation:

```bash
java -version
javac -version
```

### 1. Get the project

```bash
git clone https://github.com/shubhankar360/ecommerce-system.git
cd ecommerce-system
```

### 2. Compile

**Linux / macOS**

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

**Windows (PowerShell)**

```powershell
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

**Windows (CMD)**

```cmd
mkdir out
dir /s /B src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

### 3. Run

Run **from the project root** so that `data/products.csv` resolves:

```bash
java -cp out com.ecommerce.Main
```

On Windows CMD, enable UTF-8 first so ₹ renders correctly:

```cmd
chcp 65001
java -cp out com.ecommerce.Main
```

You can also point the app at a different catalogue file:

```bash
java -cp out com.ecommerce.Main path/to/other-catalog.csv
```

### 4. Running in an IDE

Import `src` as the source root, set the **working directory to the project root**, and run `com.ecommerce.Main`. If the working directory is wrong the app prints a clear message telling you so.

---

## How to Use

On start you register as a customer, then a six-option menu drives the session:

```
=== E-COMMERCE SYSTEM ===
1. View Products      # full catalogue grouped by type
2. Add to Cart        # by product ID, validated against stock
3. View Cart          # formatted table with line totals
4. Update Cart        # change a quantity or remove an item
5. Checkout           # places the order, adds GST, prints confirmation
6. Exit
```

---

## Business Rules

| Rule | Value |
|---|---|
| Electronics discount | 10% of list price |
| Clothing discount | 15% of list price |
| Books discount | 10% of list price |
| GST | 18%, applied to the discounted subtotal |
| Order ID | `ORD1000`, incrementing |
| Customer ID | `CUST001`, incrementing |
| Estimated delivery | Order date + 7 days |
| Stock | Deducted at checkout; add-to-cart is rejected if it exceeds stock |

**Worked example** (from the test run in `docs/TESTING.md`):

```
Smartphone X    ₹50000.00  −10%  → ₹45000.00 × 1  = ₹45000.00
Cotton T-Shirt  ₹ 1200.00  −15%  → ₹ 1020.00 × 3  = ₹ 3060.00
Java Programming ₹  800.00 −10%  → ₹  720.00 × 1  = ₹  720.00
                                     Subtotal      = ₹48780.00
                                     GST (18%)     = ₹ 8780.40
                                     Final Amount  = ₹57560.40
```

---

## Catalogue Data Format

`data/products.csv` — one product per row, header included:

```
type,id,name,price,description,stock,attr1,attr2,attr3
```

| type | attr1 | attr2 | attr3 |
|---|---|---|---|
| `ELECTRONICS` | brand | warranty (months) | *(unused)* |
| `CLOTHING` | size | colour | material |
| `BOOK` | author | ISBN | pages |

Fields must not contain commas. Unknown types and malformed rows are skipped with a message rather than crashing the load.

---

## Documentation

- [`docs/UML_CLASS_DIAGRAM.md`](docs/UML_CLASS_DIAGRAM.md) — UML class diagram, inheritance hierarchy, polymorphism examples
- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — layering, data structures, algorithms, design decisions
- [`docs/TESTING.md`](docs/TESTING.md) — test case table and the complete verified run transcript

Screenshots of your own run belong in `docs/screenshots/` — the full text transcript of a verified run is already in `docs/TESTING.md`.

---

## Author

**Shubhankar Gupta** — [@shubhankar360](https://github.com/shubhankar360)
