/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import utils.DbUtils;

/**
 *
 * @author admin
 */
public class ProductDAO {
    
    private static final String GET_PRODUCT_BY_ID = "SELECT productId, productName, description, price, imageUrl, available, categoryId FROM tblProducts WHERE productId = ? ";
    
    public static boolean isExist(String productName){
        return getProductByName(productName)!=null?true:false;
    }
    
    public static List<ProductDTO> getProductByName(String productName){
        List<ProductDTO> list = new ArrayList<>();
        ProductDTO p = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("SELECT productId,description , price,imageUrl,available,categoryId FROM tblProducts WHERE productName LIKE ? ");
            ps.setString(1, "%"+productName +"%");
            rs = ps.executeQuery();
            
            while(rs.next()){
                int productId = rs.getInt("productId");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String imageUrl = rs.getString("imageUrl");
                boolean available = rs.getBoolean("available");
                int categoryId = rs.getInt("categoryId");
                
                p = new ProductDTO(productId, productName, description, price, imageUrl, available, categoryId);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn,ps,rs);
        }
        return list;
    }
    

    public static boolean addProduct(ProductDTO p){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.out.println(p);
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("INSERT INTO tblProducts (productName, description, price, imageUrl, available, categoryId) SELECT ?, ?, ?, ?, ?, ? WHERE EXISTS (SELECT 1 FROM tblCategory WHERE categoryId = ?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getImageUrl());
            ps.setBoolean(5, p.getAvailable());
            ps.setInt(6, p.getCategoryId());
            ps.setInt(7, p.getCategoryId());
            
             int count = ps.executeUpdate();
             rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                p.setProductId(generatedId);
            }
            success= (count>0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn, ps, rs);
        }
        return success;
    }

    private static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        if(rs!=null){
            rs = null;
        }
        if(ps!= null){
            ps = null;
        }
        if(conn!=null){
            conn = null;
        }
    }

    public List<ProductDTO> getProductByCategoryId(int categoryId) {
        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductDTO p = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("SELECT p.productId, p.productName,  p.description, p.price, p.imageUrl, p.available FROM  tblProducts p INNER JOIN  tblInventory i ON p.productId = i.productId WHERE  p.categoryId = ? AND i.quantityAvailable > 0");
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            
            while(rs.next()){
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String imageUrl = rs.getString("imageUrl");
                boolean available = rs.getBoolean("available");
                p = new ProductDTO(productId, productName, description, price, imageUrl, available, categoryId);
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn, ps, rs);
        }
        return list;
    }

    public ProductDTO getProductById(int productId) {
        ProductDTO p = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_PRODUCT_BY_ID);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("productName");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                String imageUrl = rs.getString("imageUrl");
                boolean available = rs.getBoolean("available");
                int categoryId = rs.getInt("categoryId");

                p = new ProductDTO(productId, productName, description, price, imageUrl, available, categoryId);
            }
        } catch (Exception e) {
            System.out.println("Error in getProductById: " + e.getMessage());
        } finally {
            closeResource(conn, ps, rs);
        }

        return p;
    }

}
