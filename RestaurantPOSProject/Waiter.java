class Waiter extends StaffMember {
    public Waiter(String name, int id) { super(name, id); }
    @Override public String getRole() { return "Waiter"; }
    public void takeOrder(Table t, MenuItem item, int qty) {
        if (t.getState() != TableState.OCCUPIED) { System.out.println("Table not occupied!"); return; }
        t.getCurrentOrder().addItem(new OrderItem(item, qty));
        System.out.println(getName() + " added " + qty + "x " + item.getName());
    }
}
