package presentation;

import service.ProductService;
import java.util.Scanner;
public class ProductMenu {
    private Scanner sc = new Scanner(System.in);
    private ProductService productService = new ProductService();
    public void showMenu() {
        while (true) {
            System.out.println("=== QUAN LY SAN PHAM ===");
            System.out.println("1. Them moi san pham");
            System.out.println("2. Sua thong tin san pham");
            System.out.println("3. Xoa san pham");
            System.out.println("4. Hien thi danh sach san pham");
            System.out.println("5. Tim kiem san pham theo ten");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = productService.validateInt();
            switch (choice) {
                case 1 -> productService.addProduct();
                case 2 -> productService.updateProduct();
                case 3 -> productService.deleteProduct();
                case 4 -> productService.showProducts();
                case 5 -> productService.searchProduct();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }



}
