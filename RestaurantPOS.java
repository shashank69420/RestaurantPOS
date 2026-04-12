import javax.swing.*;
import java.math.BigDecimal;
import java.awt.Color;
import java.awt.Font;

/**
 * RestaurantPOS — Entry point for L'Etoile Fine Dining POS system.
 * Initializes 30 menu items (Western + Eastern cuisine), categorized inventory,
 * 15 tables, and launches the Manager Dashboard GUI.
 *
 * OOP Design Patterns demonstrated across the project:
 *   - Encapsulation    (all model classes with private fields)
 *   - Abstraction      (StaffMember abstract class)
 *   - Inheritance      (Admin, Chef, Waiter extend StaffMember)
 *   - Polymorphism     (getRole() overrides, custom Swing renderers)
 *   - Composition      (Order has OrderItems, Kitchen has KitchenOrders)
 *   - State Pattern    (OrderState, TableState enums driving behaviour)
 */
public class RestaurantPOS {

    // ── Theme Constants ──
    public static final Color BG_DARK       = new Color(15, 15, 15);
    public static final Color SURFACE       = new Color(28, 28, 32);
    public static final Color SURFACE_LIGHT = new Color(42, 42, 48);
    public static final Color ACCENT_GOLD   = new Color(212, 175, 55);
    public static final Color ACCENT_WARM   = new Color(232, 195, 75);
    public static final Color TEXT_PRIMARY   = new Color(245, 245, 245);
    public static final Color TEXT_SECONDARY = new Color(160, 160, 170);
    public static final Color SUCCESS_GREEN  = new Color(72, 199, 142);
    public static final Color WARNING_AMBER  = new Color(255, 183, 77);
    public static final Color DANGER_RED     = new Color(239, 83, 80);
    public static final Color VEG_GREEN      = new Color(56, 176, 0);
    public static final Color NON_VEG_RED    = new Color(200, 50, 50);

