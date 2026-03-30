class Table {
    private final int tableNumber;
    private TableState state;
    private Order currentOrder;
    public Table(int n)               { this.tableNumber = n; this.state = TableState.AVAILABLE; }
    public int getTableNumber()       { return tableNumber; }
    public TableState getState()      { return state; }
    public Order getCurrentOrder()    { return currentOrder; }
    public void seatCustomers()       { if (state != TableState.AVAILABLE) throw new IllegalStateException("Table not available!"); state = TableState.OCCUPIED; currentOrder = new Order(tableNumber); }
    public void markWaitingForFood()  { state = TableState.WAITING_FOR_FOOD; }
    public void markReadyForBill()    { state = TableState.READY_FOR_BILL; }
    public void processPayment(KitchenOrder ko) {
        if (state != TableState.READY_FOR_BILL) throw new IllegalStateException("Table " + tableNumber + " not ready for bill!");
        ko.markPaid();
        System.out.println("Table " + tableNumber + " payment processed. Order #" + ko.getOrder().getOrderId() + " [PAID]");
        clearTable();
    }
    public void clearTable()          { state = TableState.AVAILABLE; currentOrder = null; }
    @Override public String toString() { return "Table " + tableNumber + " [" + state + "]"; }
}
