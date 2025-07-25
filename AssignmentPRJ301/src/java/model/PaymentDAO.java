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
public class PaymentDAO {

    public static boolean addMoneyAndNote(String phone, int money, String ma) {
        UserDAO udao = new UserDAO();
        System.out.println(phone);
        UserDTO user = udao.getUserByPhone(phone);
        boolean success = false;
        System.out.println(user);
        if (user != null) {
            Connection conn = null;
            PreparedStatement pr = null;
            ResultSet rs = null;
            PaymentDTO p = new PaymentDTO(0, phone, money, ma);
            try {
                conn = DbUtils.getConnection();
                pr = conn.prepareStatement("INSERT INTO tblPayment (phone,money,ma) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);

                pr.setString(1, phone);
                pr.setInt(2, money);
                pr.setString(3, ma);
                System.out.println("1");
                int count = pr.executeUpdate();
                rs = pr.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    p.setSo(generatedId);
                    System.out.println("3");
                }
                System.out.println("2");
                success = (count > 0);
                System.out.println(success);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.out.println("4");
            } finally {
                closeResources(conn, pr, null);
            }

        }
        return success;
    }

    private static void closeResources(Connection conn, PreparedStatement prs, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prs != null) {
                prs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing resources:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<PaymentDTO> getByPhone(String phone) {
        List<PaymentDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pr = null;
        ResultSet rs = null;
        PaymentDTO pm = null;
        try {
            conn = DbUtils.getConnection();
            pr = conn.prepareStatement("SELECT so,money,ma FROM tblPayment WHERE phone = ?");
            pr.setString(1, phone);
            rs = pr.executeQuery();
            while(rs.next()){
                int so = rs.getInt("so");
                int money = rs.getInt("money");
                String ma = rs.getString("ma");
                
                pm = new PaymentDTO(so, phone, money, ma);
                list.add(pm);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            closeResources(conn, pr, rs);
        }
        return list;
    }
}
