/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Desktop;

/**
 *
 * @author Admin
 */
@WebServlet(name = "MainController", urlPatterns = {"","/MainController", "/mc"})
public class MainController extends HttpServlet {

    private static final String WELCOME = "welcome.jsp";

    private boolean isUserAction(String action) {
        return "login".equals(action)
                || "logout".equals(action)
                || "register".equals(action);
    }

    private boolean isProductAction(String action) {
        return "searchName".equals(action)||
                "toProduct".equals(action)||
                "addProduct".equals(action);
    }
    
    private boolean isCartAction(String action) {
        return "addCart".equals(action)
                || "updateQuantity".equals(action)
                || "updateNote".equals(action)
                || "checkout".equals(action)
                ;
    }

    private boolean isCateogryAction(String action) {
        return "openCategory".equals(action);
    }
    
    private boolean isInventoryAction(String action){
        return "store".equals(action)
                || "nhapHang".equals(action)
                ;
    }
    
    private boolean isOrderAction(String action) {
        return "updateOrderStatus".equals(action)
                || "viewOrders".equals(action);
    }

        private boolean isWalletAction(String action) {
            return "amtc".equals(action)||
                    "paymentHistory".equals(action);
        }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        try {
            String action = request.getParameter("action");
            System.out.println(action + "11");
            if (isUserAction(action)) {
                url = "/UserController";
            } else if (isProductAction(action)) {
                url = "/ProductController";
            }else if(isCartAction(action)){
                url = "/CartController";
            } else if (isCateogryAction(action)) {
                url = "/CategoryController";
            } else if(isInventoryAction(action)){
                url = "/InventoryController";
            }else if(isOrderAction(action)){
                url = "/OrderController";
            }else if(isWalletAction(action)){
                url = "/WalletController";
            }
        } catch (Exception e) {
            System.out.println("error in ProcessrRequest: " + e);
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



}
