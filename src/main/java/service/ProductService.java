package service;

import dao.CategoryDAO;
import dao.ProductDAO;
import model.Category;
import model.Product;
import util.Validator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ProductService {
    private ProductDAO productDAO = new ProductDAO();
    private Scanner sc = new Scanner(System.in);
    DecimalFormat df = new DecimalFormat("#,###");
    public void addProduct() {
        System.out.println("=== Them moi san pham ===");
        System.out.print("Ten: ");
        String name = sc.nextLine();
        System.out.print("Hang san xuat: ");
        String brand = sc.nextLine();
        System.out.print("Dung luong: ");
        String capacity = sc.nextLine();
        System.out.print("Mau sac: ");
        String color = sc.nextLine();
        System.out.print("Gia ban: ");
        double price = sc.nextDouble();
        System.out.print("So luong trong kho: ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.print("Mo ta: ");
        String description = sc.nextLine();
        System.out.println("=== Danh sach danh muc ===");
        CategoryDAO categoryDAO=new CategoryDAO();
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
        System.out.println();
        System.out.print("Nhap ID danh muc cho san pham: ");
        int categoryId = sc.nextInt();
        sc.nextLine();

        Product p = new Product();
        p.setName(name);
        p.setBrand(brand);
        p.setCapacity(capacity);
        p.setColor(color);
        p.setPrice(price);
        p.setStock(stock);
        p.setDescription(description);
        p.setCategoryId(categoryId);
        productDAO.addProduct(p);
        System.out.println("Them san pham thanh cong!");
    }
    public void updateProduct() {
        System.out.println("=== Sua thong tin san pham ===");
        System.out.print("Nhap ID san pham can sua: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product old = productDAO.getById(id);
        if (old == null) {
            System.out.println("Khong tim thay san pham!");
            return;
        }

        System.out.printf("Ten hien tai: %s. Sua? (Y/N): ", old.getName());
        String choice = sc.nextLine();
        String name = choice.equalsIgnoreCase("Y") ? sc.nextLine() : old.getName();

        System.out.printf("Hang hien tai: %s. Sua? (Y/N): ", old.getBrand());
        choice = sc.nextLine();
        String brand = choice.equalsIgnoreCase("Y") ? sc.nextLine() : old.getBrand();

        System.out.printf("Dung luong hien tai: %s. Sua? (Y/N): ", old.getCapacity());
        choice = sc.nextLine();
        String capacity = choice.equalsIgnoreCase("Y") ? sc.nextLine() : old.getCapacity();

        System.out.printf("Mau hien tai: %s. Sua? (Y/N): ", old.getColor());
        choice = sc.nextLine();
        String color = choice.equalsIgnoreCase("Y") ? sc.nextLine() : old.getColor();

        System.out.printf("Gia hien tai: %.2f. Sua? (Y/N): ", old.getPrice());
        choice = sc.nextLine();
        double price = choice.equalsIgnoreCase("Y") ? sc.nextDouble() : old.getPrice();
        sc.nextLine();

        System.out.printf("So luong hien tai: %d. Sua? (Y/N): ", old.getStock());
        choice = sc.nextLine();
        int stock = choice.equalsIgnoreCase("Y") ? sc.nextInt() : old.getStock();
        sc.nextLine();

        System.out.printf("Mo ta hien tai: %s. Sua? (Y/N): ", old.getDescription());
        choice = sc.nextLine();
        String description = choice.equalsIgnoreCase("Y") ? sc.nextLine() : old.getDescription();

        System.out.printf("Danh muc hien tai: %d. Sua? (Y/N): ", old.getCategoryId());
        choice = sc.nextLine();
        int categoryId = old.getCategoryId();
        if (choice.equalsIgnoreCase("Y")) {
            CategoryDAO categoryDAO = new CategoryDAO();
            List<Category> list = categoryDAO.getAllCategories();
            System.out.printf("%-5s  %-20s %-30s%n", "ID", "Ten", "Mo ta");
            System.out.println("-------------------------------------------------------------");
            for (Category c : list) {
                System.out.printf("%-5d %-20s %-30s%n",
                        c.getId(),
                        c.getName(),
                        c.getDescription() == null ? "" : c.getDescription());
            }
            System.out.println("-------------------------------------------------------------");
            System.out.print("Nhap ID danh muc moi: ");
            categoryId = sc.nextInt();
            sc.nextLine();
        }

        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setBrand(brand);
        p.setCapacity(capacity);
        p.setColor(color);
        p.setPrice(price);
        p.setStock(stock);
        p.setDescription(description);
        p.setCategoryId(categoryId);

        productDAO.updateProduct(p);
        System.out.println("Cap nhat san pham thanh cong!");
    }
    public void deleteProduct() {
        System.out.println("=== Xoa san pham ===");
        System.out.print("Nhap ID san pham can xoa: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product old = productDAO.getById(id);
        if (old == null) {
            System.out.println("Khong tim thay san pham!");
            return;
        }

        System.out.printf("Ban co chac muon xoa san pham '%s'? (Y/N): ", old.getName());
        String choice = sc.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            productDAO.deleteProduct(id);
            System.out.println("Xoa san pham thanh cong!");
        } else {
            System.out.println("Huy thao tac xoa.");
        }
    }
    public void searchProduct() {
        System.out.println("=== Tim kiem san pham ===");
        System.out.print("Nhap ten san pham can tim: ");
        String keyword = sc.nextLine();

        List<Product> list = productDAO.searchProductByName(keyword);
        if (list.isEmpty()) {
            System.out.println("Khong tim thay san pham!");
            return;
        }

        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s %-10s%n",
                "ID", "Ten", "Hang", "Dung luong", "Mau", "Gia", "Danh muc");
        System.out.println("--------------------------------------------------------------------------------");

        for (Product p : list) {
            System.out.printf("%-5d %-20s %-15s %-10s %-10s %-10s %-10d%n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getCapacity(),
                    p.getColor(),
                    Validator.formatMoney(p.getPrice()),
                    p.getCategoryId());
        }
    }

    public void showProducts() {
        System.out.println("=== Danh sach san pham ===");
        List<Product> list = productDAO.getAllProducts();
        if (list.isEmpty()) {
            System.out.println("Chua co san pham nao!");
            return;
        }

        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s %-10s%n",
                "ID", "Ten", "Hang", "Dung luong", "Mau", "Gia", "Danh muc");
        System.out.println("--------------------------------------------------------------------------------");

        for (Product p : list) {
            System.out.printf("%-5d %-20s %-15s %-10s %-10s %-10s %-10d%n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getCapacity(),
                    p.getColor(),
                    Validator.formatMoney(p.getPrice()),
                    p.getCategoryId());
        }
    }

}
