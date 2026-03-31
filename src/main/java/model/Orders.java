package model;

import java.sql.Timestamp;

public class Orders {
    private int id;
    private int customer_id;
    private String status;
    private Timestamp createdAt;
    private String customerName;
    private String customerEmail;
    private String productName;
    private int quantity;
    private double price;
    private String couponCode; // mã giảm giá, có thể null

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    private double finalTotal; // tổng sau giảm
    private int discountPercent;

    public Orders(int id, int customer_id, String status, Timestamp createdAt, String customerName, String customerEmail, String productName, int quantity, double price, String couponCode, double finalTotal, int discountPercent) {
        this.id = id;
        this.customer_id = customer_id;
        this.status = status;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.couponCode = couponCode;
        this.finalTotal = finalTotal;
        this.discountPercent = discountPercent;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }

    public String getProductName() {

        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Orders() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
