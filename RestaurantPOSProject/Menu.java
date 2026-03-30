import java.util.*;

class Menu {
    private final List<MenuItem> items = new ArrayList<>();
    public void addItem(MenuItem item) { items.add(item); }
    public List<MenuItem> getItems()   { return Collections.unmodifiableList(items); }
    public MenuItem findItem(String name) {
        for (MenuItem i : items) if (i.getName().equalsIgnoreCase(name)) return i;
        return null;
    }
    public void removeItem(String name) {
        MenuItem i = findItem(name);
        if (i == null) throw new IllegalArgumentException("Item not found: " + name);
        items.remove(i);
    }
    public void updateItemPrice(String name, double price) {
        MenuItem i = findItem(name);
        if (i == null) throw new IllegalArgumentException("Item not found: " + name);
        i.setPrice(price);
    }
    public void printMenu() {
        String cat = "";
        for (MenuItem i : items) {
            if (!i.getCategory().equals(cat)) { cat = i.getCategory(); System.out.println("  [" + cat + "]"); }
            System.out.println("    " + i);
        }
    }
}
