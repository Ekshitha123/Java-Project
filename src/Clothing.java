public class Clothing extends Product {
    private String clothingFeature; 

    public Clothing(String id, String name, double price, String clothingFeature) {
        super(id, name, price);
        this.clothingFeature = clothingFeature;
    }

    // Getters and setters for clothingFeature

    @Override
    public String toString() {
        return super.toString() + ", ClothingFeature='" + clothingFeature + "'";
    }
}
