abstract class StaffMember {
    private final String name;
    private final int id;
    public StaffMember(String name, int id) { this.name = name; this.id = id; }
    public String getName() { return name; }
    public int getId()      { return id; }
    public abstract String getRole();
    @Override public String toString() { return getRole() + ": " + name + " (ID:" + id + ")"; }
}
