# System Architecture

## 1. Layering

The system is a three-layer console application. Each layer only talks downward.

```
┌──────────────────────────────────────────────────────────────┐
│  PRESENTATION           com.ecommerce.Main                    │
│  Menu loop, all Scanner input, all input validation.          │
│  Holds no business rules.                                     │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│  SERVICE / MANAGER                                            │
│  ProductCatalog     — load and search the catalogue           │
│  CustomerManager    — register and look up customers          │
│  OrderManager       — place orders, deduct stock, history     │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│  DOMAIN / MODEL                                               │
│  Product (abstract) + Electronics / Clothing / Book           │
│  CartItem, ShoppingCart, Order, Customer                      │
│  Each object owns the rules for its own data.                 │
└───────────────────────────┬──────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│  DATA           data/products.csv                             │
└──────────────────────────────────────────────────────────────┘
```

**Why it is split this way.** `Main` can be replaced by a web or GUI front end without touching a single business rule, because no pricing, tax or stock logic lives there. Equally, the discount rule for clothing lives in exactly one place — `ClothingProduct.calculateDiscount()` — so changing 15% to 20% is a one-line edit with no search-and-replace across the codebase.

---

## 2. Data Structures

| Structure | Where | Why this one |
|---|---|---|
| `ArrayList<Product>` | `ProductCatalog.products` | Catalogue is read-heavy and iterated in order for display; random access by index is O(1) |
| `ArrayList<CartItem>` | `ShoppingCart.items` | Small, ordered, iterated on every total and display; insertion order is the display order |
| `ArrayList<Order>` | `OrderManager.orders` | Append-only history, always traversed in full or filtered |
| `ArrayList<Customer>` | `CustomerManager.customers` | Same access pattern as above |
| `static int` counters | `Order.orderCounter`, `CustomerManager.customerCounter` | Shared across all instances — the natural way to issue unique sequential IDs |

A `HashMap<String, Product>` would give O(1) lookup by ID instead of the current O(n). For a catalogue of this size the linear scan is not measurable, and `ArrayList` keeps the ordered display that the UI needs without a second structure. Swapping it in later would only touch `ProductCatalog`.

---

## 3. Key Algorithms

### 3.1 Discount → final price (polymorphic)

```
getFinalPrice() = price − calculateDiscount()

calculateDiscount() resolves at runtime:
    ElectronicsProduct → price × 0.10
    ClothingProduct    → price × 0.15
    BookProduct        → price × 0.10
```

Complexity: O(1). The branch is handled by dynamic dispatch, not by an `if`/`switch` chain — adding a type never edits existing code.

### 3.2 Add to cart (merge-or-append)

```
for each item in cart:
    if item.product.id == newProduct.id:
        item.quantity += newQuantity
        recalculate total
        return
append new CartItem(newProduct, newQuantity)
recalculate total
```

Complexity: O(n) over cart lines. Adding the same product twice produces one line with a combined quantity, never a duplicate row.

### 3.3 Cart total

```
totalAmount = Σ (item.product.getFinalPrice() × item.quantity)
```

Recomputed from scratch inside `calculateTotal()` after every add, remove and update. Complexity O(n) — chosen over incremental adjustment because a full recompute cannot drift out of sync after an edit or removal.

### 3.4 Checkout

```
1. reject if cart is empty
2. build Order:
      orderId       = "ORD" + orderCounter++
      cart          = deep-ish snapshot of the live cart lines
      finalAmount   = cartTotal × 1.18
      status        = "Processing"
      delivery date = orderDate + 7 days
3. for each cart line: product.stock −= quantity
4. append order to history
5. print order details + confirmation
6. clear the live cart
```

**Why the snapshot matters.** Step 6 empties the live cart. If `Order` held a reference to it, every past order would display as empty. `Order.snapshotOf()` copies the lines into a private `ShoppingCart`, so the order remains an accurate historical record. The `Product` objects themselves are shared by reference — intentional, so stock stays consistent system-wide.

