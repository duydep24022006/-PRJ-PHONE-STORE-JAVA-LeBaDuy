package dao;

import entity.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * ProductDAO: lớp chịu trách nhiệm tương tác với bảng product trong DB.
 * Bao gồm các chức năng CRUD (Create, Read, Update, Delete) và Search.
 */
public class ProductDAO {
    private Connection conn = DBConnection.getInstance().getConnection();

    public List<Product> getAllProducts() {
        List<Product> list =new ArrayList<>();
        String sql="select * from product";
        try(Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sql)){
            while (rs.next()){
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm tiện ích: ánh xạ ResultSet -> Product object
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p=new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setCapacity(rs.getString("capacity"));
        p.setColor(rs.getString("color"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setDescription(rs.getString("description"));
        return p;
    }
    public  void addProduct(Product p){
        String sql= "insert into product(name,brand,capacity,color,price,stock,description) values(?,?,?,?,?,?,?)";
        try(PreparedStatement ps =conn.prepareStatement(sql)){
            ps.setString(1,p.getName());
            ps.setString(2,p.getBrand());
            ps.setString(3,p.getCapacity());
            ps.setString(4,p.getColor());
            ps.setDouble(5,p.getPrice());
            ps.setInt(6,p.getStock());
            ps.setString(7,p.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  void updateProduct(Product p){
        String sql ="update product set name=?,brand=?,capacity=?,color=?,price=?,stock=? where id=?";
        try (PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,p.getName());
            ps.setString(2,p.getBrand());
            ps.setString(3,p.getCapacity());
            ps.setString(4,p.getColor());
            ps.setDouble(5,p.getPrice());
            ps.setInt(6,p.getStock());
            ps.setString(7,p.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  void deleteProduct(Product p){
        String sql ="delete from product where id=?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1,p.getId());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * Tìm kiếm sản phẩm theo tên (LIKE %keyword%)
     */
    public  List<Product> searchProductByName(String search){
        List<Product>list =new ArrayList<>();
        String sql="select * from product where name like ?";
        try(PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1,"%"+search+"%");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(mapResultSetToProduct(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }



}
