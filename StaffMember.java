/**
 * StaffMember — Abstract base class for all restaurant staff.
 * Demonstrates: Abstraction, Encapsulation, Inheritance.
 * Subclasses: Admin, Chef, Waiter.
 */
public abstract class StaffMember {
    private final String name;
    private final int id;

    public StaffMember(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId()      { return id; }

    /** Each subclass defines its own role — Polymorphism. */
    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + ": " + name + " (ID:" + id + ")";
    }
}
