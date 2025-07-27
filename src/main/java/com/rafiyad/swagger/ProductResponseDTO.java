package com.rafiyad.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "Product response object without ID")
public class ProductResponseDTO {
    
    @Schema(description = "Name of the product.", example = "Laptop", required = true)
    private String name;
    
    @Schema(description = "Price of the product.", example = "1200.00", required = true)
    private double price;
    
    // Default constructor
    public ProductResponseDTO() {
    }
    
    // All-args constructor
    public ProductResponseDTO(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    // Constructor from Product
    public ProductResponseDTO(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
    }
    
    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    // equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponseDTO that = (ProductResponseDTO) o;
        return Double.compare(that.price, price) == 0 &&
               Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
    
    @Override
    public String toString() {
        return "ProductResponseDTO{" +
               "name='" + name + '\'' +
               ", price=" + price +
               '}';
    }
}