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
import jakarta.servlet.http.HttpSession;
import model.CartDAO;
import model.CartDTO;
import model.UserDAO;
import model.UserDTO;
import utils.PasswordUtils;
import static utils.PasswordUtils.encryptSHA256;

/**
 *
 * @author Admin
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {
    
    UserDAO udao = new UserDAO();
    
    private static final String WELCOME_PAGE = "welcome.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String REGISTER_PAGE = "register.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
         String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if ("login".equals(action)) {
                url = handleLogin(request, response);
            } else if ("logout".equals(action)) {
                url = handleLogout(request, response);
            } else if ("register".equals(action)) {
                url = handleRegister(request, response);
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

    private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String url = LOGIN_PAGE;
        HttpSession session = request.getSession();
        String userName = request.getParameter("strUserName");
        String password = request.getParameter("strPassword");
        password = PasswordUtils.encryptSHA256(password);
        UserDAO userDAO = new UserDAO();
        if (userDAO.login(userName, password)) {
            // Dang nhap thanh cong
            url = "welcome.jsp";
            UserDTO user = userDAO.getUserByUserName(userName);
            session.setAttribute("user", user);
            //Load lại Cart từ DB
            CartDAO cartDAO = new CartDAO();
            int cartId = cartDAO.getOrCreateCartId(user.getUserId());
            CartDTO cart = new CartDTO(cartId, user.getUserId());
            cart.setItems(cartDAO.getCartItems(cartId));
            session.setAttribute("cart", cart);
        
        } else {
            // Dang nhap that bai
            url = "login.jsp";
            request.setAttribute("message", "User Name or Password incorrect!");
        }
        return url;
    }

     private String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if(session!=null){
            UserDTO user = (UserDTO) session.getAttribute("user");
            if(user!=null){
                session.invalidate();
            }
        }
        return LOGIN_PAGE;
    }

   private String handleRegister(HttpServletRequest request, HttpServletResponse response) {
    String checkError = "";
    String message = "";

    String userName = request.getParameter("userName");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String phone = request.getParameter("phone");
    String address = request.getParameter("address");
    String role = "CUSTOMER"; 

    //Nếu userName rỗng (chưa nhập gì), thì chỉ trả về form, không xử lý
    if (userName == null && fullName == null && email == null && password == null) {
        return "register.jsp";
    }
    
    // Username
    if (userName == null || userName.isEmpty()) {
        checkError += "Username is required.<br/>";
    } else if (userName.length() > 50) {
        checkError += "Username cannot exceed 50 characters.<br/>";
    } else if (udao.getUserByUserName(userName) != null) {
        checkError += "Username already exists.<br/>";
    }

    // Full Name
    if (fullName == null || fullName.isEmpty()) {
        checkError += "Full Name is required.<br/>";
    } else if (fullName.length() > 100) {
        checkError += "Full Name cannot exceed 100 characters.<br/>";
    }

    // Email
    if (email == null || email.isEmpty()) {
        checkError += "Email is required.<br/>";
    } else if (email.length() > 100) {
        checkError += "Email cannot exceed 100 characters.<br/>";
    } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
        checkError += "Invalid email format.<br/>";
    } else if (udao.isEmailExists(email)) {
        checkError += "Email already exists.<br/>";
    }

    // Password
    if (password == null || password.trim().isEmpty()) {
        checkError += "Password is required.<br/>";
    } else if (password.length() < 6) {
        checkError += "Password must be at least 6 characters.<br/>";
    }

    // phone và address (cho phép null)

    UserDTO user = new UserDTO(0, userName, fullName, email, password, phone, address, role,0);

    if (checkError.isEmpty()) {
        
        if (udao.registerUser(user)) {
            message = "Register successful! You can now log in.";
            request.setAttribute("message", message);
            return "login.jsp";
        } else {
            checkError += "Failed to register user.<br/>";
        }
    }

    request.setAttribute("checkError", checkError);
    request.setAttribute("user", user);  

    return "register.jsp";
}


    
}