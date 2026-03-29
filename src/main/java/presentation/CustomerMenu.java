package presentation;

import entity.Customer;
import service.CustomerService;

public class CustomerMenu {
    private Customer customer;
    private CustomerService customerService = new CustomerService();
    public void setCustomer(Customer customer) {
        this.customer = customer;
        customerService.setCustomer(customer);
    }


    public void showMenu() {
        int choice;
        do {
            System.out.println("=== MENU KHACH HANG ===");
            System.out.println("1. Xem danh sach san pham");
            System.out.println("2. Tim kiem san pham theo ten");
            System.out.println("3. Dat hang");
            System.out.println("4. Xem lich su don hang");
            System.out.println("5. Cap nhat thong tin ca nhan");
            System.out.println("6. Loc san pham theo hang");
            System.out.println("7. Loc san pham theo gia");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            choice = customerService.validateInt();

            switch (choice) {
                case 1 -> customerService.viewProducts();
                case 2 -> customerService.searchProductByName();
                case 3 -> customerService.placeOrder();
                case 4 -> customerService.viewOrders();
                case 5 -> customerService.updateProfile();
                case 6-> customerService.fillterByBrand();
                case 7 -> customerService.filterByPrice();
                case 0 -> System.out.println("Thoat menu khach hang.");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }




}
