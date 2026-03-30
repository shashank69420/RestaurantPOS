import java.util.*;

public class RestaurantPOS {
    public static void main(String[] args) {

        // --- Setup ---
        Menu menu = new Menu();
        menu.addItem(new MenuItem("Burger",  8.99, "Mains"));
        menu.addItem(new MenuItem("Steak",  14.99, "Mains"));
        menu.addItem(new MenuItem("Fries",   3.49, "Sides"));
        menu.addItem(new MenuItem("Soda",    1.99, "Drinks"));

        Inventory inv = new Inventory();
        inv.addStock("Buns", 10);
        inv.addStock("Steak Cuts", 10);
        inv.addStock("Potato Bags", 10);
        inv.linkItem("Burger", "Buns");
        inv.linkItem("Steak",  "Steak Cuts");
        inv.linkItem("Fries",  "Potato Bags");

        Kitchen kitchen = new Kitchen();

        StaffMember waiter = new Waiter("Alice", 101);
        StaffMember chef   = new Chef("Bob",     201, "Grills");
        Admin       admin  = new Admin("Manager Roy", 301);

        // --- Admin Panel ---
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add menu item");
            System.out.println("2. Remove menu item");
            System.out.println("3. Change item price");
            System.out.println("4. Update stock (set absolute qty)");
            System.out.println("5. Restock item (add qty + re-enable)");
            System.out.println("6. View inventory");
            System.out.println("0. Exit admin panel");
            System.out.print("Choose: ");
            choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.print("Item name: ");   String addName  = sc.nextLine();
                    System.out.print("Price: ");       double addPrice = sc.nextDouble(); sc.nextLine();
                    System.out.print("Category: ");    String addCat   = sc.nextLine();
                    System.out.print("Ingredient: ");  String addIng   = sc.nextLine();
                    System.out.print("Stock qty: ");   int addQty      = sc.nextInt(); sc.nextLine();
                    admin.addMenuItem(menu, addName, addPrice, addCat);
                    inv.addStock(addIng, addQty);
                    inv.linkItem(addName, addIng);
                }
                case 2 -> { System.out.print("Item name to remove: "); String remName = sc.nextLine(); admin.removeMenuItem(menu, remName, inv); }
                case 3 -> { System.out.print("Item name: "); String chgName = sc.nextLine(); System.out.print("New price: "); double chgPrice = sc.nextDouble(); sc.nextLine(); admin.changePrice(menu, chgName, chgPrice); }
                case 4 -> { System.out.print("Ingredient name: "); String updIng = sc.nextLine(); System.out.print("New qty: "); int updQty = sc.nextInt(); sc.nextLine(); admin.updateStock(inv, updIng, updQty); }
                case 5 -> { System.out.print("Ingredient name: "); String rsIng = sc.nextLine(); System.out.print("Item name: "); String rsItem = sc.nextLine(); System.out.print("Qty to add: "); int rsQty = sc.nextInt(); sc.nextLine(); admin.restock(inv, menu, rsIng, rsItem, rsQty); }
                case 6  -> admin.viewInventory(inv);
                case 0  -> System.out.println("[Admin] Exiting admin panel.");
                default -> System.out.println("Invalid choice.");
            }
        }
        sc.close();

        // --- End-to-end demo ---
        System.out.println("\n=== END-TO-END DEMO ===");
        Table t1 = new Table(1);
        t1.seatCustomers();
        System.out.println("Seated: " + t1);

        Waiter w = (Waiter) waiter;
        w.takeOrder(t1, menu.findItem("Burger"), 2);
        w.takeOrder(t1, menu.findItem("Fries"),  1);
        Order o1 = t1.getCurrentOrder();
        o1.submit();
        t1.markWaitingForFood();

        KitchenOrder ko = new KitchenOrder(o1);
        kitchen.receiveOrder(ko);

        Chef c = (Chef) chef;
        c.prepareOrder(ko);
        kitchen.printActiveOrders();
        c.finishOrder(ko, t1);

        for (OrderItem oi : o1.getItems()) inv.onItemSold(oi.getMenuItem().getName(), oi.getQuantity(), menu);

        o1.printBill(2, 8.0, 15.0);
        t1.processPayment(ko);
        System.out.println("Table after payment: " + t1);
        kitchen.printActiveOrders();

        System.out.println("\n[Updated Inventory]");
        inv.printStock();
        System.out.println("\n[Updated Menu]");
        menu.printMenu();

        // --- Billing scenarios ---
        System.out.println("\n=== BILLING SCENARIOS ===");
        Order testOrder = new Order(99);
        testOrder.addItem(new OrderItem(menu.findItem("Steak"), 2));
        testOrder.addItem(new OrderItem(menu.findItem("Fries"), 2));
        testOrder.addItem(new OrderItem(menu.findItem("Soda"),  2));
        System.out.println("-- 1 person, no tax, no tip --");
        testOrder.printBill();
        System.out.println("-- 2 people splitting, no tax, no tip --");
        testOrder.printBill(2);
        System.out.println("-- 2 people splitting, 8% tax, no tip --");
        testOrder.printBill(2, 8.0);
        System.out.println("-- 2 people splitting, 8% tax, 15% tip --");
        testOrder.printBill(2, 8.0, 15.0);
        System.out.println("-- 4 people splitting, 8% tax, 18% tip --");
        testOrder.printBill(4, 8.0, 18.0);
    }
}
