package dao;

import model.CartItem;
import model.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private Connection conn;
    public CartDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int createCart(int customerId) throws SQLException {
        String sql = "insert into cart (customer_id) values (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void addItem(int cartId, int productId, int quantity) throws SQLException {
        String sql = "insert into cart_item (cart_id, product_id, quantity) values (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }


    public List<CartItem> getItems(int cartId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "select p.*, ci.quantity from cart_item ci join product p on ci.product_id=p.id where ci.cart_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                CartItem item = new CartItem(p, rs.getInt("quantity"));
                items.add(item);
            }
        }
        return items;
    }
    public void removeItem(int cartId, int productId) throws SQLException {
        String sql = "delete from cart_item where cart_id=? and product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Da xoa san pham co ID " + productId + " khoi gio hang.");
            } else {
                System.out.println("Khong tim thay san pham co ID " + productId + " trong gio hang.");
            }
        }
    }

    // xoa toan bo gio hang
    public void clearCart(int cartId) throws SQLException {
        String sql = "delete from cart_item where cart_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }
}
