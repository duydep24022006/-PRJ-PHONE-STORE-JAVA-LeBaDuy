package entity;

import java.sql.Timestamp;

public class Orders {
    private int id;
    private int customer_id;
    private String status;
    private Timestamp createdAt;

    public Orders(int id, int customer_id, String status, Timestamp createdAt) {
        this.id = id;
        this.customer_id = customer_id;
        this.status = status;
        this.createdAt = createdAt;
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
