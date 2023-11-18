import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Product product = new Product("P001", "Test Product", 10.0);
        assertNotNull(product, "Product should not be null");
        assertEquals("P001", product.getId(), "Product ID should match the expected value");
        assertEquals("Test Product", product.getName(), "Product name should match the expected value");
        assertEquals(10.0, product.getPrice(), 0.001, "Product price should match the expected value");
    }

    @Test
    public void testProductToString() {
        Product product = new Product("P001", "Test Product", 10.0);
        String expectedString = "Product{id='P001', name='Test Product', price=10.0}";
        assertEquals(expectedString, product.toString(), "Product toString should match the expected value");
    }
}
