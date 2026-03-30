class MenuItem {
    private final String name;
    private final String category;
    private double price;
    private boolean available = true;
    public MenuItem(String name, double price, String category) { this.name = name; this.price = price; this.category = category; }
    public String getName()             { return name; }
    public double getPrice()            { return price; }
    public String getCategory()         { return category; }
    public boolean isAvailable()        { return available; }
    public void setAvailable(boolean a) { this.available = a; }
    void setPrice(double p) { if (p < 0) throw new IllegalArgumentException("Negative price!"); this.price = p; }
    @Override public String toString()  { return name + " ($" + String.format("%.2f", price) + ")" + (available ? "" : " [SOLD OUT]"); }
}
