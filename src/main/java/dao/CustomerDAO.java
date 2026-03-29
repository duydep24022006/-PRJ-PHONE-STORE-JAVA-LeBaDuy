package dao;

import entity.Customer;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class CustomerDAO {
    private Connection conn = DBConnection.getInstance().getConnection();
    public List<Customer> getAllCustomer(){
        List<Customer> list =new ArrayList<>();
        String sql = "select * from customer";
        try(Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql)){
         while (rs.next()){
             list.add(mapResultSetToCustome(rs));
         }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public Customer mapResultSetToCustome(ResultSet rs) throws SQLException{
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setPassword(rs.getString("password"));
        c.setRole(rs.getString("role"));
        return c;
    }
    public void register(Customer c){
        String sql = "insert into customer(name, email, phone, address, password, role) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.setString(5, BCrypt.hashpw(c.getPassword(), BCrypt.gensalt()));
            ps.setString(6, c.getRole());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Customer login(String email, String password){
        String sql="select * from customer where email=? and password=?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return mapResultSetToCustome(rs);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public void updateCustomer(Customer c) {
        String sql = "UPDATE customer SET name=?, phone=?, address=?, password=?, role=? WHERE email=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getPassword());
            ps.setString(5, c.getRole());
            ps.setString(6, c.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCustomer(String email){
        String sql = "delete from customer where email=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, email);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}
