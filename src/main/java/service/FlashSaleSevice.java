package service;

import dao.ProductDAO;
import model.Product;
import util.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FlashSaleSevice {
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();

    public void createFlashSale() {
        System.out.print("Nhap ID san pham: ");
        int productId = sc.nextInt();
        sc.nextLine();
        Product p = productDAO.getById(productId);
        if (p == null) {
            System.out.println("Khong tim thay san pham voi ID: " + productId);
            return;
        }

        // Kiểm tra nếu sản phẩm đang có flash sale hợp lệ
        if (p.getFlashSalePrice() != null && p.getFlashSaleExpiry() != null
                && p.getFlashSaleExpiry().isAfter(LocalDateTime.now())) {
            System.out.println("San pham ID " + productId + " dang co flash sale den "
                    + p.getFlashSaleExpiry().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    + ". Vui long xoa hoac doi het han truoc khi tao moi.");
            return;
        }
        System.out.print("Nhap gia flash sale: ");
        double flashPrice = sc.nextDouble();
        sc.nextLine();

        System.out.print("Nhap so luong flash sale: ");
        int flashQuantity = sc.nextInt();
        sc.nextLine();

        System.out.print("Nhap ngay het han (yyyy-MM-dd HH:mm:ss): ");
        String expiryStr = sc.nextLine();
        LocalDateTime expiry = LocalDateTime.parse(expiryStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        productDAO.createFlashSale(productId, flashPrice, flashQuantity, expiry);
    }
    public void removeFlashSale() {
        System.out.print("Nhap ID san pham muon xoa flash sale: ");
        int productId = sc.nextInt();
        sc.nextLine();

        productDAO.removeFlashSale(productId);
    }
    public void viewFlashSaleProducts() {
        List<Product> products = productDAO.getAllProducts();
        System.out.printf("%-5s %-25s %-15s %-15s %-15s %-10s %-10s %-20s\n",
                "ID", "Ten san pham", "Hang", "Gia goc (VND)", "Gia Flash Sale", "Giam (%)", "Ton kho", "Het han");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (Product p : products) {
            if (p.getFlashSalePrice() != null && p.getFlashSaleExpiry() != null
                    && p.getFlashSaleExpiry().isAfter(LocalDateTime.now())) {
                double discountPercent = ((p.getPrice() - p.getFlashSalePrice()) / p.getPrice()) * 100;
                System.out.printf("%-5d %-25s %-15s %-15s %-15s %-10.0f%% %-10d %-20s\n",
                        p.getId(),
                        p.getName(),
                        p.getBrand(),
                        Validator.formatMoney(p.getPrice()),
                        Validator.formatMoney(p.getFlashSalePrice()),
                        discountPercent,
                        p.getStock(),
                        p.getFlashSaleExpiry().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        }
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
