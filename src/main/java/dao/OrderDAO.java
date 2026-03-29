package dao;

import entity.Orders;
import util.DBConnection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection conn= DBConnection.getInstance().getConnection();
    public List<Orders> getAllOrders(){
        List<Orders> list=new ArrayList<>();
        String sql="select * from orders";
        try(Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql)){
            while(rs.next()){
                list.add(mapResultSetToOrder(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    private Orders mapResultSetToOrder(ResultSet rs) throws SQLException {
        Orders o=new Orders();
        o.setId(rs.getInt("id"));
        o.setCustomer_id(rs.getInt("customer_id"));
        o.setStatus(rs.getString("status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
    public void addOrder(Orders o){
        String sql ="insert into orders(custtomer_id,statust) value (?,?)";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,o.getCustomer_id());
            ps.setString(2,o.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();        }
    }
    public Orders getOrderById(int id) {
        String sql = "select * from orders where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateOrder(int id, String status){
        String sql="update orders set status=? where id=?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,status);
            ps.setInt(2,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteOrder(int id){
        String sql="delete from orders where id=?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(2,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public void createOrder(int customer_id, int productId,int quantity){
        String insertOrderSQL="insert into orders(customer_id, status) values(?,'pending')";
    String insertDetailSQL = "INSERT INTO order_details(order_id, product_id, quantity, price) VALUES (?,?,?,?)";
        String updateStockSQL="update product set stock = stock - ? where id = ?";
        String priceProductSQL="SELECT price FROM product WHERE id=?";
        try{
            conn.setAutoCommit(false);
            // 1. Tạo order
            int orderId=-1;
            try(PreparedStatement ps=conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,customer_id);
                ps.executeUpdate();
                ResultSet rs=ps.getGeneratedKeys();
                if(rs.next()){
                    orderId=rs.getInt(1);
                }
            }
            // 2. Lấy giá sản phẩm
            double price=0;
            try(PreparedStatement ps=conn.prepareStatement(priceProductSQL)){
                ps.setInt(1,productId);
                ResultSet rs=ps.executeQuery();
                if(rs.next()){
                    price=rs.getDouble("price");
                }
            }
            // 3. Thêm order_details
            try(PreparedStatement ps=conn.prepareStatement(insertDetailSQL)){
                ps.setInt(1,orderId);
                ps.setInt(2,productId);
                ps.setInt(3,quantity);
                ps.setDouble(4,price);
                ps.executeUpdate();
            }
            // 4. Giảm stock
            try(PreparedStatement ps=conn.prepareStatement(updateStockSQL)){
                ps.setInt(1,quantity);
                ps.setInt(2,productId);
                ps.executeUpdate();
            }
            conn.commit();
            System.out.println("Tao don hang thanh cong!");
        }catch (SQLException e){
            try {
                conn.rollback(); // rollback nếu lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();

        }finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Orders> getOrdersByCustomer(int customer_id){
        List<Orders> list =new ArrayList<>();
        String sql="select * from orders where customer_id=?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,customer_id);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Orders o=mapResultSetToOrder(rs);
                list.add(o);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

}
