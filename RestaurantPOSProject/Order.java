import java.util.*;

class Order {
    private static int nextId = 1;
    private final int orderId;
    private final int tableNumber;
    private final List<OrderItem> items = new ArrayList<>();
    private boolean submitted = false;
    public Order(int tableNumber) { this.orderId = nextId++; this.tableNumber = tableNumber; }
    public int getOrderId()       { return orderId; }
    public int getTableNumber()   { return tableNumber; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public void addItem(OrderItem newItem) {
        if (submitted) throw new IllegalStateException("Order already submitted!");
        for (OrderItem oi : items) {
            if (oi.getMenuItem().getName().equals(newItem.getMenuItem().getName())) { oi.addQuantity(newItem.getQuantity()); return; }
        }
        items.add(newItem);
    }
    public double getTotal() { double t = 0; for (OrderItem oi : items) t += oi.getSubtotal(); return t; }
    public void submit() { if (items.isEmpty()) throw new IllegalStateException("Empty order!"); submitted = true; }
    public void printBill(int split, double taxPercent, double tipPercent) {
        double subtotal  = getTotal();
        double tax       = subtotal * taxPercent / 100.0;
        double tip       = subtotal * tipPercent / 100.0;
        double grand     = subtotal + tax + tip;
        double perPerson = Math.ceil(grand * 100.0 / split) / 100.0;
        System.out.println("  ---- Bill: Order #" + orderId + " ----");
        for (OrderItem oi : items) System.out.println("    " + oi);
        System.out.println("  Subtotal : $" + String.format("%.2f", subtotal));
        System.out.println("  Tax  (" + String.format("%.1f", taxPercent) + "%): $" + String.format("%.2f", tax));
        System.out.println("  Tip  (" + String.format("%.1f", tipPercent) + "%): $" + String.format("%.2f", tip));
        System.out.println("  Grand Total : $" + String.format("%.2f", grand));
        if (split > 1) System.out.println("  Split (" + split + " people): $" + String.format("%.2f", perPerson) + " each");
        System.out.println("  --------------------------------");
    }
    public void printBill(int split, double taxPercent) { printBill(split, taxPercent, 0); }
    public void printBill(int split)                    { printBill(split, 0, 0); }
    public void printBill()                             { printBill(1, 0, 0); }
}
