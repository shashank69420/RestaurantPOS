import java.util.*;

/**
 * Inventory — Manages ingredient stock levels and multi-ingredient recipes.
 * Supports ingredient categorization and recipe lookups.
 * Demonstrates: Encapsulation, Composition (nested Maps), data integrity checks.
 */
public class Inventory {
    private final Map<String, Integer> stock = new LinkedHashMap<>();
    private final Map<String, String> ingredientCategory = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> itemRecipe = new HashMap<>();

    public void addStock(String ingredient, int qty, String category) {
        stock.put(ingredient, stock.getOrDefault(ingredient, 0) + qty);
        ingredientCategory.put(ingredient, category);
    }

    public void addRecipeIngredient(String item, String ingredient, int qtyRequired) {
        itemRecipe.putIfAbsent(item, new LinkedHashMap<>());
        itemRecipe.get(item).put(ingredient, qtyRequired);
    }

    public void clearRecipe(String item) {
        itemRecipe.remove(item);
    }

    /** Returns the recipe for a menu item, or null if none defined. */
    public Map<String, Integer> getRecipe(String itemNameLower) {
        Map<String, Integer> recipe = itemRecipe.get(itemNameLower);
        if (recipe == null) return null;
        return Collections.unmodifiableMap(recipe);
    }

    public boolean canPrepare(String itemName, int qtyToPrepare) {
        Map<String, Integer> recipe = itemRecipe.get(itemName);
        if (recipe == null || recipe.isEmpty()) return true;

        for (Map.Entry<String, Integer> req : recipe.entrySet()) {
            int currentStock = stock.getOrDefault(req.getKey(), 0);
            if (currentStock < req.getValue() * qtyToPrepare) {
                return false;
            }
        }
        return true;
    }

    public void deduceStockForOrder(String itemName, int qty) {
        Map<String, Integer> recipe = itemRecipe.get(itemName);
        if (recipe == null) return;

        if (!canPrepare(itemName, qty)) {
            throw new IllegalStateException("Not enough stock to prepare " + qty + "x " + itemName);
        }

        for (Map.Entry<String, Integer> req : recipe.entrySet()) {
            String ingredient = req.getKey();
            int required = req.getValue() * qty;
            stock.put(ingredient, stock.get(ingredient) - required);
        }
    }

    public void updateStock(String ingredient, int qty) {
        if (qty < 0) throw new IllegalArgumentException("Stock cannot be negative!");
        stock.put(ingredient, qty);
    }

    public Map<String, Integer> getStockSnapshot() {
        return Collections.unmodifiableMap(stock);
    }

    public Map<String, String> getCategorySnapshot() {
        return Collections.unmodifiableMap(ingredientCategory);
    }

    /** Returns list of ingredients that are critically low (<=5) or out of stock. */
    public List<String> getLowStockWarnings() {
        List<String> warnings = new ArrayList<>();
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            if (e.getValue() <= 5) {
                warnings.add(e.getKey() + ": " + e.getValue() + (e.getValue() == 0 ? " [OUT]" : " [LOW]"));
            }
        }
        return warnings;
    }

    public void printStock() {
        for (Map.Entry<String, Integer> e : stock.entrySet()) {
            System.out.println("  " + e.getKey() + ": " + e.getValue()
                    + (e.getValue() == 0 ? " [OUT]" : ""));
        }
    }
}
