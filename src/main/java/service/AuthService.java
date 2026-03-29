package service;

import dao.CustomerDAO;
import entity.Customer;
import presentation.AdminMenu;
import presentation.CustomerMenu;
import util.Validator;

import java.util.Scanner;

public class AuthService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private Scanner sc = new Scanner(System.in);
    public void register() {
        System.out.println("=== Register ===");
        String email;
        while (true) {
            System.out.print("Email: ");
            email = sc.nextLine();
            if (!Validator.isValidEmail(email)) {
                System.out.println("Email khong hop le, vui long nhap lai!");
                continue;
            }
            final String checkEmail = email;
            boolean exists = customerDAO.getAllCustomer().stream()
                    .anyMatch(c -> c.getEmail().equalsIgnoreCase(checkEmail));
            if (exists) {
                System.out.println("Email da ton tai, vui long nhap lai!");
                continue;
            }
            break;
        }

        System.out.print("Ten: ");
        String name = sc.nextLine();

        String phone;
        while (true) {
            System.out.print("SDT: ");
            phone = sc.nextLine();
            if (!Validator.isValidPhone(phone)) {
                System.out.println("So dien thoai khong hop le, vui long nhap lai!");
                continue;
            }
            break;
        }

        System.out.print("Dia chi: ");
        String address = sc.nextLine();

        String password;
        while (true) {
            System.out.print("Mat khau: ");
            password = sc.nextLine();
            if (!Validator.isValidPassword(password)) {
                System.out.println("Mat khau phai tu 8 ky tu tro len, co ca chu va so!");
                continue;
            }
            break;
        }

        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPhone(phone);
        c.setAddress(address);
        c.setPassword(password);
        c.setRole("customer");

        customerDAO.register(c);
        System.out.println("Dang ky thanh cong");
    }

    public void login() {
        System.out.println("=== Login ===");
        while (true) {
            System.out.print("Email: ");
            String email = sc.nextLine();
            if (!Validator.isValidEmail(email)) {
                System.out.println("Email khong hop le, vui long nhap lai!");
                continue;
            }

            System.out.print("Mat khau: ");
            String password = sc.nextLine();

            Customer c = customerDAO.login(email, password);
            if (c != null) {
                System.out.println("Dang nhap thanh cong! Xin chao " + c.getName());
                if (c.getRole().equalsIgnoreCase("admin")) {
                    AdminMenu am = new AdminMenu();
                    am.setCustomer(c);
                    am.showMenu();
                } else {
                    CustomerMenu cm = new CustomerMenu();
                    cm.setCustomer(c);
                    cm.showMenu();
                }
            } else {
                System.out.println("Sai email hoac mat khau, vui long nhap lai!");
            }
            break;
        }
    }
}
