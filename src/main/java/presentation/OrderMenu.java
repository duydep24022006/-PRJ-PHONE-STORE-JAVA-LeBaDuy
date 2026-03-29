package presentation;

import service.CategoryService;
import service.OrderService;
import entity.Orders;

import java.util.List;
import java.util.Scanner;

public class OrderMenu {
    private Scanner sc = new Scanner(System.in);
    private OrderService orderService = new OrderService();
    public void showMenu() {
        int choice;
        do {
            System.out.println("=== QUAN LY DON HANG ===");
            System.out.println("1. Xem danh sach don hang");
            System.out.println("2. Cap nhat trang thai don hang");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = orderService.validateInt();

            switch (choice) {
                case 1 -> orderService.viewAllOrders();
                case 2 -> orderService.updateOrderStatus();
                case 0 -> System.out.println("Quay lai menu Admin.");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }


}
