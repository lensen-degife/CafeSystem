package allCafeInfo;

import java.util.HashMap;
import java.util.Map;

public class Order {

    // key = item ID, value = quantity
    private Map<Integer, Integer> items;

    public Order() {
        items = new HashMap<>();
    }

    // Add item to order
    public void addItem(int itemId, int quantity) {
        items.put(itemId, items.getOrDefault(itemId, 0) + quantity);
    }

    // Get all items
    public Map<Integer, Integer> getItems() {
        return items;
    }

    // Calculate total using CafeMenu
    public double calculateTotal(CafeMenu cafeMenu) {
        double total = 0.0;
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            MenuItem item = cafeMenu.getItemById(entry.getKey());
            if (item != null) {
                total += item.price * entry.getValue();
            }
        }
        return total;
    }

    // Print order summary
    public void printSummary(CafeMenu cafeMenu) {
        System.out.println("\nYour Order Summary:");
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            MenuItem item = cafeMenu.getItemById(entry.getKey());
            if (item != null) {
                double subtotal = item.price * entry.getValue();
                System.out.printf("%-15s x%d ------ %.2f birr%n",
                        item.name, entry.getValue(), subtotal);
            }
        }
        System.out.printf("Total Amount: %.2f birr%n", calculateTotal(cafeMenu));
    }
}