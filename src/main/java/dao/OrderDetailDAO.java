package dao;

import entity.OrderDetail;
import entity.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private Connection conn = DBConnection.getInstance().getConnection();
    public List<OrderDetail> getAllOrderDetail() {
        List<OrderDetail> list =new ArrayList<>();
        String sql="select * from product";
        try(Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sql)){
            while (rs.next()){
                list.add(mapResultSetToOrderDetail(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    private OrderDetail mapResultSetToOrderDetail(ResultSet rs) throws SQLException {
        OrderDetail od = new OrderDetail();
        od.setId(rs.getInt("id"));
        od.setOrderId(rs.getInt("order_id"));
        od.setProductId(rs.getInt("product_id"));
        od.setQuantity(rs.getInt("quantity"));
        od.setPrice(rs.getDouble("price"));
        return od;
    }

    public void addOrderDetail(OrderDetail od) {
        String sql = "INSERT INTO order_details(order_id, product_id, quantity, price) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, od.getOrderId());
            ps.setInt(2, od.getProductId());
            ps.setInt(3, od.getQuantity());
            ps.setDouble(4, od.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteOrderDetail(int id) {
        String sql = "DELETE FROM order_details WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
