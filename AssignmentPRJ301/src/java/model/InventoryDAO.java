/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DbUtils;

/**
 *
 * @author admin
 */
public class InventoryDAO {

    public static List<InventoryDTO> getAllInventory() {
        List<InventoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InventoryDTO i = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("SELECT inventoryId,productId,quantityAvailable FROM tblInventory");
            rs = ps.executeQuery();

            while (rs.next()) {
                int inventoryId = rs.getInt("inventoryId");
                int productId = rs.getInt("productId");
                int quantityAvailable = rs.getInt("quantityAvailable");

                i = new InventoryDTO(inventoryId, productId, quantityAvailable);
                list.add(i);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn, ps, rs);
        }
        return list;
    }

    private static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            rs = null;
        }
        if (ps != null) {
            ps = null;
        }
        if (conn != null) {
            conn = null;
        }
    }

    public static boolean themSoLuong(InventoryDTO i, int nhapHang) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("UPDATE tblInventory SET quantityAvailable = quantityAvailable + ? WHERE inventoryId = ? AND productId = ?");
            ps.setInt(1, nhapHang);
            ps.setInt(2, i.getInventoryId());
            ps.setInt(3, i.getProductId());

          int row = ps.executeUpdate();
          success = (row>0)?true:false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn, ps, null);
        }
     return success;
    }

    public boolean decreaseQuantity(int productId, int quantity) {
    String sql = "UPDATE tblInventory SET quantityAvailable = quantityAvailable - ? " +
                 "WHERE productId = ? AND quantityAvailable >= ?";
    try {
        Connection conn = DbUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
    
        ps.setInt(1, quantity);
        ps.setInt(2, productId);
        ps.setInt(3, quantity);

        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean addIntoInve(int i, int i0, int productId, int i1) {
        Connection conn = null;
        PreparedStatement pr = null;
        ResultSet rs = null;
        boolean success = false;
         try {
            conn = DbUtils.getConnection();
            pr = conn.prepareStatement("INSERT INTO tblInventory (inventoryId,productId,quantityAvailable) VALUES (?,?,?) ");
            pr.setInt(1, i0);
            pr.setInt(2, productId);
            pr.setInt(3, i1);
             int count = pr.executeUpdate();
             
            success= (count>0);
        } catch (Exception e) {
             System.out.println(e.getMessage());
        } finally {
             closeResource(conn, pr, null);
        }
         return success;
    }

}
