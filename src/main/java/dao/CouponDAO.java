package dao;

import entity.Coupon;
import util.DBConnection;

import java.sql.Connection;

public class CouponDAO {
    private Connection conn;

    public CouponDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
