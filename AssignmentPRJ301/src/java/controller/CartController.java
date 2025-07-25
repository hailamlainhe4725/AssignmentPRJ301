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
import model.CartDAO;
import model.CartDTO;
import model.CartItemDTO;
import model.OrderDAO;
import model.ProductDAO;
import model.ProductDTO;
import model.UserDTO;
import utils.AuthUtils;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    CartDAO cartDAO = new CartDAO();

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
        String url = "";
        try {
            String action = request.getParameter("action");
            if ("addCart".equals(action)) {
                url = handleAddCart(request, response);
            } else if ("updateQuantity".equals(action)) {
                url = handleUpdateCartQuantity(request, response);
            } else if ("updateNote".equals(action)) {
                url = handleUpdateNote(request, response);
            } else if ("checkout".equals(action)) {
                url = handleCheckout(request, response);
            }
        } catch (Exception e) {
            e.getMessage();
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

    private String handleAddCart(HttpServletRequest request, HttpServletResponse response) {
        String checkError = "";
        String message = "";
        if (!AuthUtils.isLoggedIn(request)) {
            request.setAttribute("message", "Please log in to use the cart.");
            return "login.jsp";
        }
        UserDTO user = AuthUtils.getCurrentUser(request);

        String productIdRaw = request.getParameter("productId");
        String quantityRaw = request.getParameter("quantity");
        String note = request.getParameter("note");

        int productId = -1;
        int quantity = 1;
        // ===== productId =====
        if (productIdRaw == null || productIdRaw.trim().isEmpty()) {
            checkError += "Product ID is missing.<br/>";
        } else {
            try {
                productId = Integer.parseInt(productIdRaw);
                if (productId <= 0) {
                    checkError += "Invalid product selected.<br/>";
                }
            } catch (NumberFormatException e) {
                checkError += "Product ID must be a valid number.<br/>";
            }
        }
        // ===== quantity =====
        if (quantityRaw != null && !quantityRaw.trim().isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityRaw);
                if (quantity < 1 || quantity > 10) {
                    checkError += "Quantity must be between 1 and 10.<br/>";
                }
            } catch (NumberFormatException e) {
                checkError += "Quantity must be a valid number.<br/>";
            }
        }
        if (!checkError.isEmpty()) {
            request.setAttribute("checkError", checkError);
            return "menu.jsp";
        }
        // ===== add to cart =====
        try {
            ProductDAO pdao = new ProductDAO();
            ProductDTO product = pdao.getProductById(productId);

            if (product == null) {
                request.setAttribute("errorMessage", "Product not found.");
                return "menu.jsp";
            }
            
            int cartId = cartDAO.getOrCreateCartId(user.getUserId());

            CartItemDTO item = new CartItemDTO(productId, product.getProductName(), product.getPrice(), quantity, note);
            cartDAO.addOrUpdateItem(cartId, item);

            // Cập nhật lại giỏ hàng trong session
            CartDTO cart = new CartDTO(cartId, user.getUserId());
            cart.setItems(cartDAO.getCartItems(cartId));
            request.getSession().setAttribute("cart", cart);

            message = "Added to cart successfully!";
            request.setAttribute("message", message);
            return "menu.jsp";

        } catch (Exception e) {
            e.printStackTrace();
            checkError += "Unexpected error occurred while adding to cart.<br/>";
            request.setAttribute("checkError", checkError);
            return "menu.jsp";
        }
    }

    private String handleUpdateCartQuantity(HttpServletRequest request, HttpServletResponse response) {
        if (!AuthUtils.isLoggedIn(request)) {
            request.setAttribute("message", "Please log in to use the cart.");
            return "login.jsp";
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int change = Integer.parseInt(request.getParameter("change"));

            UserDTO user = AuthUtils.getCurrentUser(request);

            int cartId = cartDAO.getOrCreateCartId(user.getUserId());

            cartDAO.updateItemQuantity(cartId, productId, change);

            // Cập nhật lại cart trong session
            CartDTO cart = new CartDTO(cartId, user.getUserId());
            cart.setItems(cartDAO.getCartItems(cartId));
            request.getSession().setAttribute("cart", cart);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("checkError", "Failed to update quantity.");
        }

        return "cart.jsp";
    }

    private String handleUpdateNote(HttpServletRequest request, HttpServletResponse response) {
        if (!AuthUtils.isLoggedIn(request)) {
            request.setAttribute("checkError", "Please log in to use this function.");
            return "login.jsp";
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String note = request.getParameter("note");
            UserDTO user = AuthUtils.getCurrentUser(request);

            int cartId = cartDAO.getOrCreateCartId(user.getUserId());

            cartDAO.updateNote(cartId, productId, note);

            // Cập nhật lại giỏ trong session
            CartDTO cart = new CartDTO(cartId, user.getUserId());
            cart.setItems(cartDAO.getCartItems(cartId));
            request.getSession().setAttribute("cart", cart);

            request.setAttribute("message", "Note updated.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("checkError", "Failed to update note.");
        }

        return "cart.jsp";
    }

    private String handleCheckout(HttpServletRequest request, HttpServletResponse response) {
        if (!AuthUtils.isLoggedIn(request)) {
        request.setAttribute("checkError", "Please log in to checkout.");
        return "login.jsp";
    }

    try {
        UserDTO user = AuthUtils.getCurrentUser(request);
        CartDTO cart = (CartDTO) request.getSession().getAttribute("cart");
        
        if (cart == null || cart.getItems().isEmpty()) {
            return "cart.jsp";
        }

        String shippingAddress = request.getParameter("shippingAddress");
        double totalAmount = cart.getTotalAmount();
        if(user.getWallet() >= cart.getTotalAmount()){
        //   Tạo đơn hàng
        OrderDAO orderDAO = new OrderDAO();
        int orderId = orderDAO.createOrder(user.getUserId(), cart.getItems(), totalAmount, shippingAddress);

        //   Xoá giỏ hàng
        cartDAO.clearCart(cart.getCartId());
        request.getSession().removeAttribute("cart");
        request.setAttribute("message", "Order placed successfully. Order ID: #" + orderId);
        return "cart.jsp"; 
        }else{
            request.setAttribute("checkError", "Your wallet is not enough to pay,please check it.");
            return "payment.jsp";
        }  
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("checkError", "Checkout failed. Please try again.");
        return "cart.jsp";
    }
}




}
