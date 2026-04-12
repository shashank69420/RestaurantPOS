import java.util.*;

public class Kitchen {
    private final List<KitchenOrder> activeOrders = new ArrayList<>();
    private final List<KitchenOrder> orderHistory = new ArrayList<>();

    public void receiveOrder(KitchenOrder ko) {
        activeOrders.add(ko);
        System.out.println("[Kitchen] Received: " + ko);
    }

    public void archiveOrder(KitchenOrder ko) {
        activeOrders.remove(ko);
        orderHistory.add(ko);
    }

    public List<KitchenOrder> getActiveOrders() {
        return Collections.unmodifiableList(activeOrders);
    }

    public void printActiveOrders() {
        System.out.println("[Kitchen] Active Orders:");
        if (activeOrders.isEmpty()) {
            System.out.println("  None");
            return;
        }
        for (KitchenOrder ko : activeOrders) {
            System.out.println("  " + ko);
        }
    }

    public void printAllOrders() {
        System.out.println("[Kitchen] Active Orders:");
        for (KitchenOrder ko : activeOrders) System.out.println("  " + ko);
        System.out.println("[Kitchen] Completed Orders (History):");
        for (KitchenOrder ko : orderHistory) System.out.println("  " + ko);
    }
}
