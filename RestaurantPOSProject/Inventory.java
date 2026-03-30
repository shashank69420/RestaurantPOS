import java.util.*;

class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();
    private final Map<String, String> itemToIngredient = new HashMap<>();
    public void addStock(String ingredient, int qty)      { stock.put(ingredient, qty); }
    public void linkItem(String item, String ingredient)   { itemToIngredient.put(item, ingredient); }
    public void unlinkItem(String item)                    { itemToIngredient.remove(item); }
    public void onItemSold(String itemName, int qty, Menu menu) {
        String ingredient = itemToIngredient.get(itemName);
        if (ingredient == null) return;
        int updated = stock.getOrDefault(ingredient, 0) - qty;
        if (updated < 0) throw new IllegalStateException("Out of stock: " + ingredient);
        stock.put(ingredient, updated);
        System.out.println("  [Stock] " + ingredient + " -> " + updated);
        if (updated == 0) {
            MenuItem mi = menu.findItem(itemName);
            if (mi != null) { mi.setAvailable(false); System.out.println("  [Stock] " + itemName + " SOLD OUT!"); }
        }
    }
    public void updateStock(String ingredient, int qty) {
        if (qty < 0) throw new IllegalArgumentException("Stock cannot be negative!");
        stock.put(ingredient, qty);
        System.out.println("  [Stock Updated] " + ingredient + " -> " + qty);
    }
    public void restock(String ingredient, int qty, String itemName, Menu menu) {
        stock.put(ingredient, stock.getOrDefault(ingredient, 0) + qty);
        System.out.println("  [Restock] " + ingredient + " -> " + stock.get(ingredient));
        MenuItem mi = menu.findItem(itemName);
        if (mi != null && !mi.isAvailable()) { mi.setAvailable(true); System.out.println("  [Restock] " + itemName + " now AVAILABLE"); }
    }
    public void printStock() { for (Map.Entry<String, Integer> e : stock.entrySet()) System.out.println("  " + e.getKey() + ": " + e.getValue() + (e.getValue() == 0 ? " [OUT]" : "")); }
}
