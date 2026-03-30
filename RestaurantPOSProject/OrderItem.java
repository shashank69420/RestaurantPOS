class OrderItem {
    private final MenuItem menuItem;
    private int quantity;
    public OrderItem(MenuItem m, int qty) {
        if (!m.isAvailable()) throw new IllegalStateException(m.getName() + " is sold out!");
        this.menuItem = m; this.quantity = qty;
    }
    public MenuItem getMenuItem()  { return menuItem; }
    public int getQuantity()       { return quantity; }
    public void addQuantity(int n) { quantity += n; }
    public double getSubtotal()    { return menuItem.getPrice() * quantity; }
    @Override public String toString() { return quantity + "x " + menuItem.getName() + " = $" + String.format("%.2f", getSubtotal()); }
}
