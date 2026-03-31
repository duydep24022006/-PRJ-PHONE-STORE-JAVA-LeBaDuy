package service;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Customer;
import model.Orders;
import util.Validator;

import java.util.List;
import java.util.Scanner;

public class OrderService {
    private Customer customer;
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public void viewAllOrders() {
        List<Orders> orders = getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Khong co don hang nao!");
            return;
        }
        System.out.printf("%-5s %-20s %-25s %-20s %-10s %-15s %-15s %-12s %-20s\n",
                "ID", "Khach hang", "Email", "San pham", "So luong", "Gia (VND)", "Thanh tien (VND)", "Trang thai", "Ngay tao");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Orders o : orders) {
            double lineTotal = o.getQuantity() * o.getPrice();
            System.out.printf("%-5d %-20s %-25s %-20s %-10d %-15s %-15s %-12s %-20s\n",
                    o.getId(),
                    o.getCustomerName(),
                    o.getCustomerEmail(),
                    o.getProductName(),
                    o.getQuantity(),
                    Validator.formatMoney(o.getPrice()),
                    Validator.formatMoney(lineTotal),
                    o.getStatus(),
                    o.getCreatedAt());
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void updateOrderStatus() {

        System.out.print("Nhap ID don hang can cap nhat: ");
        int orderId = validateInt();

        Orders order = orderDAO.getOrderById(orderId);
        if (order == null) {
            System.out.println("Khong tim thay don hang!");
            return;
        }

        String currentStatus = order.getStatus().toLowerCase();
        System.out.println("Trang thai hien tai: " + currentStatus);
        System.out.print("Nhap trang thai moi (shipping/delivered/cancelled): ");
        String newStatus = sc.nextLine().trim().toLowerCase();

        boolean validTransition = false;
        switch (currentStatus) {
            case "pending":
                if (newStatus.equals("shipping")) {
                    validTransition = true;
                } else if (newStatus.equals("cancelled")) {
                    validTransition = true;
                    orderDAO.cancelOrder(orderId);
                    return;
                }
                break;
            case "shipping":
                validTransition = newStatus.equals("delivered");
                break;
            case "delivered":
            case "cancelled":
                validTransition = false;
                break;
        }

        if (!validTransition) {
            System.out.println("Chuyen trang thai khong hop le!");
            return;
        }

        orderDAO.updateOrder(orderId, newStatus);
        System.out.println("Cap nhat trang thai thanh cong!");
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
