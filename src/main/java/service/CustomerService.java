package service;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Customer;
import model.Orders;
import model.Product;
import util.Validator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private Customer customer;
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private ReportService reportService = new ReportService();
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void viewProducts() {
        List<Product> products = productDAO.getAllProducts();
        System.out.printf("%-5s %-25s %-15s %-12s %-15s %-15s %-10s %-10s\n",
                "ID", "Ten san pham", "Hang", "Dung luong", "Gia goc (VND)", "Gia Flash Sale", "Giam (%)", "Ton kho");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (Product p : products) {
            // Mặc định không có flash sale
            String flashSaleDisplay = "-";
            String discountPercentDisplay = "-";

            // Nếu có flash sale hợp lệ (còn số lượng, chưa hết hạn)
            if (p.getFlashSalePrice() != null && p.getFlashSalePrice() > 0
                    && p.getFlashSaleQuantity() > 0 && p.getFlashSaleExpiry() != null
                    && p.getFlashSaleExpiry().isAfter(LocalDateTime.now())) {

                flashSaleDisplay = Validator.formatMoney(p.getFlashSalePrice());

                // Tính phần trăm giảm giá
                double discountPercent = ((p.getPrice() - p.getFlashSalePrice()) / p.getPrice()) * 100;
                discountPercentDisplay = String.format("%.0f%%", discountPercent);
            }

            System.out.printf("%-5d %-25s %-15s %-12s %-15s %-15s %-10s %-10d\n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getCapacity(),
                    Validator.formatMoney(p.getPrice()),
                    flashSaleDisplay,
                    discountPercentDisplay,
                    p.getStock());
        }
    }

    public void searchProductByName() {
        System.out.print("Nhap ten san pham can tim: ");
        String name = sc.nextLine();
        List<Product> products = productDAO.searchProductByName(name);
        if (products.isEmpty()) {
            System.out.println("Khong tim thay san pham phu hop!");
            return;
        }
        System.out.printf("%-5s %-25s %-15s %-12s %-12s %-10s\n",
                "ID", "Ten san pham", "Hang", "Dung luong", "Gia (VND)", "Ton kho");
        System.out.println("----------------------------------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-5d %-25s %-15s %-12s %-12.2f %-10d\n",
                    p.getId(), p.getName(), p.getBrand(), p.getCapacity(), p.getPrice(), p.getStock());
        }

    }
    public void placeOrder() {
        System.out.print("Nhap ID san pham muon mua: ");
        int productId = validateInt();
        System.out.print("Nhap so luong: ");
        int quantity = validateInt();

        Product product = productDAO.getById(productId);
        if (product == null) {
            System.out.println("San pham khong ton tai!");
            return;
        }
        if (quantity <= 0) {
            System.out.println("So luong phai lon hon 0!");
            return;
        }
        if (product.getStock() < quantity) {
            System.out.println("So luong vuot qua so luong trong kho!");
            return;
        }

        int orderId= orderDAO.createOrder(customer.getId(), productId, quantity);

        // Hỏi mã giảm giá
        System.out.print("Ban co ma giam gia khong? (Nhap code hoac Enter bo qua): ");
        String code = sc.nextLine().trim();
        if (!code.isEmpty()) {
            double finalAmount = orderDAO.applyCoupon(orderId, code);
            if (finalAmount > 0) {
                System.out.println("Tong tien sau giam: " + Validator.formatMoney(finalAmount) + " VND");
            }
        }
        System.out.println("Dat hang thanh cong!");
    }

    public void updateProfile() {
        System.out.println("=== Cap nhat thong tin ca nhan ===");
        System.out.printf("Ten hien tai: %s.? (Y/N): ", customer.getName());
        String choice = sc.nextLine();
        String name = customer.getName();
        if (choice.equalsIgnoreCase("N")) {
            do {
                System.out.print("Nhap ten moi: ");
                name = sc.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Ten khong duoc de trong!");
                }
            } while (name.isEmpty());
        }

        // Email
        System.out.printf("Email hien tai: %s.? (Y/N): ", customer.getEmail());
        choice = sc.nextLine();
        String email = customer.getEmail();
        if (choice.equalsIgnoreCase("N")) {
            do {
                System.out.print("Nhap email moi: ");
                email = sc.nextLine().trim();
                if (!Validator.isValidEmail(email)) {
                    System.out.println("Email khong hop le!");
                    email = "";
                }
            } while (email.isEmpty());
        }

        // SDT
        System.out.printf("SDT hien tai: %s. ? (Y/N): ", customer.getPhone());
        choice = sc.nextLine();
        String phone = customer.getPhone();
        if (choice.equalsIgnoreCase("N")) {
            do {
                System.out.print("Nhap SDT moi: ");
                phone = sc.nextLine().trim();
                if (!Validator.isValidPhone(phone)) {
                    System.out.println("So dien thoai khong hop le!");
                    phone = "";
                }
            } while (phone.isEmpty());
        }

        // Địa chỉ
        System.out.printf("Dia chi hien tai: %s.? (Y/N): ", customer.getAddress());
        choice = sc.nextLine();
        String address = customer.getAddress();
        if (choice.equalsIgnoreCase("N")) {
            do {
                System.out.print("Nhap dia chi moi: ");
                address = sc.nextLine().trim();
                if (address.isEmpty()) {
                    System.out.println("Dia chi khong duoc de trong!");
                }
            } while (address.isEmpty());
        }

        // Cập nhật đối tượng
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        // Gọi DAO để lưu
        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.updateCustomer(customer);

        System.out.println("Cap nhat thong tin thanh cong!");
    }
    public void viewOrders() {
        System.out.println("=== Lich su don hang ===");
        List<Orders> orders = orderDAO.getOrdersByCustomer(customer.getId());

        if (orders.isEmpty()) {
            System.out.println("Ban chua co don hang nao!");
            return;
        }
        System.out.printf("%-5s %-15s %-25s %-20s %-10s %-15s %-15s %-12s %-15s %-20s\n",
                "ID", "Khach hang", "Email", "San pham", "SL", "Gia (VND)", "Thanh tien", "Ma giam gia", "Tong sau giam", "Ngay tao");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");

        for (Orders o : orders) {
            double lineTotal = o.getQuantity() * o.getPrice();
            System.out.printf("%-5d %-15s %-25s %-20s %-10d %-15s %-15s %-12s %-15s %-20s\n",
                    o.getId(),
                    o.getCustomerName(),
                    o.getCustomerEmail(),
                    o.getProductName(),
                    o.getQuantity(),
                    Validator.formatMoney(o.getPrice()),
                    Validator.formatMoney(lineTotal),
                    (o.getCouponCode() == null ? "Khong co" : o.getCouponCode()),
                    Validator.formatMoney(o.getFinalTotal()),

                    o.getCreatedAt());
        }
    }


    public void displayProducts(List<Product> products) {
        System.out.printf("%-5s %-25s %-15s %-12s %-12s %-10s\n",
                "ID", "Ten san pham", "Hang", "Dung luong", "Gia (VND)", "Ton kho");
        System.out.println("----------------------------------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-5d %-25s %-15s %-12s %-12s %-10d\n",
                    p.getId(), p.getName(), p.getBrand(), p.getCapacity(), Validator.formatMoney(p.getPrice()), p.getStock());
        }
    }
    public void fillterByBrand() {
        System.out.printf("Nhap ten hang: ");
        String brand = sc.nextLine();
        List<Product> products = productDAO
                .searchByBrand(brand);
        if(products.isEmpty()) {
            System.out.println("Khong tim thay san pham");
            return;
        }
        displayProducts(products);
    }
    public void filterByPrice() {
        System.out.print("Nhap gia thap nhat: ");
        double min = validateDouble();
        System.out.print("Nhap gia cao nhat: ");
        double max = validateDouble();
        if (min < 0 || max < 0 || min > max) {
            System.out.println("Khoang gia khong hop le!");
            return;
        }
        List<Product> products = productDAO.searchByPriceRange(min, max);
        if (products.isEmpty()) {
            System.out.println("Khong tim thay san pham!");
            return;
        }
        displayProducts(products);
    }
    public int validateInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Nhap sai, vui long nhap so nguyen: ");
            }
        }
    }
    public double validateDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Nhap sai, vui long nhap so thuc: ");
            }
        }
    }
    public void topProducts(){
        reportService.reportTop5();
    }

}
