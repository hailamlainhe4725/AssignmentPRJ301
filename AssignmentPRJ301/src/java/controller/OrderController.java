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
import java.util.List;
import model.OrderDAO;
import model.OrderDTO;
import utils.AuthUtils;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

     
     protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
         String url = "orders.jsp";
        try {
            String action = request.getParameter("action");
            if ("updateOrderStatus".equals(action)) {
                url = handleUpdateOrderStatus(request, response);
            } else if ("viewOrders".equals(action)) {
                url = handleViewOrders(request,response);
            }
        } catch (Exception e) {
        } finally {
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

    private String handleUpdateOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        if(AuthUtils.isAdmin(request)){
        try {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("newStatus");

        OrderDAO dao = new OrderDAO();
        boolean updated = dao.updateOrderStatus(orderId, newStatus);

        if (updated) {
            request.setAttribute("message", "Order status updated successfully.");
        } else {
            request.setAttribute("checkError", "Failed to update order status.");
        }

        List<OrderDTO> orders = dao.getAllOrders();
        request.setAttribute("orders", orders);

    } catch (Exception e) {
        request.setAttribute("checkError", "Error: " + e.getMessage());
    }

    url = "orders.jsp";   
    }else{
            url = "login.jsp";
        }
        return url;
}

    private String handleViewOrders(HttpServletRequest request, HttpServletResponse response) {
List<OrderDTO> orders = new OrderDAO().getAllOrders();
                request.setAttribute("orders", orders);
                return "orders.jsp";
    }

}
