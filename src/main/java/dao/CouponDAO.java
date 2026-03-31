package dao;

import model.Coupon;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {
    private Connection conn;

    public CouponDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Coupon> getAllCoupons() {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupon";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToCoupon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setId(rs.getInt("id"));
        c.setCode(rs.getString("code"));
        c.setDiscountPercent(rs.getInt("discount_percent"));
        c.setExpiryDate(rs.getTimestamp("expiry_date"));
        c.setUsageLimit(rs.getInt("usage_limit"));
        c.setUsedCount(rs.getInt("used_count"));
        return c;
    }
    public void addCoupon(Coupon coupon){
        String sql ="INSERT INTO coupon(code, discount_percent, expiry_date, usage_limit, used_count) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, coupon.getCode());
            ps.setInt(2, coupon.getDiscountPercent());
            ps.setTimestamp(3, coupon.getExpiryDate());
            ps.setInt(4, coupon.getUsageLimit());
            ps.setInt(5, coupon.getUsedCount());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Coupon searchByCode(String code) {
        String sql = "SELECT * FROM coupon WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCoupon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateUsage(String code) {
        String sql = "UPDATE coupon SET used_count = used_count + 1 WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCoupon(int id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isValidCoupon(String code) {
        String sql = "SELECT * FROM coupon WHERE code = ?";
        try(PreparedStatement sp=conn.prepareStatement(sql)){
            sp.setString(1, code);
            ResultSet rs=sp.executeQuery();
            if(rs.next()){
                Timestamp expiry = rs.getTimestamp("expiry_date");
                int usageLimit = rs.getInt("usage_limit");
                int usedCount = rs.getInt("used_count");

                if (expiry != null && expiry.before(new java.util.Date())) {
                    return false; // hết hạn
                }
                if (usedCount >= usageLimit) {
                    return false; // hết lượt
                }
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public int getDiscountPercent(String code) {
        String sql = "SELECT discount_percent FROM coupon WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("discount_percent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void incrementUsage(String code) {
        String sql = "UPDATE coupon SET used_count = used_count + 1 WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
