/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DbUtils;
/**
 *
 * @author Admin
 */
public class CartDAO {
    
    
    

    private static final String FIND_CART_BY_USER = "SELECT cartId FROM tblCarts WHERE userId = ?";
    private static final String CREATE_CART = "INSERT INTO tblCarts (userId) OUTPUT INSERTED.cartId VALUES (?)";
    private static final String FIND_CART_ITEMS
            = "SELECT ci.productId, p.productName, p.price, ci.quantity, ci.note FROM tblCartItems ci JOIN tblProducts p ON ci.productId = p.productId WHERE ci.cartId = ?";

    private static final String CHECK_ITEM_EXIST = "SELECT quantity FROM tblCartItems WHERE cartId = ? AND productId = ?";
    private static final String INSERT_ITEM = "INSERT INTO tblCartItems (cartId, productId, quantity, note) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ITEM = "UPDATE tblCartItems SET quantity = ?, note = ? WHERE cartId = ? AND productId = ?";
    private static final String DELETE_ITEM = "DELETE FROM tblCartItems WHERE cartId = ? AND productId = ?";
    private static final String CLEAR_CART = "DELETE FROM tblCartItems WHERE cartId = ?";
    private static final String UPDATE_QUANTITY = "UPDATE tblCartItems SET quantity = quantity + ? WHERE cartId = ? AND productId = ?";
    private static final String UPDATE_NOTE = "UPDATE tblCartItems SET note = ? WHERE cartId = ? AND productId = ?";
   
    
    public int getOrCreateCartId(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cartId = -1;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(FIND_CART_BY_USER);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                cartId = rs.getInt("cartId");
            } else {
                closeResources(ps, rs);
                ps = conn.prepareStatement(CREATE_CART);
                ps.setInt(1, userId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    cartId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return cartId;
    }

    public List<CartItemDTO> getCartItems(int cartId) {
    List<CartItemDTO> items = new ArrayList<>();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        conn = DbUtils.getConnection();
        ps = conn.prepareStatement(FIND_CART_ITEMS);
        ps.setInt(1, cartId);
        rs = ps.executeQuery();

        while (rs.next()) {
            int productId = rs.getInt("productId");
            String name = rs.getString("productName");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");
            String note = rs.getString("note");  

            CartItemDTO item = new CartItemDTO(productId, name, price, quantity, note);  
            items.add(item);
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeResources(conn, ps, rs);
    }

    return items;
}


    public void addOrUpdateItem(int cartId, CartItemDTO item) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_ITEM_EXIST);
            ps.setInt(1, cartId);
            ps.setInt(2, item.getProductId());
            rs = ps.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("quantity");
                closeResources(ps, rs);
                ps = conn.prepareStatement(UPDATE_ITEM);
                ps.setInt(1, currentQty + item.getQuantity());
                ps.setString(2, item.getNote());
                ps.setInt(3, cartId);
                ps.setInt(4, item.getProductId());
                ps.executeUpdate();
            } else {
                closeResources(ps, rs);
                ps = conn.prepareStatement(INSERT_ITEM);
                ps.setInt(1, cartId);
                ps.setInt(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setString(4, item.getNote());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
    }


    public void removeItem(int cartId, int productId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ITEM)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCart(int cartId) {
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(CLEAR_CART)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResources(PreparedStatement ps, ResultSet rs) {
        closeResources(null, ps, rs);
    }

   


    
    public void updateItemQuantity(int cartId, int productId, int change) {
    try (
        Connection conn = DbUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT quantity FROM tblCartItems WHERE cartId = ? AND productId = ?");
    ) {
        ps.setInt(1, cartId);
        ps.setInt(2, productId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int currentQty = rs.getInt("quantity");
            int newQty = currentQty + change;

            if (newQty <= 0) {
                try (PreparedStatement deletePs = conn.prepareStatement(DELETE_ITEM)) {
                    deletePs.setInt(1, cartId);
                    deletePs.setInt(2, productId);
                    deletePs.executeUpdate();
                }
            } else {
                try (PreparedStatement updatePs = conn.prepareStatement(UPDATE_QUANTITY)) {
                    updatePs.setInt(1, change);
                    updatePs.setInt(2, cartId);
                    updatePs.setInt(3, productId);
                    updatePs.executeUpdate();
                }
            }
        }

        rs.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void updateNote(int cartId, int productId, String note) {
        try (
                Connection conn = DbUtils.getConnection();  
                PreparedStatement ps = conn.prepareStatement(UPDATE_NOTE);) {
            ps.setString(1, note);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
