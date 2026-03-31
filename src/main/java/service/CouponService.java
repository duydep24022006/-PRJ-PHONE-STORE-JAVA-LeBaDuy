package service;

import dao.CouponDAO;
import model.Coupon;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class CouponService {
    private Scanner sc = new Scanner(System.in);
    private CouponDAO couponDAO = new CouponDAO();

    public  void addCoupon(){
        System.out.print("Nhap ma giam gia: ");
        String code = sc.nextLine();
        System.out.print("Nhap % giam gia: ");
        int discountPercent = sc.nextInt();
        sc.nextLine();
        System.out.print("Nhap ngay het han (yyyy-MM-dd HH:mm:ss): ");
        String expiryStr = sc.nextLine();
        Timestamp expiryDate = Timestamp.valueOf(expiryStr);
        System.out.print("Nhap gioi han so lan su dung: ");
        int usageLimit = sc.nextInt();
        sc.nextLine();
        Coupon c = new Coupon();
        c.setCode(code);
        c.setDiscountPercent(discountPercent);
        c.setExpiryDate(expiryDate);
        c.setUsageLimit(usageLimit);
        c.setUsedCount(0);

        couponDAO.addCoupon(c);
        System.out.println("Them ve giam gia thanh cong!");
    }
    public void viewCoupons() {
        List<Coupon> coupons = couponDAO.getAllCoupons();
        System.out.printf("%-5s %-15s %-12s %-12s %-12s %-20s\n",
                "ID", "Ma giam gia", "Phan tram", "Gioi han", "Da dung", "Ngay het han");
        System.out.println("--------------------------------------------------------------------------------------");
        for (Coupon c : coupons) {
            System.out.printf("%-5d %-15s %-12d %-12d %-12d %-20s\n",
                    c.getId(),
                    c.getCode(),
                    c.getDiscountPercent(),
                    c.getUsageLimit(),
                    c.getUsedCount(),
                    c.getExpiryDate());
        }
    }
    public void deleteCoupon() {
        System.out.print("Nhap ID giam gia muon xoa: ");
        int id = sc.nextInt();
        sc.nextLine();
        couponDAO.deleteCoupon(id);
        System.out.println("Xoa giam gia thanh cong!");
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
