import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class PaymentProcessingTest {

    // creating a subclass of PaymentProcessing to override the random behavior for consistent testing
    class TestablePaymentProcessing extends PaymentProcessing {
        private final boolean forceSuccess;

        public TestablePaymentProcessing(boolean forceSuccess) {
            this.forceSuccess = forceSuccess;
        }

        @Override
        protected boolean simulatePaymentGateway(double amount) {
            return forceSuccess;
        }
    }

    @Test
    public void testProcessPaymentSuccess() {
        // Arrange
        PaymentProcessing paymentProcessing = new TestablePaymentProcessing(true);
        
        // Act
        boolean result = paymentProcessing.processPayment(100.0);
        
        // Assert
        assertTrue(result, "Payment should be processed successfully.");
    }

    @Test
    public void testProcessPaymentFailure() {
        // Arrange
        PaymentProcessing paymentProcessing = new TestablePaymentProcessing(false);
        
        // Act
        boolean result = paymentProcessing.processPayment(100.0);
        
        // Assert
        assertFalse(result, "Payment should fail.");
    }

    @Test
    public void testProcessPaymentInvalidAmount() {
        // Arrange
        PaymentProcessing paymentProcessing = new PaymentProcessing();
        
        // Act
        boolean result = paymentProcessing.processPayment(-100.0);
        
        // Assert
        assertFalse(result, "Negative payment amount should be invalid.");
    }
}
