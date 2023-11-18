import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserAuthenticationServiceTest {
    
    private UserAuthenticationService authService;

    @BeforeEach
    void setUp() {
        authService = new UserAuthenticationService();
    }

    @Test
    void registerUser_ShouldSucceed_WithValidCredentials() {
        // Arrange
        String username = "newUser";
        String password = "Password@123";

        // Act
        authService.registerUser(username, password);

        // Assert
        assertTrue(authService.loginUser(username, password));
    }

    @Test
    void loginUser_ShouldFail_WithIncorrectPassword() {
        // Arrange
        String username = "existingUser";
        String password = "CorrectPassword@123";
        String wrongPassword = "WrongPassword@123";
        authService.registerUser(username, password);

        // Act
        boolean loginResult = authService.loginUser(username, wrongPassword);

        // Assert
        assertFalse(loginResult);
    }

    @Test
    void loginUser_ShouldSucceed_WithCorrectPassword() {
        // Arrange
        String username = "existingUser";
        String password = "CorrectPassword@123";
        authService.registerUser(username, password);

        // Act
        boolean loginResult = authService.loginUser(username, password);

        // Assert
        assertTrue(loginResult);
    }
    
    
}
