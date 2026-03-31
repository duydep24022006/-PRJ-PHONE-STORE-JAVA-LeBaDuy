package dao;

import model.OrderDetail;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {
    private Connection conn ;
    public OrderDetailDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<OrderDetail> getAllOrderDetail() {
        List<OrderDetail> list =new ArrayList<>();
        String sql="select * from order_details";
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
        String sql = "insert into order_details(order_id, product_id, quantity, price) values (?,?,?,?)";
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
        String sql = "delete from order_details where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<OrderDetail> getDetailsByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.id, od.order_id, od.product_id, od.quantity, od.price, p.name AS product_name " +
                    "FROM order_details od " +
                    "JOIN product p ON od.product_id = p.id " +
                    "WHERE od.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail od = new OrderDetail();
                od.setId(rs.getInt("id"));
                od.setOrderId(rs.getInt("order_id"));
                od.setProductId(rs.getInt("product_id"));
                od.setQuantity(rs.getInt("quantity"));
                od.setPrice(rs.getDouble("price"));
                od.setProductName(rs.getString("product_name")); // thêm tên sản phẩm
                list.add(od);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
