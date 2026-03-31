package dao;

import model.Product;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * ProductDAO: lớp chịu trách nhiệm tương tác với bảng product trong DB.
 * Bao gồm các chức năng CRUD (Create, Read, Update, Delete) và Search.
 */
public class ProductDAO {
    private Connection conn;

    public ProductDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "select * from product";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
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
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setCapacity(rs.getString("capacity"));
        p.setColor(rs.getString("color"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setDescription(rs.getString("description"));
        double flashPrice = rs.getDouble("flash_sale_price");
        if (!rs.wasNull()) {
            p.setFlashSalePrice(flashPrice);
        }
        int flashQty = rs.getInt("flash_sale_quantity");
        if (!rs.wasNull()) {
            p.setFlashSaleQuantity(flashQty);
        }
        Timestamp expiry = rs.getTimestamp("flash_sale_expiry");
        if (expiry != null) {
            p.setFlashSaleExpiry(expiry.toLocalDateTime());
        }
        return p;
    }

    public void addProduct(Product p) {
        String sql = "insert into product(name,brand,capacity,color,price,stock,description) values(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getCapacity());
            ps.setString(4, p.getColor());
            ps.setDouble(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product p) {
        String sql = "update product set name=?,brand=?,capacity=?,color=?,price=?,stock=? where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getCapacity());
            ps.setString(4, p.getColor());
            ps.setDouble(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        String sql = "delete from product where id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // 23000 là SQLState cho lỗi ràng buộc
                System.out.println("San pham nay dang duoc su dung trong cac don hang hoac lien ket khac, nen khong the xoa.");
                System.out.println("Vui long xoa cac du lieu lien quan truoc khi xoa san pham.");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tìm kiếm sản phẩm theo tên (LIKE %keyword%)
     */
    public List<Product> searchProductByName(String search) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from product where name like ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + search + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Product getById(int id) {
        String sql = "select * from product where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setBrand(rs.getString("brand"));
                p.setCapacity(rs.getString("capacity"));
                p.setColor(rs.getString("color"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setDescription(rs.getString("description"));
                p.setCategoryId(rs.getInt("category_id"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> searchByBrand(String brand) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from product where brand like ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + brand + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> searchByPriceRange(double min, double max) {
        List<Product> list = new ArrayList<>();
        String sql = "select * from product where price between ? and ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isFlashSaleActive(int productId){
        String sql= "SELECT flash_sale_quantity, flash_sale_expiry FROM product WHERE id = ?";

        try (PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setInt(1, productId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int qty=rs.getInt("flash_sale_quantity");
                Timestamp expiry =rs.getTimestamp("flash_sale_expiry");
                return qty>0 && expiry != null &&  expiry.after(new Timestamp(System.currentTimeMillis()));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void createFlashSale(int productId , double flashPrice,int flashQuantity, LocalDateTime expiry) {
        String sql = "UPDATE product SET flash_sale_price = ?, flash_sale_quantity = ?, flash_sale_expiry = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, flashPrice);
            ps.setInt(2, flashQuantity);
            ps.setTimestamp(3, Timestamp.valueOf(expiry));
            ps.setInt(4, productId);
            ps.executeUpdate();
            System.out.println("Flash sale da duoc tao cho san pham ID: " + productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeFlashSale(int productId) {
        String sql = "UPDATE product SET flash_sale_price = NULL, flash_sale_quantity = NULL, flash_sale_expiry = NULL WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
            System.out.println("Flash sale da duoc xoa cho san pham ID: " + productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}