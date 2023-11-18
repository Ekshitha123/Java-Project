import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String username;
    private String hashedPassword;
    private Set<String> roles;

    public User(String username, String password) {
        setUsername(username);
        setHashedPassword(password); // Hash the password upon user creation
        roles = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{5,}$");
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid username format. Username should contain at least 5 letters and consist of only alphabets (a-z, A-Z) and numbers (0-9).");
        }
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid password format. Password must contain 8 or more characters with at least one number, one uppercase and lowercase letter, and one special character.");
        }
        this.hashedPassword = hashPassword(password); // Simulating password hashing
    }

    private String hashPassword(String password) {
        // Placeholder for password hashing logic
        return "hashed_" + password; // This should be replaced with actual hashing logic
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public Set<String> getRoles() {
        return new HashSet<>(roles); // Returning a copy to preserve encapsulation
    }
}
