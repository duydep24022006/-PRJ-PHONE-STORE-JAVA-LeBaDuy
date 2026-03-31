package presentation;


import model.Customer;
import service.ReportService;
import util.Validator;

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
            System.out.println("4. Quan ly ma giam gia");
            System.out.println("5. Quan ly flash sale");
            System.out.println("6. danh sanh 5 san pham ban chay nhat thang");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = validateInt();
            switch (choice) {
                case 1 -> new CategoryMenu().showMenu();
                case 2 -> new ProductMenu().showMenu();
                case 3 -> new OrderMenu().showMenu();
                case 4 -> new CouponMenu().showMenu();
                case 5 -> new FlashSaleMenu().showMenu();
                case 6 -> new ReportService().reportTop5();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }
    private int validateInt() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                String input = sc.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Nhap sai, vui long nhap so nguyen: ");
            }
        }
    }
}