    public static final Font FONT_LOGO    = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBHEAD = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);

    public static void main(String[] args) {

        // ══════════════════════════════════════════
        //  MENU — 30 items, 6 categories
        // ══════════════════════════════════════════
        Menu menu = new Menu();

        // ── Appetizers & Soups (6) ──
        menu.addItem(new MenuItem("Lobster Bisque",          new BigDecimal("18.00"), "Appetizers & Soups", false));
        menu.addItem(new MenuItem("Truffle Arancini",        new BigDecimal("16.50"), "Appetizers & Soups", true));
        menu.addItem(new MenuItem("Tuna Tartare",            new BigDecimal("22.00"), "Appetizers & Soups", false));
        menu.addItem(new MenuItem("French Onion Soup",       new BigDecimal("14.00"), "Appetizers & Soups", true));
        menu.addItem(new MenuItem("Tom Yum Goong",           new BigDecimal("16.00"), "Appetizers & Soups", false));
        menu.addItem(new MenuItem("Edamame",                 new BigDecimal("9.00"),  "Appetizers & Soups", true));

        // ── Western Mains (6) ──
        menu.addItem(new MenuItem("Wagyu Ribeye Steak",      new BigDecimal("89.00"), "Western Mains", false));
        menu.addItem(new MenuItem("Truffle Burger",          new BigDecimal("28.00"), "Western Mains", false));
        menu.addItem(new MenuItem("Pan-Seared Salmon",       new BigDecimal("38.00"), "Western Mains", false));
        menu.addItem(new MenuItem("Duck Confit",             new BigDecimal("42.00"), "Western Mains", false));
        menu.addItem(new MenuItem("Wild Mushroom Risotto",   new BigDecimal("32.00"), "Western Mains", true));
        menu.addItem(new MenuItem("Eggplant Parmigiana",     new BigDecimal("26.00"), "Western Mains", true));

        // ── Eastern Mains (6) ──
        menu.addItem(new MenuItem("Butter Chicken",          new BigDecimal("24.00"), "Eastern Mains", false));
        menu.addItem(new MenuItem("Paneer Tikka Masala",     new BigDecimal("22.00"), "Eastern Mains", true));
        menu.addItem(new MenuItem("Pad Thai",                new BigDecimal("20.00"), "Eastern Mains", false));
        menu.addItem(new MenuItem("Japanese Katsu Curry",    new BigDecimal("26.00"), "Eastern Mains", false));
        menu.addItem(new MenuItem("Szechuan Mapo Tofu",      new BigDecimal("19.00"), "Eastern Mains", true));
        menu.addItem(new MenuItem("Korean Bibimbap",         new BigDecimal("23.00"), "Eastern Mains", false));

        // ── Sides (6) ──
        menu.addItem(new MenuItem("Truffle Pommes Frites",   new BigDecimal("14.00"), "Sides", true));
        menu.addItem(new MenuItem("Grilled Asparagus",       new BigDecimal("12.00"), "Sides", true));
        menu.addItem(new MenuItem("Garlic Butter Prawns",    new BigDecimal("16.00"), "Sides", false));
        menu.addItem(new MenuItem("Garlic Naan",             new BigDecimal("6.00"),  "Sides", true));
        menu.addItem(new MenuItem("Steamed Jasmine Rice",    new BigDecimal("5.00"),  "Sides", true));
        menu.addItem(new MenuItem("Miso Soup",               new BigDecimal("7.00"),  "Sides", true));

        // ── Desserts (6) ──
        menu.addItem(new MenuItem("Creme Brulee",            new BigDecimal("15.00"), "Desserts", true));
        menu.addItem(new MenuItem("Chocolate Fondant",       new BigDecimal("17.00"), "Desserts", true));
        menu.addItem(new MenuItem("Tiramisu",                new BigDecimal("14.50"), "Desserts", true));
        menu.addItem(new MenuItem("Matcha Panna Cotta",      new BigDecimal("14.00"), "Desserts", true));
        menu.addItem(new MenuItem("Mango Sticky Rice",       new BigDecimal("12.00"), "Desserts", true));
        menu.addItem(new MenuItem("Gulab Jamun",             new BigDecimal("10.00"), "Desserts", true));

        // ── Beverages (4) ──
        menu.addItem(new MenuItem("Sparkling Water",         new BigDecimal("6.00"),  "Beverages", true));
        menu.addItem(new MenuItem("Espresso",                new BigDecimal("5.00"),  "Beverages", true));
        menu.addItem(new MenuItem("Fresh Orange Juice",      new BigDecimal("8.00"),  "Beverages", true));
        menu.addItem(new MenuItem("Masala Chai",             new BigDecimal("6.00"),  "Beverages", true));

        // ══════════════════════════════════════════
        //  INVENTORY — categorized ingredients
        // ══════════════════════════════════════════
        Inventory inv = new Inventory();

        // Proteins
        inv.addStock("Lobster Shells",     30, "Proteins");
        inv.addStock("Sushi-Grade Tuna",   20, "Proteins");
        inv.addStock("Wagyu A5 Cuts",      15, "Proteins");
        inv.addStock("Ground Beef Patty",  40, "Proteins");
        inv.addStock("Atlantic Salmon",    25, "Proteins");
        inv.addStock("Duck Legs",          20, "Proteins");
        inv.addStock("King Prawns",        25, "Proteins");
        inv.addStock("Chicken Thighs",     40, "Proteins");
        inv.addStock("Paneer Block",       30, "Proteins");
        inv.addStock("Pork Katsu Cutlet",  25, "Proteins");
        inv.addStock("Firm Tofu",          30, "Proteins");
        inv.addStock("Minced Pork",        25, "Proteins");
        inv.addStock("Beef Bulgogi",       20, "Proteins");
        inv.addStock("Tiger Prawns",       25, "Proteins");

        // Dairy & Eggs
        inv.addStock("Heavy Cream",        60, "Dairy & Eggs");
        inv.addStock("Mozzarella",         35, "Dairy & Eggs");
        inv.addStock("Gruyere Cheese",     30, "Dairy & Eggs");
        inv.addStock("Parmesan",           35, "Dairy & Eggs");
        inv.addStock("Butter",             50, "Dairy & Eggs");
        inv.addStock("Eggs",               60, "Dairy & Eggs");
        inv.addStock("Mascarpone",         25, "Dairy & Eggs");
        inv.addStock("Yogurt",             30, "Dairy & Eggs");

        // Produce
        inv.addStock("Yellow Onions",      50, "Produce");
        inv.addStock("Avocado",            25, "Produce");
        inv.addStock("Potatoes",           80, "Produce");
        inv.addStock("Asparagus Bundles",  25, "Produce");
        inv.addStock("Wild Mushrooms",     30, "Produce");
        inv.addStock("Eggplant",           20, "Produce");
        inv.addStock("Garlic",             60, "Produce");
        inv.addStock("Lemon",              50, "Produce");
        inv.addStock("Oranges",            40, "Produce");
        inv.addStock("Lemongrass",         30, "Produce");
        inv.addStock("Thai Chili",         35, "Produce");
        inv.addStock("Edamame Pods",       30, "Produce");
        inv.addStock("Bean Sprouts",       25, "Produce");
        inv.addStock("Mango",              25, "Produce");
        inv.addStock("Ginger",             40, "Produce");
        inv.addStock("Spinach",            30, "Produce");
        inv.addStock("Scallions",          40, "Produce");

        // Grains & Noodles
        inv.addStock("Arborio Rice",       40, "Grains & Noodles");
        inv.addStock("Brioche Buns",       40, "Grains & Noodles");
        inv.addStock("Beef Baguette",      40, "Grains & Noodles");
        inv.addStock("Ladyfingers",        30, "Grains & Noodles");
        inv.addStock("Jasmine Rice",       50, "Grains & Noodles");
        inv.addStock("Rice Noodles",       35, "Grains & Noodles");
        inv.addStock("Naan Dough",         40, "Grains & Noodles");
        inv.addStock("Japanese Rice",      40, "Grains & Noodles");
        inv.addStock("Sticky Rice",        25, "Grains & Noodles");

        // Sauces & Condiments
        inv.addStock("Truffle Oil",        25, "Sauces & Condiments");
        inv.addStock("Olive Oil",          50, "Sauces & Condiments");
        inv.addStock("Sesame Oil",         30, "Sauces & Condiments");
        inv.addStock("Peppercorn Sauce",   20, "Sauces & Condiments");
        inv.addStock("Capers",             30, "Sauces & Condiments");
        inv.addStock("Marinara Sauce",     25, "Sauces & Condiments");
        inv.addStock("Tikka Masala Paste", 30, "Sauces & Condiments");
        inv.addStock("Coconut Milk",       35, "Sauces & Condiments");
        inv.addStock("Soy Sauce",          40, "Sauces & Condiments");
        inv.addStock("Fish Sauce",         30, "Sauces & Condiments");
        inv.addStock("Gochujang Paste",    20, "Sauces & Condiments");
        inv.addStock("Szechuan Pepper Oil",25, "Sauces & Condiments");
        inv.addStock("Katsu Curry Sauce",  25, "Sauces & Condiments");
        inv.addStock("Tamarind Paste",     20, "Sauces & Condiments");
        inv.addStock("Peanuts",            30, "Sauces & Condiments");

        // Herbs, Spices & Aromatics
        inv.addStock("Thyme",              40, "Herbs & Spices");
        inv.addStock("Vanilla Beans",      20, "Herbs & Spices");
        inv.addStock("Garam Masala",       30, "Herbs & Spices");
        inv.addStock("Matcha Powder",      20, "Herbs & Spices");
        inv.addStock("Cardamom",           25, "Herbs & Spices");
        inv.addStock("Rose Syrup",         20, "Herbs & Spices");
        inv.addStock("Chai Spice Blend",   25, "Herbs & Spices");

        // Bakery & Beverages
        inv.addStock("Dark Chocolate",     30, "Bakery & Beverages");
        inv.addStock("Espresso Beans",     40, "Bakery & Beverages");
        inv.addStock("San Pellegrino",     50, "Bakery & Beverages");
        inv.addStock("Tea Leaves",         35, "Bakery & Beverages");
        inv.addStock("Milk",               40, "Bakery & Beverages");

        // ══════════════════════════════════════════
        //  RECIPES (30 menu items)
        // ══════════════════════════════════════════

        // -- Appetizers & Soups --
        inv.addRecipeIngredient("lobster bisque",          "Lobster Shells",    2);
        inv.addRecipeIngredient("lobster bisque",          "Heavy Cream",       1);

        inv.addRecipeIngredient("truffle arancini",        "Arborio Rice",      1);
        inv.addRecipeIngredient("truffle arancini",        "Truffle Oil",       1);
        inv.addRecipeIngredient("truffle arancini",        "Mozzarella",        1);

        inv.addRecipeIngredient("tuna tartare",            "Sushi-Grade Tuna",  1);
        inv.addRecipeIngredient("tuna tartare",            "Sesame Oil",        1);
        inv.addRecipeIngredient("tuna tartare",            "Avocado",           1);

        inv.addRecipeIngredient("french onion soup",       "Yellow Onions",     3);
        inv.addRecipeIngredient("french onion soup",       "Gruyere Cheese",    1);
        inv.addRecipeIngredient("french onion soup",       "Beef Baguette",     1);

        inv.addRecipeIngredient("tom yum goong",           "Tiger Prawns",      3);
        inv.addRecipeIngredient("tom yum goong",           "Lemongrass",        1);
        inv.addRecipeIngredient("tom yum goong",           "Thai Chili",        1);
        inv.addRecipeIngredient("tom yum goong",           "Fish Sauce",        1);

        inv.addRecipeIngredient("edamame",                 "Edamame Pods",      2);

        // -- Western Mains --
        inv.addRecipeIngredient("wagyu ribeye steak",      "Wagyu A5 Cuts",     1);
        inv.addRecipeIngredient("wagyu ribeye steak",      "Peppercorn Sauce",  1);

        inv.addRecipeIngredient("truffle burger",          "Brioche Buns",      1);
        inv.addRecipeIngredient("truffle burger",          "Ground Beef Patty", 1);
        inv.addRecipeIngredient("truffle burger",          "Truffle Oil",       1);

        inv.addRecipeIngredient("pan-seared salmon",       "Atlantic Salmon",   1);
        inv.addRecipeIngredient("pan-seared salmon",       "Lemon",             1);
        inv.addRecipeIngredient("pan-seared salmon",       "Capers",            1);
        inv.addRecipeIngredient("pan-seared salmon",       "Butter",            1);

        inv.addRecipeIngredient("duck confit",             "Duck Legs",         1);
        inv.addRecipeIngredient("duck confit",             "Thyme",             1);
        inv.addRecipeIngredient("duck confit",             "Garlic",            2);

        inv.addRecipeIngredient("wild mushroom risotto",   "Wild Mushrooms",    2);
        inv.addRecipeIngredient("wild mushroom risotto",   "Arborio Rice",      1);
        inv.addRecipeIngredient("wild mushroom risotto",   "Parmesan",          1);

        inv.addRecipeIngredient("eggplant parmigiana",     "Eggplant",          2);
        inv.addRecipeIngredient("eggplant parmigiana",     "Mozzarella",        1);
        inv.addRecipeIngredient("eggplant parmigiana",     "Marinara Sauce",    1);
        inv.addRecipeIngredient("eggplant parmigiana",     "Parmesan",          1);

        // -- Eastern Mains --
        inv.addRecipeIngredient("butter chicken",          "Chicken Thighs",    2);
        inv.addRecipeIngredient("butter chicken",          "Tikka Masala Paste", 1);
        inv.addRecipeIngredient("butter chicken",          "Heavy Cream",       1);
        inv.addRecipeIngredient("butter chicken",          "Butter",            1);
        inv.addRecipeIngredient("butter chicken",          "Garam Masala",      1);

        inv.addRecipeIngredient("paneer tikka masala",     "Paneer Block",      1);
        inv.addRecipeIngredient("paneer tikka masala",     "Tikka Masala Paste", 1);
        inv.addRecipeIngredient("paneer tikka masala",     "Yogurt",            1);

        inv.addRecipeIngredient("pad thai",                "Rice Noodles",      1);
        inv.addRecipeIngredient("pad thai",                "Tiger Prawns",      2);
        inv.addRecipeIngredient("pad thai",                "Bean Sprouts",      1);
        inv.addRecipeIngredient("pad thai",                "Tamarind Paste",    1);
        inv.addRecipeIngredient("pad thai",                "Peanuts",           1);

        inv.addRecipeIngredient("japanese katsu curry",    "Pork Katsu Cutlet", 1);
        inv.addRecipeIngredient("japanese katsu curry",    "Japanese Rice",     1);
        inv.addRecipeIngredient("japanese katsu curry",    "Katsu Curry Sauce", 1);

        inv.addRecipeIngredient("szechuan mapo tofu",      "Firm Tofu",         2);
        inv.addRecipeIngredient("szechuan mapo tofu",      "Minced Pork",       1);
        inv.addRecipeIngredient("szechuan mapo tofu",      "Szechuan Pepper Oil",1);
        inv.addRecipeIngredient("szechuan mapo tofu",      "Soy Sauce",         1);

        inv.addRecipeIngredient("korean bibimbap",         "Japanese Rice",     1);
        inv.addRecipeIngredient("korean bibimbap",         "Beef Bulgogi",      1);
        inv.addRecipeIngredient("korean bibimbap",         "Spinach",           1);
        inv.addRecipeIngredient("korean bibimbap",         "Gochujang Paste",   1);
        inv.addRecipeIngredient("korean bibimbap",         "Eggs",              1);

        // -- Sides --
        inv.addRecipeIngredient("truffle pommes frites",   "Potatoes",          3);
        inv.addRecipeIngredient("truffle pommes frites",   "Truffle Oil",       1);

        inv.addRecipeIngredient("grilled asparagus",       "Asparagus Bundles", 1);
        inv.addRecipeIngredient("grilled asparagus",       "Olive Oil",         1);

        inv.addRecipeIngredient("garlic butter prawns",    "King Prawns",       3);
        inv.addRecipeIngredient("garlic butter prawns",    "Butter",            1);
        inv.addRecipeIngredient("garlic butter prawns",    "Garlic",            2);

        inv.addRecipeIngredient("garlic naan",             "Naan Dough",        1);
        inv.addRecipeIngredient("garlic naan",             "Garlic",            1);
        inv.addRecipeIngredient("garlic naan",             "Butter",            1);

        inv.addRecipeIngredient("steamed jasmine rice",    "Jasmine Rice",      1);

        inv.addRecipeIngredient("miso soup",               "Firm Tofu",         1);
        inv.addRecipeIngredient("miso soup",               "Scallions",         1);
        inv.addRecipeIngredient("miso soup",               "Soy Sauce",         1);

        // -- Desserts --
        inv.addRecipeIngredient("creme brulee",            "Eggs",              2);
        inv.addRecipeIngredient("creme brulee",            "Heavy Cream",       1);
        inv.addRecipeIngredient("creme brulee",            "Vanilla Beans",     1);

        inv.addRecipeIngredient("chocolate fondant",       "Dark Chocolate",    2);
        inv.addRecipeIngredient("chocolate fondant",       "Butter",            1);
        inv.addRecipeIngredient("chocolate fondant",       "Eggs",              1);

        inv.addRecipeIngredient("tiramisu",                "Mascarpone",        1);
        inv.addRecipeIngredient("tiramisu",                "Ladyfingers",       2);
        inv.addRecipeIngredient("tiramisu",                "Espresso Beans",    1);

        inv.addRecipeIngredient("matcha panna cotta",      "Heavy Cream",       1);
        inv.addRecipeIngredient("matcha panna cotta",      "Matcha Powder",     1);
        inv.addRecipeIngredient("matcha panna cotta",      "Vanilla Beans",     1);

        inv.addRecipeIngredient("mango sticky rice",       "Mango",             1);
        inv.addRecipeIngredient("mango sticky rice",       "Sticky Rice",       1);
        inv.addRecipeIngredient("mango sticky rice",       "Coconut Milk",      1);

        inv.addRecipeIngredient("gulab jamun",             "Milk",              1);
        inv.addRecipeIngredient("gulab jamun",             "Cardamom",          1);
        inv.addRecipeIngredient("gulab jamun",             "Rose Syrup",        1);

        // -- Beverages --
        inv.addRecipeIngredient("espresso",                "Espresso Beans",    1);
        inv.addRecipeIngredient("sparkling water",         "San Pellegrino",    1);
        inv.addRecipeIngredient("fresh orange juice",      "Oranges",           3);
        inv.addRecipeIngredient("masala chai",             "Tea Leaves",        1);
        inv.addRecipeIngredient("masala chai",             "Milk",              1);
        inv.addRecipeIngredient("masala chai",             "Chai Spice Blend",  1);

        // ══════════════════════════════════════════
        //  15 TABLES + KITCHEN with demo orders
        // ══════════════════════════════════════════
        Kitchen kitchen = new Kitchen();
        Table[] tables = new Table[15];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = new Table(i + 1);
        }

        // Table 1: Queued
        tables[0].seatCustomers();
        Order o1 = tables[0].getCurrentOrder();
        o1.addItem(new OrderItem(menu.findItem("wagyu ribeye steak"), 2));
        o1.addItem(new OrderItem(menu.findItem("lobster bisque"), 2));
        o1.addItem(new OrderItem(menu.findItem("truffle pommes frites"), 1));
        o1.addItem(new OrderItem(menu.findItem("sparkling water"), 2));
        o1.submit();
        tables[0].markWaitingForFood();
        kitchen.receiveOrder(new KitchenOrder(o1));

        // Table 4: Preparing
        tables[3].seatCustomers();
        Order o2 = tables[3].getCurrentOrder();
        o2.addItem(new OrderItem(menu.findItem("butter chicken"), 2));
        o2.addItem(new OrderItem(menu.findItem("garlic naan"), 3));
        o2.addItem(new OrderItem(menu.findItem("paneer tikka masala"), 1));
        o2.addItem(new OrderItem(menu.findItem("masala chai"), 2));
        o2.submit();
        tables[3].markWaitingForFood();
        KitchenOrder ko2 = new KitchenOrder(o2);
        ko2.startPreparing();
        kitchen.receiveOrder(ko2);

        // Table 7: Queued
        tables[6].seatCustomers();
        Order o3 = tables[6].getCurrentOrder();
        o3.addItem(new OrderItem(menu.findItem("pad thai"), 2));
        o3.addItem(new OrderItem(menu.findItem("tom yum goong"), 2));
        o3.addItem(new OrderItem(menu.findItem("mango sticky rice"), 2));
        o3.submit();
        tables[6].markWaitingForFood();
        kitchen.receiveOrder(new KitchenOrder(o3));

        // Table 10: Queued
        tables[9].seatCustomers();
        Order o4 = tables[9].getCurrentOrder();
        o4.addItem(new OrderItem(menu.findItem("japanese katsu curry"), 1));
        o4.addItem(new OrderItem(menu.findItem("korean bibimbap"), 1));
        o4.addItem(new OrderItem(menu.findItem("edamame"), 2));
        o4.addItem(new OrderItem(menu.findItem("matcha panna cotta"), 2));
        o4.submit();
        tables[9].markWaitingForFood();
        kitchen.receiveOrder(new KitchenOrder(o4));

        // ══════════════════════════════════════════
        //  LAUNCH SWING UI
        // ══════════════════════════════════════════
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            ManagerDashboard frame = new ManagerDashboard(menu, inv, kitchen, tables);
            frame.setVisible(true);
        });
    }
}
