/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.DbUtils;

/**
 *
 * @author Admin
 */
public class OrderDAO {

    private static final String INSERT_ORDER = "INSERT INTO tblOrder (userId, totalAmount, orderDate, status, shippingAddress) "
            +          "OUTPUT INSERTED.orderId VALUES (?, ?, GETDATE(), 'PENDING', ?)";
    
    private static final String INSERT_ITEM = "INSERT INTO tblOrderItems (orderId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
         
    
    public int createOrder(int userId, List<CartItemDTO> items, double totalAmount, String shippingAddress) {
    int orderId = -1;


    try (Connection conn = DbUtils.getConnection()) {
        conn.setAutoCommit(false);

        try (PreparedStatement psOrder = conn.prepareStatement(INSERT_ORDER)) {
            psOrder.setInt(1, userId);
            psOrder.setDouble(2, totalAmount);
            psOrder.setString(3, shippingAddress);

            ResultSet rs = psOrder.executeQuery();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            rs.close();
        }
        try (PreparedStatement psItem = conn.prepareStatement(INSERT_ITEM)) {
            for (CartItemDTO item : items) {
                psItem.setInt(1, orderId);
                psItem.setInt(2, item.getProductId());
                psItem.setInt(3, item.getQuantity());
                psItem.setDouble(4, item.getUnitPrice());
                psItem.addBatch();
            }
            psItem.executeBatch();
        }

        conn.commit();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return orderId;
}

    public boolean updateOrderStatus(int orderId, String newStatus) {
    try (
        Connection conn = DbUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE tblOrder SET status = ? WHERE orderId = ?")
    ) {
        ps.setString(1, newStatus);
        ps.setInt(2, orderId);
        int rows = ps.executeUpdate();

        if (rows > 0 && "CONFIRMED".equalsIgnoreCase(newStatus)) {
            List<OrderItemDTO> items = getOrderItems(orderId);   
            InventoryDAO inventoryDAO = new InventoryDAO();

            for (OrderItemDTO item : items) {
                inventoryDAO.decreaseQuantity(item.getProductId(), item.getQuantity());
            }
        }

        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public List<OrderItemDTO> getOrderItems(int orderId) {
    List<OrderItemDTO> list = new ArrayList<>();
    String sql = "SELECT orderItemId, productId, quantity, unitPrice FROM tblOrderItems WHERE orderId = ?";

    try (
        Connection conn = DbUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new OrderItemDTO(
                rs.getInt("orderItemId"),
                orderId,
                rs.getInt("productId"),
                rs.getInt("quantity"),
                rs.getDouble("unitPrice")
            ));
        }

        rs.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public List<OrderDTO> getAllOrders() {
    List<OrderDTO> list = new ArrayList<>();
    String sql = "SELECT orderId, userId, totalAmount, orderDate, status, shippingAddress FROM tblOrder";
        System.out.println("trctry");
    try (
        Connection conn = DbUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
    ) {
        while (rs.next()) {
            int orderId = rs.getInt("orderId");
            int userId = rs.getInt("userId");
            double totalAmount = rs.getDouble("totalAmount");
            Date orderDate = rs.getTimestamp("orderDate");
            String status = rs.getString("status");
            String shippingAddress = rs.getString("shippingAddress");

            OrderDTO order = new OrderDTO(orderId, userId, totalAmount, orderDate, status, shippingAddress);
            list.add(order);
        }

        System.out.println("DEBUG DAO: getAllOrders() - size = " + list.size()); // debug dòng này
    } catch (Exception e) {
        System.err.println("Error in getAllOrders: " + e.getMessage());
        e.printStackTrace();
    }
System.out.println("sautry");
    return list;
}





    
}
