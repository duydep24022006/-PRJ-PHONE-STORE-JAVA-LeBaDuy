package dao;

import model.Orders;
import util.DBConnection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    private Connection conn ;
    public OrderDAO() {
        try {
            conn = DBConnection.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();
        String sql = "select o.id as order_id, o.customer_id, c.name as customer_name, c.email as customer_email, " +
                "o.status, o.created_at, p.name as product_name, od.quantity, od.price " +
                "from orders o " +
                "join customer c on o.customer_id = c.id " +
                "join order_details od on o.id = od.order_id " +
                "join product p on od.product_id = p.id";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Orders order = new Orders();
                order.setId(rs.getInt("order_id"));
                order.setCustomer_id(rs.getInt("customer_id"));
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerEmail(rs.getString("customer_email"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setProductName(rs.getString("product_name"));
                order.setQuantity(rs.getInt("quantity"));
                order.setPrice(rs.getDouble("price"));
                list.add(order);
            }
        } catch (SQLException e) {
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
    public int createOrder(int customer_id, int productId, int quantity) {
        String insertOrderSQL = "insert into orders(customer_id, status) values(?,'pending')";
        String insertDetailSQL =  "insert into order_details(order_id, product_id, quantity, price) values (?,?,?,?)";
        String updateStockSQL =  "update product set stock = stock - ? where id = ?";
        String productSQL = "select price, flash_sale_price, flash_sale_quantity, flash_sale_expiry from product where id=?";
        int orderId = -1;

        try {
            conn.setAutoCommit(false);

            // 1. Tạo order
            try (PreparedStatement ps = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customer_id);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

            // 2. Lấy giá sản phẩm (kiểm tra flash sale)
            double price = 0;
            boolean flashSaleUsed = false;
            try (PreparedStatement ps = conn.prepareStatement(productSQL)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double normalPrice = rs.getDouble("price");
                    double flashPrice = rs.getDouble("flash_sale_price");
                    int flashQty = rs.getInt("flash_sale_quantity");
                    Timestamp expiry = rs.getTimestamp("flash_sale_expiry");

                    if (flashPrice > 0 && flashQty >= quantity && expiry != null && expiry.after(new Timestamp(System.currentTimeMillis()))) {
                        price = flashPrice;
                        flashSaleUsed = true;
                    } else {
                        price = normalPrice;
                    }
                }
            }

            // 3. Thêm order_details
            try (PreparedStatement ps = conn.prepareStatement(insertDetailSQL)) {
                ps.setInt(1, orderId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                ps.setDouble(4, price);
                ps.executeUpdate();
            }

            // 4. Giảm stock hoặc flash_sale_quantity
            if (flashSaleUsed) {
                String updateFlashSaleSQL = "update product set flash_sale_quantity = flash_sale_quantity - ? where id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateFlashSaleSQL)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, productId);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(updateStockSQL)) {
                    ps.setInt(1, quantity);
                    ps.setInt(2, productId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }

        return orderId;
    }


    public void cancelOrder(int orderId) {
        String sqlDetails = "select product_id, quantity from order_details where order_id = ?";
        String sqlUpdateStock = "update product set stock = stock + ? where id = ?";
        String sqlUpdateOrder ="update orders set status = 'cancelled' where id = ?";

        try {
            conn.setAutoCommit(false);

            // Lấy chi tiết đơn hàng
            try (PreparedStatement ps = conn.prepareStatement(sqlDetails)) {
                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    // Cộng lại stock
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock)) {
                        psUpdate.setInt(1, quantity);
                        psUpdate.setInt(2, productId);
                        psUpdate.executeUpdate();
                    }
                }
            }

            // Cập nhật trạng thái đơn
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateOrder)) {
                ps.setInt(1, orderId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Don hang da duoc huy va stock da duoc cap nhat!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public List<Orders> getOrdersByCustomer(int customerId) {
        List<Orders> list = new ArrayList<>();
        String sql = "select o.id as order_id, " +
                        "c.name as customer_name, c.email as customer_email, " +
                        "p.name as product_name, od.quantity, od.price, " +
                        "o.status, o.created_at, cp.code as coupon_code, cp.discount_percent, o.final_total " +
                        "from orders o " +
                        "join customer c on o.customer_id = c.id " +
                        "join order_details od on o.id = od.order_id " +
                        "join product p on od.product_id = p.id " +
                        "left join order_coupon oc on o.id = oc.order_id " +
                        "left join coupon cp on oc.coupon_id = cp.id " +
                        "where o.customer_id = ?";


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orders o = new Orders();
                o.setId(rs.getInt("order_id"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setCustomerEmail(rs.getString("customer_email"));
                o.setProductName(rs.getString("product_name"));
                o.setQuantity(rs.getInt("quantity"));
                o.setPrice(rs.getDouble("price"));
                o.setStatus(rs.getString("status"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                o.setCouponCode(rs.getString("coupon_code"));
                o.setDiscountPercent(rs.getInt("discount_percent"));
                o.setFinalTotal(rs.getDouble("final_total"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private double getOrderTotal(int orderId) {
        String sql = "select sum(quantity * price) as total from order_details where order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public double applyCoupon(int orderId, String code) {
        CouponDAO couponDAO = new CouponDAO();

        if (!couponDAO.isValidCoupon(code)) {
            System.out.println("Ma giam gia khong hop le!");
            return 0;
        }

        double total = getOrderTotal(orderId);
        int discountPercent = couponDAO.getDiscountPercent(code);
        double discount = total * discountPercent / 100.0;
        double finalAmount = total - discount;

        try {
            // 1. Lưu coupon vào order_coupon
            String sql = "insert into order_coupon (order_id, coupon_id) " +
                    "select ?, id from coupon where code = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orderId);
                ps.setString(2, code);
                ps.executeUpdate();
            }

            // 2. Update final_total vào orders
            String updateOrder ="update orders set final_total = ? where id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateOrder)) {
                ps.setDouble(1, finalAmount);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }

            // 3. Tăng số lần sử dụng coupon
            couponDAO.incrementUsage(code);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("ap dung ma " + code + " giam " + discountPercent + "% thanh cong!");
        return finalAmount;
    }
    public void reportTop5ProductsThisMonth() {
        String sql = "select p.name as product_name, sum(od.quantity) as total_sold " +
                    "from order_details od " +
                    "join product p on od.product_id = p.id " +
                    "join orders o on od.order_id = o.id " +
                    "where month(o.created_at) = month(curdate()) " +
                    "and year(o.created_at) = year(curdate()) " +
                    "group by p.id, p.name " +
                    "order by total_sold desc limit 5";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("=== Top 5 san pham ban chay nhat thang ===");
            System.out.printf("%-25s %-15s\n", "San pham", "So luong ban");
            System.out.println("------------------------------------------");
            while (rs.next()) {
                String name = rs.getString("product_name");
                int totalSold = rs.getInt("total_sold");
                System.out.printf("%-25s %-15d\n", name, totalSold);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int createOrder(int customerId) throws SQLException {
        String insertOrderSQL = "insert into orders(customer_id, status) values(?,'pending')";
        int orderId = -1;
        try (PreparedStatement ps = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
        }
        return orderId;
    }
    public void addOrderItem(int orderId, int productId, int quantity, double price) throws SQLException {
        String insertDetailSQL =  "insert into order_details(order_id, product_id, quantity, price) values (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(insertDetailSQL)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        }

        // Giảm stock
        String updateStockSQL = "update product set stock = stock - ? where id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateStockSQL)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }

    }
    public List<Map<String,Object>> getOrderItems(int orderId) throws SQLException {
        List<Map<String,Object>> items = new ArrayList<>();
        String sql = "SELECT p.name, od.quantity, od.price " +
                "FROM order_details od JOIN product p ON od.product_id = p.id " +
                "WHERE od.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String,Object> item = new HashMap<>();
                item.put("productName", rs.getString("name"));
                item.put("quantity", rs.getInt("quantity"));
                item.put("price", rs.getDouble("price"));
                items.add(item);
            }
        }
        return items;
    }

}
