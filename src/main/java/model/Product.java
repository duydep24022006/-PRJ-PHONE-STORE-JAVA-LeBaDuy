package model;

import java.time.LocalDateTime;

public class Product {
    private int id;
    private String name;
    private String brand;
    private String capacity;
    private String color;
    private double price;
    private int stock;
    private String description;
    private int categoryId;
    private Double flashSalePrice;
    private Integer flashSaleQuantity;
    private LocalDateTime flashSaleExpiry;

    public Double getFlashSalePrice() {
        return flashSalePrice;
    }

    public void setFlashSalePrice(Double flashSalePrice) {
        this.flashSalePrice = flashSalePrice;
    }

    public Integer getFlashSaleQuantity() {
        return flashSaleQuantity;
    }

    public void setFlashSaleQuantity(Integer flashSaleQuantity) {
        this.flashSaleQuantity = flashSaleQuantity;
    }

    public LocalDateTime getFlashSaleExpiry() {
        return flashSaleExpiry;
    }

    public void setFlashSaleExpiry(LocalDateTime flashSaleExpiry) {
        this.flashSaleExpiry = flashSaleExpiry;
    }

    public Product(int id, String name, String brand, String capacity, String color, double price, int stock, String description, int categoryId, Double flashSalePrice, Integer flashSaleQuantity, LocalDateTime flashSaleExpiry) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.capacity = capacity;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.categoryId = categoryId;
        this.flashSalePrice = flashSalePrice;
        this.flashSaleQuantity = flashSaleQuantity;
        this.flashSaleExpiry = flashSaleExpiry;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Product() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }



    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", capacity='" + capacity + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", flashSalePrice=" + flashSalePrice +
                ", flashSaleQuantity=" + flashSaleQuantity +
                ", flashSaleExpiry=" + flashSaleExpiry +
                '}';
    }
}
