package presentation;

import service.AuthService;
import java.util.Scanner;

public class AuthMenu {
    private Scanner sc = new Scanner(System.in);
    private AuthService authService = new AuthService();
    public void showAuthMenu() {
        while (true) {
            System.out.println("=== HE THONG XAC THUC ===");
            System.out.println("1. Dang ky");
            System.out.println("2. Dang nhap");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = authService.validateInt();

            switch (choice) {
                case 1 -> authService.register();
                case 2 -> authService.login();
                case 0 -> {
                    System.out.println("Thoat chuong trinh!");
                    return;
                }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }



}
