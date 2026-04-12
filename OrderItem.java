import java.math.BigDecimal;

public class OrderItem {
    private final MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem m, int qty) {
        if (m == null) throw new IllegalArgumentException("MenuItem cannot be null. Item might not exist.");
        if (!m.isAvailable()) throw new IllegalStateException(m.getName() + " is sold out!");
        this.menuItem = m; 
        this.quantity = qty;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public void addQuantity(int n) { quantity += n; }
    
    public BigDecimal getSubtotal() { 
        return menuItem.getPrice().multiply(BigDecimal.valueOf(quantity)); 
    }
    
    @Override 
    public String toString() { 
        return quantity + "x " + menuItem.getName() + " = $" + getSubtotal().toPlainString(); 
    }
}
