import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private final Map<Product, Integer> cartItems;

    private ShoppingCart() {
        cartItems = new HashMap<>();
    }

    private static class ShoppingCartHolder {
        private static final ShoppingCart INSTANCE = new ShoppingCart();
    }

    public static ShoppingCart getInstance() {
        return ShoppingCartHolder.INSTANCE;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }
        cartItems.put(product, cartItems.getOrDefault(product, 0) + quantity);
    }

    public void removeProduct(Product product) {
        if (product != null) {
            cartItems.remove(product);
        }
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public List<Product> getItems() {
        return new ArrayList<>(cartItems.keySet());
    }

    public List<Integer> getQuantities() {
        return new ArrayList<>(cartItems.values());
    }

    public Map<Product, Integer> getItemsWithQuantities() {
        return new HashMap<>(cartItems);
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalCost += product.getPrice() * quantity;
        }
        return totalCost;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void viewCart() {
        if (cartItems.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("Shopping Cart Contents:");
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - Quantity: " + quantity);
        }
    }

    public void updateProductQuantity(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return;
        }
        cartItems.put(product, quantity);
    }
}
