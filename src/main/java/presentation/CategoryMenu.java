package presentation;

import service.CategoryService;
import java.util.Scanner;

public class CategoryMenu {
    private CategoryService categoryService=new CategoryService();
    Scanner sc=new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("=== QUAN LY DANH MUC ===");
            System.out.println("1. Them moi danh muc");
            System.out.println("2. Hien thi danh muc");
            System.out.println("3. Xoa danh muc");
            System.out.println("4. Cap nhat danh muc");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> categoryService.addCategory();
                case 2 -> categoryService.showCategories();
                case 3 -> categoryService.deleteCategory();
                case 4 -> categoryService.updateCategory();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }


}
