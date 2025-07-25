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
public class CategoryDAO {
    public static List<CategoryDTO> getAllCategory(){
        List<CategoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CategoryDTO c = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement("SELECT categoryId,categoryName,description,image FROM tblCategory ");
            rs = ps.executeQuery();
            
            while(rs.next()){
                int categoryId = rs.getInt("categoryId");
                String categoryName = rs.getString("categoryName");
                String description = rs.getString("description");
                String image = rs.getString("image");
                c = new CategoryDTO(categoryId, categoryName, description,image);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResource(conn,ps,rs);
        }
        
        return list;
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
}