### 3.5 GST

```
subtotal     = Σ discounted line totals
gst          = subtotal × 0.18
finalAmount  = subtotal × 1.18
```

Order of operations is deliberate: **discount first, tax second**. Tax is charged on what the customer actually pays, not on the list price.

---

## 4. Catalogue Loading

`ProductCatalog.loadFromFile()` reads `data/products.csv` line by line:

1. Skip the header row.
2. Skip blank lines.
3. `split(",", -1)` — the `-1` limit keeps trailing empty fields, so `ELECTRONICS` rows with an unused `attr3` still parse to 9 columns.
4. Switch on the `type` column to build the matching subclass.
5. A malformed or unknown row prints a message and is skipped; the rest of the catalogue still loads.

The file is read as UTF-8 explicitly rather than relying on the platform default, so the catalogue parses identically on Windows, macOS and Linux.

**Trade-off:** this is a deliberately minimal CSV reader — fields containing commas are not supported. A quoted-field parser would be needed for user-supplied data; for a fixed catalogue file it would be complexity with no payoff.

---

## 5. Design Decisions

| Decision | Alternative | Why the choice was made |
|---|---|---|
| `Product` is abstract | Concrete class with a `type` string field | The compiler forces every type to define its own discount; a string field would push the logic into `if` chains that must be edited for every new type |
| Discount lives inside each product class | A central `DiscountService` with a switch | Keeps the rule next to the data it applies to; new types need no edit to shared code |
| `Customer` owns a `ShoppingCart` | Global cart in `Main` | Models reality, and makes a multi-customer session a change in `Main` alone |
| `Order` snapshots the cart | Hold a live reference | The live cart is cleared after checkout — a reference would corrupt order history |
| `OrderManager` deducts stock, not `Order` | Deduct in the `Order` constructor | Constructors should build objects, not mutate unrelated ones; keeps the side effect visible at the call site |
| GST as a `private static final` constant | Literal `0.18` inline | Single point of change, and the rate is documented by name |
| Getters everywhere, no public fields | Public fields for brevity | Encapsulation — internal representation can change without breaking callers |
| CSV data file | Hard-coded product list in Java | Catalogue can change without recompiling; satisfies the "product catalog data" requirement |
| `System.setOut` forced to UTF-8 | Rely on the platform default | The ₹ symbol prints as `?` on terminals defaulting to a non-UTF-8 charset |

---

## 6. Error Handling

| Situation | Behaviour |
|---|---|
| `data/products.csv` missing | Clear message naming the path and the likely cause (wrong working directory), then a clean exit |
| Malformed CSV row | Row skipped with a message; load continues |
| Unknown product type in CSV | Row skipped with a message |
| Non-numeric menu input | Re-prompts until a valid integer is entered — no crash |
| Menu number outside 1–6 | "Invalid choice" message, menu redisplayed |
| Unknown product ID | "No product found with ID: …" |
| Quantity ≤ 0 | Rejected with an explanatory message |
| Quantity greater than stock | Rejected, showing the actual available quantity |
| Checkout with an empty cart | Rejected with a prompt to add a product first |
| Update or view on an empty cart | "Your cart is empty!" |
| Input stream closes (Ctrl+D, piped script ends) | "Input ended. Exiting." and a clean shutdown instead of `NoSuchElementException` |

No exception is allowed to reach the user as a stack trace.

---

## 7. Extension Points

- **New product type** — extend `Product`, implement `calculateDiscount()` and `getType()`, add one `case` to `ProductCatalog.parseLine()`.
- **Persistence** — `OrderManager` and `CustomerManager` are the only places that hold state; each could write to a file or database behind its existing method signatures.
- **Different front end** — `Main` is the only class that imports `Scanner`; swapping it for a servlet or GUI touches nothing below it.
- **Coupons or tiered discounts** — `Order.calculateFinalAmount()` is the single place where the subtotal becomes a payable amount.
