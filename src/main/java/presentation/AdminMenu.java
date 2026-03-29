package presentation;


import entity.Customer;
import service.OrderService;

import java.util.Scanner;

public class AdminMenu {
    private Scanner sc = new Scanner(System.in);
    private Customer customer;
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void showMenu() {
        while (true) {
            System.out.println("=== ADMIN MENU ===");
            System.out.println("1. Quan ly danh muc");
            System.out.println("2. Quan ly san pham");
            System.out.println("3. Quan ly don hang");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> new CategoryMenu().showMenu();
                case 2 -> new ProductMenu().showMenu();
                case 3 -> new OrderMenu().showMenu();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }
}
