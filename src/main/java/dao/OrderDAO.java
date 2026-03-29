package dao;

import entity.Orders;
import util.DBConnection;

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
}
