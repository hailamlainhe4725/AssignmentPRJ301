/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.PaymentDAO;
import model.PaymentDTO;
import model.UserDAO;
import model.UserDTO;
import utils.AuthUtils;
import utils.EmailUtils;

/**
 *
 * @author admin
 */
@WebServlet(name = "WalletController", urlPatterns = {"/WalletController"})
public class WalletController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "welcome.jsp";
        String action = request.getParameter("action");

        try {
            if ("amtc".equals(action)) {
                url = handleAddMoneyToCustomers(request, response);
            } else if ("paymentHistory".equals(action)) {
                url = handlePHistory(request, response);
            }
        } catch (Exception e) {
        } finally {
            System.out.println("day la kiem tra url " + url);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
private String handleAddMoneyToCustomers(HttpServletRequest request, HttpServletResponse response) {
    String message = "";
    String url = "payment.jsp"; // default

    String phone = request.getParameter("phone");
    String moneyStr = request.getParameter("money");
    String ma = request.getParameter("ma");

    if (phone == null || phone.trim().isEmpty() || !phone.matches("\\d+")) {
        message +="Invalid Phone number!<br/>";
    }

    if (ma == null || ma.trim().isEmpty()) {
        message +="Transaction code is required!<br/>";
    }

    int money = 0;
    try {
        money = Integer.parseInt(moneyStr);
        if (money <= 0) throw new NumberFormatException();
    } catch (NumberFormatException e) {
        message +="Invalid money amount!<br/>";
    }


    UserDAO udao = new UserDAO();
    UserDTO user = udao.getUserByPhone(phone);
    if (user == null) {
        message += "User not found!";
    }

        if(!message.isEmpty()){
        request.setAttribute("message", message);
        return url;
    }
        
    PaymentDAO pdao = new PaymentDAO();
    if (pdao.addMoneyAndNote(phone, money, ma)) {
        udao.tangTien(money, phone);
        try {
            EmailUtils.sendTransactionEmail(user.getEmail(), user.getFullName(), user.getPhone(), money, ma);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Added successfully but failed to send email.";
        }
        message = "Add successfully for phone: " + phone;
        url = "cart.jsp";
    } else {
        message = "Error adding money!";
    }

    request.setAttribute("message", message);
    return url;
}


    private String handlePHistory(HttpServletRequest request, HttpServletResponse response) {
        if (AuthUtils.isLoggedIn(request)) {
            UserDTO user = AuthUtils.getCurrentUser(request);
            PaymentDAO pmdao = new PaymentDAO();
            List<PaymentDTO> list = pmdao.getByPhone(user.getPhone());
            HttpSession session = request.getSession();
            String action = request.getParameter("action");
            request.setAttribute("action", action);
            session.setAttribute("list", list);
            return "payment.jsp";
        }
        return "login.jsp";
    }

}
