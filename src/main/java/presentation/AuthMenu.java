package presentation;

import dao.CustomerDAO;
import entity.Customer;

import java.util.PrimitiveIterator;
import java.util.Scanner;

public class AuthMenu {
    private CustomerDAO customerDAO=new CustomerDAO();
    private Scanner sc=new Scanner(System.in);

    public void showAuthMenu() {
        while (true) {
            System.out.println("=== HỆ THỐNG XÁC THỰC ===");
            System.out.println("1. Đăng ký");
            System.out.println("2. Đăng nhập");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 0 -> {
                    System.out.println("Thoát chương trình!");
                    return;
                }
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void register() {
        System.out.println("=== Register ===");
        System.out.print("Tên: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("SĐT: ");
        String phone = sc.nextLine();
        System.out.print("Địa chỉ: ");
        String address = sc.nextLine();
        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();
        System.out.print("Role (admin/customer): ");
        String role = sc.nextLine();
        Customer c=new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPhone(phone);
        c.setAddress(address);
        c.setPassword(password);
        c.setRole(role);
        customerDAO.register(c);
        System.out.println("Đăng kí thành công");
    }
    private void login() {
        System.out.println("=== Login ===");
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Mật khẩu: "); String password = sc.nextLine();
    }
}
