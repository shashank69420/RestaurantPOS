import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Order {
    private static int nextId = 1;
    private final int orderId;
    private final int tableNumber;
    private final Map<String, OrderItem> items = new LinkedHashMap<>();
    private boolean submitted = false;

    public Order(int tableNumber) { 
        this.orderId = nextId++; 
        this.tableNumber = tableNumber; 
    }
    
    public int getOrderId() { return orderId; }
    public int getTableNumber() { return tableNumber; }
    
    public Collection<OrderItem> getItems() { 
        return Collections.unmodifiableCollection(items.values()); 
    }

    public void addItem(OrderItem newItem) {
        if (submitted) throw new IllegalStateException("Order already submitted!");
        String itemName = newItem.getMenuItem().getName().toLowerCase();
        
        if (items.containsKey(itemName)) {
            items.get(itemName).addQuantity(newItem.getQuantity());
        } else {
            items.put(itemName, newItem);
        }
    }

    public BigDecimal getTotal() { 
        BigDecimal total = BigDecimal.ZERO; 
        for (OrderItem oi : items.values()) {
            total = total.add(oi.getSubtotal()); 
        }
        return total; 
    }

    public void submit() { 
        if (items.isEmpty()) throw new IllegalStateException("Empty order!"); 
        submitted = true; 
    }

    public void printBill(int split, BigDecimal taxPercent, BigDecimal tipPercent) {
        BigDecimal subtotal = getTotal();
        BigDecimal hundred = new BigDecimal("100");
        
        BigDecimal tax = subtotal.multiply(taxPercent).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal tip = subtotal.multiply(tipPercent).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal grand = subtotal.add(tax).add(tip);
        
        BigDecimal perPerson = grand.divide(new BigDecimal(split), 2, RoundingMode.CEILING);

        System.out.println("  ---- Bill: Order #" + orderId + " ----");
        for (OrderItem oi : items.values()) {
            System.out.println("    " + oi);
        }
        System.out.println("  Subtotal: $" + subtotal.toPlainString());
        System.out.println("  Tax (" + taxPercent.toPlainString() + "%): $" + tax.toPlainString());
        System.out.println("  Tip (" + tipPercent.toPlainString() + "%): $" + tip.toPlainString());
        System.out.println("  Grand Total: $" + grand.toPlainString());
        if (split > 1) {
            System.out.println("  Split (" + split + " people): $" + perPerson.toPlainString() + " each");
        }
        System.out.println("  --------------------------------");
    }
    
    public void printBill(int split, BigDecimal taxPercent) { 
        printBill(split, taxPercent, BigDecimal.ZERO); 
    }
    
    public void printBill(int split) { 
        printBill(split, BigDecimal.ZERO, BigDecimal.ZERO); 
    }
    
    public void printBill() { 
        printBill(1, BigDecimal.ZERO, BigDecimal.ZERO); 
    }
}
