import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.info("Application started.");

        Scanner scanner = new Scanner(System.in);
        UserAuthenticationService authService = new UserAuthenticationService();
        ProductCatalog catalog = new ProductCatalog();
        ShoppingCart cart = ShoppingCart.getInstance();
        OrderProcessing orderProcessing = new OrderProcessing(new PaymentProcessing());
        CartBuilder cartBuilder = new CartBuilder();
        PaymentProcessing paymentProcessing = new PaymentProcessing();

        // Populate product catalog for demonstration
        ProductFactory factory = new ProductFactory();
        catalog.addProduct(factory.createProduct("Electronics", "P100", "Wireless Headsets", 499.99, "Noise Cancelling"));
        catalog.addProduct(factory.createProduct("Electronics", "P101", "Smartphone", 900.95, "5G Capable"));
        catalog.addProduct(factory.createProduct("Electronics", "P102", "HP Laptop", 740.93, "8GB RAM"));
        catalog.addProduct(factory.createProduct("Electronics", "P103", "Wireless Mouse", 200.50, "Bluetooth"));
        catalog.addProduct(factory.createProduct("Clothing", "P104", "T-Shirt", 29.99, "Size M"));
        catalog.addProduct(factory.createProduct("Clothing", "P105", "Jeans", 59.99, "Size 32"));
        logger.info("Product catalog populated.");
        

        boolean exit = false;
        boolean isLoggedIn = false; // Flag to track login status

        while (!exit) {
            System.out.println("\n1. Sign Up\n2. Sign In\n3. Explore Products\n4. Check Cart\n5. Confirm Order\n6. Quit");
            System.out.print("Enter your selection: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Registration logic
                    logger.info("User selected Registration.");
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    authService.registerUser(username, password);
                    break;

                case 2:
                    // Login logic
                    logger.info("User selected Login.");
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    isLoggedIn = authService.loginUser(loginUsername, loginPassword);
                    if (isLoggedIn) {
                        System.out.println("Login successful.");
                    } else {
                        System.out.println("Login failed. Exiting further steps.");
                    }
                    break;

                case 3:
                    // Check login status before proceeding
                    if (!isLoggedIn) {
                        System.out.println("Please log in first.");
                        break;
                    }
                    // Browse products and add to cart
                    catalog.listProducts();
                    System.out.print("Enter Product ID to add to cart (or '0' to go back): ");
                    String productChoice = scanner.nextLine();
                    if (!productChoice.equals("0")) {
                        Product chosenProduct = catalog.findProductById(productChoice);
                        if (chosenProduct != null) {
                            System.out.print("Enter Quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            cart.addProduct(chosenProduct, quantity);
                        } else {
                            System.out.println("Product not found.");
                        }
                    }
                    break;

                case 4:
                    // Check login status before proceeding
                    if (!isLoggedIn) {
                        System.out.println("Please log in first.");
                        break;
                    }
                    // View cart
                    cart.viewCart();
                    double totalCost = cart.calculateTotalCost();
                    System.out.println("Total amount to be paid: $" + totalCost);
                    break;

                case 5:
                    // Check login status before proceeding
                    if (!isLoggedIn) {
                        System.out.println("Please log in first.");
                        break;
                    }
                    // Place order
                    if (cart.isEmpty()) {
                        System.out.println("Cannot process order: Shopping Cart is empty.");
                        break;
                    }
                    cart.viewCart();
                    double cost = cart.calculateTotalCost();
                    System.out.println("Total amount to be paid: $" + cost);
                    System.out.println("Select payment method:");
                    System.out.println("1. Debit Card\n2. Credit Card\n3. PayPal");
                    System.out.print("Please select an option: ");
                    int paymentChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    String paymentMethod = "";
                    switch (paymentChoice) {
                        case 1:
                            paymentMethod = "Debit Card";
                            break;
                        case 2:
                            paymentMethod = "Credit Card";
                            break;
                        case 3:
                            paymentMethod = "PayPal";
                            break;
                        default:
                            System.out.println("Invalid payment method choice. Payment failed. Please try again.");
                            break;
                    }

                    if (!paymentMethod.isEmpty()) {
                        System.out.println("Selected payment method: " + paymentMethod);
                        if (paymentChoice >= 1 && paymentChoice <= 3) {
                            boolean isPaymentSuccessful = paymentProcessing.processPayment(cost);
                            if (isPaymentSuccessful) {
                                System.out.println("Payment done successfully.");
                                cart.clearCart();
                                System.out.println("Your cart is empty now.");
                                exit = true; // Exit the application after successful payment
                            } else {
                                System.out.println("Payment failed. Please try again.");
                            }
                        }
                    }
                    break;

                case 6:
                    exit = true;
                    logger.info("User chose to exit. Application terminating.");
                    break;

                default:
                    System.out.println("Invalid choice.");
                    logger.warn("User made an invalid choice.");
                    break;
            }
        }

        scanner.close();
        logger.info("Application ended.");
    }
}
