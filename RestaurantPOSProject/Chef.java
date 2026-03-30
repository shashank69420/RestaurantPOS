class Chef extends StaffMember {
    private final String specialty;
    public Chef(String name, int id, String specialty) { super(name, id); this.specialty = specialty; }
    public String getSpecialty() { return specialty; }
    @Override public String getRole() { return "Chef"; }
    public void prepareOrder(KitchenOrder ko) {
        ko.startPreparing();
        System.out.println("Chef " + getName() + " preparing Order #" + ko.getOrder().getOrderId());
    }
    public void finishOrder(KitchenOrder ko, Table t) {
        ko.markReady();
        t.markReadyForBill();
        System.out.println("Chef " + getName() + " done -> Table " + t.getTableNumber() + " ready for bill");
    }
}
