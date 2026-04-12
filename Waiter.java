/**
 * Waiter — Concrete StaffMember who takes orders from tables.
 * Demonstrates: Inheritance, Polymorphism (getRole), Encapsulation.
 */
public class Waiter extends StaffMember {
    public Waiter(String name, int id) { super(name, id); }

    @Override
    public String getRole() { return "Waiter"; }

    public void takeOrder(Table t, MenuItem item, int qty) {
        if (t.getState() != TableState.OCCUPIED) {
            System.out.println("Table not occupied!");
            return;
        }
        if (item == null) {
            System.out.println("Cannot add order: MenuItem does not exist.");
            return;
        }
        try {
            t.getCurrentOrder().addItem(new OrderItem(item, qty));
            System.out.println(getName() + " added " + qty + "x " + item.getName());
        } catch (IllegalStateException e) {
            System.out.println("Error taking order: " + e.getMessage());
        }
    }
}
