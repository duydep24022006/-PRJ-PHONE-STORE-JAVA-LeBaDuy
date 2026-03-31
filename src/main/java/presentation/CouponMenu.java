package presentation;

import service.CouponService;
import java.util.Scanner;

public class CouponMenu {
    private Scanner sc = new Scanner(System.in);
    private CouponService couponService = new CouponService();
    public void showMenu() {
        while (true) {
            System.out.println("=== QUAN LY MA GIAM GIA ===");
            System.out.println("1. Them ma giam gia moi");
            System.out.println("2. Xem danh sach ma giam gia");
            System.out.println("3. Xoa ma giam gia");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            int choice = couponService.validateInt();
            switch (choice) {
                case 1 -> couponService.addCoupon();
                case 2 -> couponService.viewCoupons();
                case 3 -> couponService.deleteCoupon();
                case 0 -> { return; }
                default -> System.out.println("Lua chon khong hop le!");
            }
        }
    }
}
