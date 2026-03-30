import java.util.*;

class Kitchen {
    private final List<KitchenOrder> orders = new ArrayList<>();
    public void receiveOrder(KitchenOrder ko) {
        orders.add(ko);
        System.out.println("[Kitchen] Received: " + ko);
    }
    public void printActiveOrders() {
        System.out.println("[Kitchen] Active Orders:");
        boolean any = false;
        for (KitchenOrder ko : orders) {
            if (ko.isActive()) { System.out.println("  " + ko); any = true; }
        }
        if (!any) System.out.println("  None");
    }
    public void printAllOrders() {
        System.out.println("[Kitchen] All Orders:");
        for (KitchenOrder ko : orders) System.out.println("  " + ko);
    }
}
