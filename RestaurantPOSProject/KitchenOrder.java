class KitchenOrder {
    private final Order order;
    private OrderState status = OrderState.SENT_TO_KITCHEN;
    public KitchenOrder(Order order)   { this.order = order; }
    public Order getOrder()            { return order; }
    public OrderState getStatus()      { return status; }
    public void startPreparing()       { status = OrderState.PREPARING; }
    public void markReady()            { status = OrderState.READY; }
    public void markPaid()             { status = OrderState.PAID; }
    public boolean isActive()          { return status != OrderState.PAID; }
    @Override public String toString() { return "KitchenOrder #" + order.getOrderId() + " (Table " + order.getTableNumber() + ") [" + status + "]"; }
}
