package service;

import dao.OrderDAO;
import dao.ProductDAO;
import entity.Customer;
import entity.Orders;
import entity.Product;

import java.util.List;
import java.util.Scanner;

public class OrderService {
    private Customer customer;
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    // Cho phép set customer từ bên ngoài
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Admin cập nhật trạng thái đơn hàng
    public void updateOrderStatus(int orderId, String newStatus) {
        List<String> validStatuses = List.of("pending", "approved", "shipping", "cancelled");
        if (!validStatuses.contains(newStatus.toLowerCase())) {
            System.out.println("Trang thai khong hop le!");
            return;
        }
        orderDAO.updateOrder(orderId, newStatus);
        System.out.println("Cap nhat trang thai don hang thanh cong!");
    }

    public void viewAllOrders() {
        List<Orders> orders = getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Khong co don hang nao!");
            return;
        }
        System.out.printf("%-5s %-12s %-15s %-20s\n",
                "ID", "Khach hang", "Trang thai", "Ngay tao");
        System.out.println("-------------------------------------------------------------");
        for (Orders o : orders) {
            System.out.printf("%-5d %-12d %-15s %-20s\n",
                    o.getId(),
                    o.getCustomer_id(),
                    o.getStatus(),
                    o.getCreatedAt());
        }
    }

    public void updateOrderStatus() {
        System.out.print("Nhap ID don hang can cap nhat: ");
        int orderId = validateInt();
        System.out.print("Nhap trang thai moi (pending/approved/shipping/cancelled): ");
        String status = sc.nextLine();
        updateOrderStatus(orderId, status);
    }
    // Admin xem toàn bộ đơn hàng
    public List<Orders> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    // Customer xem đơn hàng của mình
    public List<Orders> getOrdersByCustomer(int customerId) {
        return orderDAO.getOrdersByCustomer(customerId);
    }

    public int validateInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Nhap sai, vui long nhap so nguyen: ");
            }
        }
    }

}
