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
import java.util.ArrayList;
import java.util.List;
import model.InventoryDAO;
import model.ProductDAO;
import model.ProductDTO;

/**
 *
 * @author admin
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String url = "";
        try {
            if("searchName".equals(action)){
                url = handleSearch(request,response);
            }else if("addProduct".equals(action)){
                url = handleAddProduct(request,response);
            }else if("toProduct".equals(action)){
                url = handleToProduct(request,response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    private String handleSearch(HttpServletRequest request, HttpServletResponse response) {
        List<ProductDTO> list = new ArrayList<>();
        ProductDTO p = null;
        ProductDAO pdao = new ProductDAO();
        String productName = request.getParameter("productName");
        if(pdao.isExist(productName)){
            list = pdao.getProductByName(productName);
        }
        request.setAttribute("productName", productName);
        System.out.println(list);
        HttpSession session = request.getSession();
        session.setAttribute("list", list);
        return "menu.jsp";
    }

private String handleAddProduct(HttpServletRequest request, HttpServletResponse response) {
    String checkError = "";
    String message = "";

    String productName = request.getParameter("productName");
    String description = request.getParameter("description");
    String priceStr = request.getParameter("price");
    String imageUrl = request.getParameter("imageUrl");
    String availableStr = request.getParameter("available");
    String categoryStr = request.getParameter("categoryId");
    double price = 0;
    boolean available = true;
    int categoryId = 0;
    ProductDTO p = null;
    ProductDAO pdao = new ProductDAO();

    // Validate product name
    if (productName == null || productName.trim().isEmpty()) {
        checkError += "Product name is required.<br/>";
    }

    // Validate description
    if (description == null || description.trim().isEmpty()) {
        checkError += "Description is required.<br/>";
    }

    // Validate price
    if (priceStr == null || priceStr.trim().isEmpty()) {
        checkError += "Price is required.<br/>";
    } else {
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                checkError += "Price cannot be negative.<br/>";
            }
        } catch (NumberFormatException e) {
            checkError += "Invalid price format.<br/>";
        }
    }

    // Validate image URL
    if (imageUrl == null || imageUrl.trim().isEmpty()) {
        checkError += "Image URL is required.<br/>";
    }

    // Validate availability
    if (availableStr != null) {
        available = Boolean.parseBoolean(availableStr);
    }
    
      if (categoryStr == null || categoryStr.trim().isEmpty()) {
        checkError += "category is required.+<br/>";
    } else {
        try {
            categoryId = Integer.parseInt(categoryStr);
            if (categoryId <= 0) {
                checkError += "categoryId cannot be negative.<br/>";
            }
        } catch (NumberFormatException e) {
            checkError += "Invalid categoryId format.<br/>";
        }
    }
    System.out.println(checkError);
    // If no error, create ProductDTO and add to DB
    if (checkError.isEmpty()) {
        System.out.println("2");
        p = new ProductDTO(0, productName, description, price, imageUrl, available, categoryId);
        if (!pdao.addProduct(p)) {
            checkError += "Cannot add product: " + p.getProductName() + "<br/>";
        } else {
            InventoryDAO idao = new InventoryDAO();
            if(idao.addIntoInve(0,1,p.getProductId(),0))message = "Add successfully.";;
            
            
        }
    }

    request.setAttribute("checkError", checkError);
    request.setAttribute("message", message);

    return "productForm.jsp";
    }

    


    private String handleToProduct(HttpServletRequest request, HttpServletResponse response) {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        ProductDAO pdao = new  ProductDAO();
        List<ProductDTO> list = pdao.getProductByCategoryId(categoryId);
        HttpSession session = request.getSession();
        session.setAttribute("list", list);
        return "menu.jsp";
    }

    
    

}
