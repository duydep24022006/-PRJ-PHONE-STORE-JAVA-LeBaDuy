package service;

import dao.CategoryDAO;
import model.Category;
import java.util.List;
import java.util.Scanner;

public class CategoryService {
    private CategoryDAO categoryDAO=new CategoryDAO();
    Scanner sc=new Scanner(System.in);
    public void addCategory() {
        System.out.println("=== Them moi danh muc ===");
        System.out.print("Ten danh muc: ");
        String name = sc.nextLine();
        System.out.print("Mo ta: ");
        String description = sc.nextLine();

        Category c=new Category();
        c.setName(name);
        c.setDescription(description);
        categoryDAO.addCategory(c);
        System.out.println("Them danh muc thanh cong");
    }
    public void showCategories() {
        System.out.println("=== Danh sach danh muc ===");
        List<Category> list = categoryDAO.getAllCategories();
        System.out.printf("%-5s  %-20s %-30s%n", "ID", "Ten", "Mo ta");
        System.out.println("-------------------------------------------------------------");

        for (Category c : list) {
            System.out.printf("%-5d %-20s %-30s%n",
                    c.getId(),
                    c.getName(),
                    c.getDescription() == null ? "" : c.getDescription());
        }
        System.out.println("---------------------------------------------------------------");
    }
    public void deleteCategory() {
        System.out.println("=== Xoa danh muc ===");
        System.out.print ("Nhap ID danh muc can xoa: ");
        int id = sc.nextInt();
        sc.nextLine();
        categoryDAO.deleteCategory(id);
        System.out.println("Xoa danh muc thanh cong!");
    }

    public void updateCategory() {
        System.out.println("=== Cap nhat danh muc ===");
        System.out.print("Nhap ID danh muc can cap nhat: ");
        int id = sc.nextInt();
        sc.nextLine();

        Category old = categoryDAO.getById(id);
        if (old == null) {
            System.out.println("Khong tim thay danh muc!");
            return;
        }

        // Validate tên
        System.out.printf("Ten hien tai: %s. Sua? (Y/N): ", old.getName());
        String choice = sc.nextLine();
        String name = old.getName();
        if (choice.equalsIgnoreCase("Y")) {
            do {
                System.out.print("Nhap ten moi (khong duoc de trong): ");
                name = sc.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Ten khong duoc bo trong!");
                }
            } while (name.isEmpty());
        }

        // Validate mô tả
        System.out.printf("Mo ta hien tai: %s. Sua? (Y/N): ", old.getDescription());
        choice = sc.nextLine();
        String description = old.getDescription();
        if (choice.equalsIgnoreCase("Y")) {
            do {
                System.out.print("Nhap mo ta moi (khong duoc de trong): ");
                description = sc.nextLine().trim();
                if (description.isEmpty()) {
                    System.out.println("Mo ta khong duoc bo trong!");
                }
            } while (description.isEmpty());
        }

        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setDescription(description);

        categoryDAO.updateCategory(c);
        System.out.println("Cap nhat danh muc thanh cong!");
    }
    public int validateInt() {
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
