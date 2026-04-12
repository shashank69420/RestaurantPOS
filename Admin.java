import java.math.BigDecimal;

/**
 * Admin — Concrete StaffMember with elevated privileges to manage the menu and inventory.
 * Demonstrates: Inheritance, Polymorphism, Encapsulation, method delegation.
 */
public class Admin extends StaffMember {
    public Admin(String name, int id) { super(name, id); }

    @Override
    public String getRole() { return "Admin"; }

    public void addMenuItem(Menu menu, String name, BigDecimal price, String category, boolean vegetarian) {
        menu.addItem(new MenuItem(name, price, category, vegetarian));
        System.out.println("[Admin] Added: " + name + " ($" + price.toPlainString() + ") to " + category);
    }

    public void removeMenuItem(Menu menu, String name, Inventory inv) {
        menu.removeItem(name);
        inv.clearRecipe(name.toLowerCase());
        System.out.println("[Admin] Removed: " + name);
    }

    public void changePrice(Menu menu, String name, BigDecimal newPrice) {
        menu.updateItemPrice(name, newPrice);
        System.out.println("[Admin] Price updated: " + name + " -> $" + newPrice.toPlainString());
    }

    public void updateStock(Inventory inv, String ingredient, int qty) {
        inv.updateStock(ingredient, qty);
    }

    public void viewInventory(Inventory inv) {
        System.out.println("[Admin] Current Inventory:");
        inv.printStock();
    }
}
