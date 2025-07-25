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
import utils.PasswordUtils;
import static utils.PasswordUtils.encryptSHA256;

/**
 *
 * @author Admin
 */
public class UserDAO {

    private static final String FIND_USER_BY_NAME = "SELECT userId, userName, email, password, phone, address, role, fullName,wallet FROM tblUsers WHERE userName = ?";
        private static final String FIND_USER_BY_Phone = "SELECT userId, userName, email, password, phone, address, role, fullName,wallet FROM tblUsers WHERE phone = ?";
    private static final String GET_ALL_USER = "SELECT userId, userName, email, password, phone, address, role, fullName,wallet FROM tblUsers ORDER BY userName";
    private static final String ADD_USER = "INSERT INTO tblUsers (userName, fullName, email, password, phone, address, role,wallet) VALUES (?, ?, ?, ?, ?, ?, ?,0)";
    private static final String CHECK_EMAIL = "SELECT userId FROM tblUsers WHERE email = ?";

    public boolean login(String userName, String password) {
        UserDTO user = getUserByUserName(userName);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public UserDTO getUserByUserName(String uName) {
        UserDTO user = null;

        try {
                 Connection conn = DbUtils.getConnection();  PreparedStatement pr = conn.prepareStatement(FIND_USER_BY_NAME);
            pr.setString(1, uName);
            ResultSet rs = pr.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String role = rs.getString("role");
                int wallet = rs.getInt("wallet");

                user = new UserDTO(userId, userName, fullName, email, password, phone, address, role, wallet);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

        public UserDTO getUserByPhone(String phone) {
        UserDTO user = null;

        try {
                 Connection conn = DbUtils.getConnection();
                PreparedStatement pr = conn.prepareStatement(FIND_USER_BY_Phone) ;
            pr.setString(1, phone);
            ResultSet rs = pr.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String address = rs.getString("address");
                String role = rs.getString("role");
                int wallet = rs.getInt("wallet");

                user = new UserDTO(userId, userName, fullName, email, password, phone, address, role, wallet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
        
    public static boolean tangTien(int money,String phone){
        boolean success =false;
        Connection conn = null;
        PreparedStatement pr = null;
        ResultSet rs= null;
        try {
            conn = DbUtils.getConnection();
            pr = conn.prepareStatement("UPDATE tblUsers SET wallet = wallet + ? WHERE phone = ? ");
            pr.setInt(1, money);
            pr.setString(2, phone);
            
            int count = pr.executeUpdate();
            success = (count>0)?true:false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResources(conn, pr, rs);
        }
        return success;
    }
    public static List<UserDTO> getAllUsers() {
        List<UserDTO> userList = new ArrayList<>();

        try (
                 Connection conn = DbUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(GET_ALL_USER);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String role = rs.getString("role");
                int wallet = rs.getInt("wallet");
                UserDTO user = new UserDTO(userId, userName, fullName, email, password, phone, address, role, wallet);
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public static boolean registerUser(UserDTO user) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(ADD_USER);

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            user.setPassword(PasswordUtils.encryptSHA256(user.getPassword()));
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());

            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in registerUser(UserDTO): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }

        return success;
    }

    public static boolean isEmailExists(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_EMAIL);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next(); // Nếu tìm thấy email => true
        } catch (Exception e) {
            System.err.println("Error in isEmailExists: " + e.getMessage());
        } finally {
            closeResources(conn, ps, rs);
        }

        return false;
    }

    private static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

     public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE tblUsers SET password = ? WHERE userId = ?";
        try {
            Connection conn = DbUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
