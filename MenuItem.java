import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * MenuItem — Represents a single dish on the restaurant menu.
 * Demonstrates: Encapsulation (private fields, controlled access via getters/setters).
 */
public class MenuItem {
    private final String name;
    private final String category;
    private BigDecimal price;
    private boolean available = true;
    private final boolean vegetarian;

    public MenuItem(String name, BigDecimal price, String category, boolean vegetarian) {
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.category = category;
        this.vegetarian = vegetarian;
    }

    public String getName()       { return name; }
    public BigDecimal getPrice()  { return price; }
    public String getCategory()   { return category; }
    public boolean isAvailable()  { return available; }
    public boolean isVegetarian() { return vegetarian; }

    public void setAvailable(boolean a) { this.available = a; }

    public void setPrice(BigDecimal p) {
        if (p.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Negative price!");
        this.price = p.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return (vegetarian ? "[V] " : "[N] ") + name + " ($" + price.toPlainString() + ")"
                + (available ? "" : " [SOLD OUT]");
    }
}
