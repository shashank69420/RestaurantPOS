class Admin extends StaffMember {
    public Admin(String name, int id) { super(name, id); }
    @Override public String getRole() { return "Admin"; }
    public void addMenuItem(Menu menu, String name, double price, String category) {
        menu.addItem(new MenuItem(name, price, category));
        System.out.println("[Admin] Added: " + name + " ($" + String.format("%.2f", price) + ") to " + category);
    }
    public void removeMenuItem(Menu menu, String name, Inventory inv) {
        menu.removeItem(name);
        inv.unlinkItem(name);
        System.out.println("[Admin] Removed: " + name);
    }
    public void changePrice(Menu menu, String name, double newPrice) {
        menu.updateItemPrice(name, newPrice);
        System.out.println("[Admin] Price updated: " + menu.findItem(name));
    }
    public void updateStock(Inventory inv, String ingredient, int qty) { inv.updateStock(ingredient, qty); }
    public void restock(Inventory inv, Menu menu, String ingredient, String itemName, int qty) { inv.restock(ingredient, qty, itemName, menu); }
    public void viewInventory(Inventory inv) { System.out.println("[Admin] Current Inventory:"); inv.printStock(); }
}
