package service;

import dao.CartDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import model.*;
import util.Validator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private Customer customer;
    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private ReportService reportService = new ReportService();
    private CartDAO cartDAO = new CartDAO();
    private Integer currentCartId = null;
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void viewProducts() {
        List<Product> products = productDAO.getAllProducts();
        System.out.printf("%-5s %-25s %-15s %-12s %-15s %-15s %-10s\n",
                "ID", "Ten san pham", "Hang", "Dung luong", "Gia goc (VND)", "Gia Flash Sale", "Ton kho");
        System.out.println("-----------------------------------------------------------------------------------------------------------");

        for (Product p : products) {
            String flashSaleDisplay = "-";

            if (p.getFlashSalePrice() != null && p.getFlashSalePrice() > 0
                    && p.getFlashSaleQuantity() > 0 && p.getFlashSaleExpiry() != null
                    && p.getFlashSaleExpiry().isAfter(LocalDateTime.now())) {

                flashSaleDisplay = Validator.formatMoney(p.getFlashSalePrice());
            }
            System.out.printf("%-5d %-25s %-15s %-12s %-15s %-15s  %-10d\n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getCapacity(),
                    Validator.formatMoney(p.getPrice()),
                    flashSaleDisplay,
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
        orders.sort((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
        if (orders.isEmpty()) {
            System.out.println("Ban chua co don hang nao!");
            return;
        }
        System.out.printf("%-5s %-15s %-25s %-20s %-10s %-15s %-15s %-12s %-15s %-20s %12s\n",
                "ID", "Khach hang", "Email", "San pham", "SL", "Gia (VND)", "Thanh tien", "Ma giam gia", "Tong sau giam", "Ngay tao","Trang thai");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");

        for (Orders o : orders) {
            double lineTotal = o.getQuantity() * o.getPrice();
            System.out.printf("%-5d %-15s %-25s %-20s %-10d %-15s %-15s %-12s %-15s %-20s %-12s\n",
                    o.getId(),
                    o.getCustomerName(),
                    o.getCustomerEmail(),
                    o.getProductName(),
                    o.getQuantity(),
                    Validator.formatMoney(o.getPrice()),
                    Validator.formatMoney(lineTotal),
                    (o.getCouponCode() == null ? "Khong co" : o.getCouponCode()),
                    Validator.formatMoney(o.getFinalTotal()),
                    o.getCreatedAt(),
                    o.getStatus());
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
            System.out.println("Khong tim thay hang");
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
    public void addToCart(){
        try {
            System.out.print("Nhap ID san pham muon mua: ");
            int id = validateInt();

            Product p = productDAO.getById(id);
            if (p == null) {
                System.out.println("San pham khong ton tai.");
                return;
            }
            int quantity ;
            while (true) {
                System.out.print("Nhap so luong: ");
                    quantity = validateInt();

                if (quantity <= 0) {
                    System.out.println("So luong phai lon hon 0.");
                    continue;
                }

                if (quantity > p.getStock()) {
                    System.out.println("So luong vuot qua so luong san pham trong kho.");
                    continue;
                }

                break;
            }
            if (currentCartId == null) {
                currentCartId = cartDAO.createCart(customer.getId());
            }
            cartDAO.addItem(currentCartId, p.getId(), quantity);
        } catch (Exception e) {
            System.out.println("Co loi xay ra khi them vao gio hang: " + e.getMessage());
        }
    }
    public void showCart() {
        try {
            if (currentCartId == null) {
                System.out.println("Gio hang dang trong.");
                return;
            }
            List<CartItem> items = cartDAO.getItems(currentCartId);
            if (items.isEmpty()) {
                System.out.println("Gio hang dang trong.");
                return;
            }

            System.out.println("=== Danh sach gio hang ===");
            System.out.printf("%-5s %-20s %-10s %-15s %-15s%n",
                    "ID", "Ten san pham", "So luong", "Gia (VND)", "Tong tien");
            System.out.println("--------------------------------------------------------------------------");

            double grandTotal = 0;
            for (CartItem item : items) {
                Product p = item.getProduct();
                double lineTotal = p.getPrice() * item.getQuantity();
                grandTotal += lineTotal;

                System.out.printf("%-5d %-20s %-10d %-15.2f %-15.2f%n",
                        p.getId(),
                        p.getName(),
                        item.getQuantity(),
                        p.getPrice(),
                        lineTotal);
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-15s %-15.2f%n",
                    "", "", "", "Tong cong:", grandTotal);
        } catch (SQLException e) {
            System.out.println("Co loi khi hien thi gio hang: " + e.getMessage());
        }
    }
    public void removeFromCart() {
        System.out.print("Nhap ID san pham muon xoa: ");
        int productId = validateInt();
        try {
            if (currentCartId == null) {
                System.out.println("Ban chua co gio hang.");
                return;
            }
            cartDAO.removeItem(currentCartId, productId);

            List<CartItem> items = cartDAO.getItems(currentCartId);
            if (items.isEmpty()) {
                currentCartId = null;
                System.out.println("Gio hang da trong sau khi xoa.");
            } else {
                System.out.println("Da xoa san pham co ID " + productId + " khoi gio hang.");
            }
        } catch (SQLException e) {
            System.out.println("Co loi khi xoa san pham: " + e.getMessage());
        }
    }

    public void clearCart() {
        try {
            if (currentCartId == null) {
                System.out.println("Ban chua co gio hang.");
                return;
            }
            cartDAO.clearCart(currentCartId);
            currentCartId = null;
            System.out.println("Da xoa toan bo gio hang.");
        } catch (SQLException e) {
            System.out.println("Co loi khi xoa gio hang: " + e.getMessage());
        }
    }

    public void checkoutCart() {
        try {
            if (currentCartId == null) {
                System.out.println("Ban chua co gio hang.");
                return;
            }

            List<CartItem> items = cartDAO.getItems(currentCartId);
            if (items.isEmpty()) {
                System.out.println("Gio hang dang trong.");
                return;
            }

            // Tạo đơn hàng
            int orderId = orderDAO.createOrder(customer.getId());

            // Thêm từng sản phẩm vào order_items
            for (CartItem item : items) {
                Product p = item.getProduct();
                orderDAO.addOrderItem(orderId, p.getId(), item.getQuantity(), p.getPrice());
            }

            // Hỏi mã giảm giá
            System.out.print("Ban co ma giam gia khong? (Nhap code hoac Enter bo qua): ");
            String code = sc.nextLine().trim();
            if (!code.isEmpty()) {
                double finalAmount = orderDAO.applyCoupon(orderId, code);
                if (finalAmount > 0) {
                    System.out.println("Tong tien sau giam: " + Validator.formatMoney(finalAmount) + " VND");
                }
            }

            System.out.println("Dat hang tu gio hang thanh cong!");


            cartDAO.clearCart(currentCartId);
            currentCartId = null;

        } catch (SQLException e) {
            System.out.println("Co loi khi dat hang tu gio hang: " + e.getMessage());
        }
    }

}
