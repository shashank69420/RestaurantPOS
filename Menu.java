import java.util.*;

public class Menu {
    private final Map<String, MenuItem> items = new LinkedHashMap<>();

    public void addItem(MenuItem item) { 
        items.put(item.getName().toLowerCase(), item); 
    }

    public Collection<MenuItem> getItems() { 
        return Collections.unmodifiableCollection(items.values()); 
    }

    public MenuItem findItem(String name) {
        if (name == null) return null;
        return items.get(name.toLowerCase());
    }

    public void removeItem(String name) {
        if (name == null || !items.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("Item not found: " + name);
        }
        items.remove(name.toLowerCase());
    }

    public void updateItemPrice(String name, java.math.BigDecimal price) {
        MenuItem i = findItem(name);
        if (i == null) throw new IllegalArgumentException("Item not found: " + name);
        i.setPrice(price);
    }

    public void printMenu() {
        String cat = "";
        for (MenuItem i : items.values()) {
            if (!i.getCategory().equals(cat)) { 
                cat = i.getCategory(); 
                System.out.println("  [" + cat + "]"); 
            }
            System.out.println("    " + i);
        }
    }
}
