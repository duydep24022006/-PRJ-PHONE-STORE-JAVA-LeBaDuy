package model;

import java.time.LocalDateTime;
import java.util.List;

public class Cart {
    private int id;
    private int customerId;
    private LocalDateTime createdAt;
    private List<CartItem> items; // lấy từ bảng cart_item qua CartDAO

    public Cart() {}

    public Cart(int id, int customerId, LocalDateTime createdAt, List<CartItem> items) {
        this.id = id;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.items = items;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
