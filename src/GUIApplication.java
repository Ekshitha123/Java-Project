import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUIApplication extends JFrame {
    private List<User> users = new ArrayList<>();
    private User currentUser = null;
    private ProductCatalog catalog = new ProductCatalog();
    private ShoppingCart cart = ShoppingCart.getInstance();
    private boolean isRegistered = false; // Flag to track if the user is registered


    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    private JPanel mainPanel;
    private JTextArea productTextArea;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton checkoutButton;
    private JButton exitButton;

    public GUIApplication(ProductCatalog catalog) {
        this.catalog = catalog; // Assign the provided catalog

        setTitle("Online Shopping App");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeLoginPanel();
        initializeMainPanel();
        

        setContentPane(loginPanel); // Start with the login panel
        setVisible(true);
    }

    private void initializeLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        

        JLabel titleLabel = new JLabel("Online Shopping App");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        loginPanel.add(titleLabel);

        usernameField = new JTextField(20);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        loginPanel.add(usernameField);

        passwordField = new JPasswordField(20);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        loginPanel.add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        loginPanel.add(registerButton);

        // Login button is initialized but not added to the panel yet
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        revalidate();
        repaint();
    }


    private void initializeMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        productTextArea = new JTextArea();
        productTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddToCart();
            }
        });
        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewCart();
            }
        });
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckout();
            }
        });
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExit();
            }
        });

        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);
        buttonPanel.add(checkoutButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void showLoginPanel() {
        // Switch to login panel
        loginPanel.remove(registerButton);
        loginPanel.add(loginButton);
        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void showMainPanel() {
        setContentPane(mainPanel);
        revalidate();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Use the hashPassword method that should match the User class implementation
        String hashedPassword = hashPassword(password);

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getHashedPassword().equals(hashedPassword)) {
                currentUser = user;
                showMainPanel();
                updateProductTextArea();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
    private String hashPassword(String password) {
        
        return "hashed_" + password;
    }
    private void handleRegistration() {
        
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");

        // Simulate user creation and check if they already exist
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            // Check if user already exists
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "User already exists. Please login.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            User newUser = new User(username, password);
            users.add(newUser);
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.", "Registration", JOptionPane.INFORMATION_MESSAGE);
            isRegistered = true;
            showLoginPanel(); // Show the login panel after registration
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password. Registration failed.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddToCart() {
        JComboBox<Product> productComboBox = new JComboBox<>();
        JTextField quantityField = new JTextField(5);
        for (Product product : catalog.getProducts()) {
            productComboBox.addItem(product);
        }

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Select Product:"));
        panel.add(productComboBox);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    throw new NumberFormatException("Quantity must be greater than 0.");
                }
                cart.addProduct(selectedProduct, quantity);
                JOptionPane.showMessageDialog(this, "Product added to cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void handleViewCart() {
        // Add your logic for viewing the cart here
        // Example: Show a dialog to display the current cart contents
        List<Product> cartItems = cart.getItems();
        List<Integer> quantities = cart.getQuantities();

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "View Cart", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder cartContents = new StringBuilder();
            cartContents.append("Cart Contents:\n");
            for (int i = 0; i < cartItems.size(); i++) {
                Product product = cartItems.get(i);
                int quantity = quantities.get(i);
                cartContents.append(product.getName()).append(" - Quantity: ").append(quantity).append("\n");
            }
            JOptionPane.showMessageDialog(this, cartContents.toString(), "View Cart", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleCheckout() {
        // Add your logic for the checkout process here
        // Example: Show a dialog for payment processing and order confirmation
        List<Product> cartItems = cart.getItems();
        double totalCost = cart.calculateTotalCost();

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cannot process the order: Shopping Cart is empty.", "Checkout", JOptionPane.ERROR_MESSAGE);
        } else {
            StringBuilder checkoutMessage = new StringBuilder();
            checkoutMessage.append("Cart Contents:\n");
            for (int i = 0; i < cartItems.size(); i++) {
                Product product = cartItems.get(i);
                int quantity = cart.getQuantities().get(i);
                checkoutMessage.append(product.getName()).append(" - Quantity: ").append(quantity).append("\n");
            }
            checkoutMessage.append("Total Cost: $").append(totalCost).append("\n");
            checkoutMessage.append("Select Payment Method:\n1. Debit Card\n2. Credit Card\n3. PayPal");

            String[] paymentMethods = { "Debit Card", "Credit Card", "PayPal" };
            String selectedMethod = (String) JOptionPane.showInputDialog(this, checkoutMessage.toString(), "Checkout", JOptionPane.QUESTION_MESSAGE, null, paymentMethods, paymentMethods[0]);

            if (selectedMethod != null && !selectedMethod.isEmpty()) {
                boolean paymentSuccessful = simulatePayment(totalCost, selectedMethod);
                if (paymentSuccessful) {
                    cart.clearCart();
                    JOptionPane.showMessageDialog(this, "Payment successful. Order placed.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Checkout", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean simulatePayment(double totalCost, String paymentMethod) {
        // Simulate payment processing here
        // In a real application, this would involve actual payment gateway integration
        return Math.random() < 0.9; // 90% success rate
    }

    private void handleExit() {
        // Add any cleanup logic here if needed
        System.exit(0);
    }

    private void updateProductTextArea() {
        // Update the product list displayed in the JTextArea
        StringBuilder productList = new StringBuilder();
        productList.append("Available Products:\n");
        for (Product product : catalog.getProducts()) {
            productList.append(product.toString()).append("\n");
        }
        productTextArea.setText(productList.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductCatalog catalog = new ProductCatalog();
            ProductFactory factory = new ProductFactory();

            // Use the factory to create and add products to the catalog
            catalog.addProduct(factory.createProduct("Electronics", "P100", "Wireless Headsets", 499.99, "Noise Cancelling"));
            catalog.addProduct(factory.createProduct("Electronics", "P101", "Smartphone", 900.95, "5G Capable"));
            catalog.addProduct(factory.createProduct("Electronics", "P102", "HP Laptop", 740.93, "8GB RAM"));
            catalog.addProduct(factory.createProduct("Electronics", "P103", "Wireless Mouse", 200.50, "Bluetooth"));
            catalog.addProduct(factory.createProduct("Clothing", "P104", "T-Shirt", 29.99, "Size M"));
            catalog.addProduct(factory.createProduct("Clothing", "P105", "Jeans", 59.99, "Size 32"));
            GUIApplication app = new GUIApplication(catalog);
            app.setVisible(true);
        });
    }

}


