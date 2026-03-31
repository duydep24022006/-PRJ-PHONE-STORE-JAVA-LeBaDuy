package presentation;

import service.FlashSaleSevice;
import service.ProductService;

import java.util.Scanner;

public class FlashSaleMenu {
    private Scanner sc = new Scanner(System.in);
    private FlashSaleSevice  flashSaleSevice = new FlashSaleSevice();

    public void showMenu() {
        while (true) {
            System.out.println("=== QUAN LY FLASH SALE ===");
            System.out.println("1. Tao flash sale cho san pham");
            System.out.println("2. Xoa flash sale");
            System.out.println("3. Xem danh sach san pham co flash sale");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> flashSaleSevice.createFlashSale();
                case 2 -> flashSaleSevice.removeFlashSale();
                case 3 -> flashSaleSevice.viewFlashSaleProducts();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }
}
