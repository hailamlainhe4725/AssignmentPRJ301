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
import model.InventoryDAO;
import model.InventoryDTO;
import utils.AuthUtils;

/**
 *
 * @author admin
 */
@WebServlet(name = "InventoryController", urlPatterns = {"/InventoryController"})
public class InventoryController extends HttpServlet {

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
        String action = request.getParameter("action");

        try {
            if ("store".equals(action)) {
                url = handleStore(request, response);
            }else if("nhapHang".equals(action)){
                url = handleNhapHang(request, response);
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

    private String handleStore(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        InventoryDAO idao = new InventoryDAO();
        List<InventoryDTO> list = idao.getAllInventory();
        session.setAttribute("list", list);
        return "inventory.jsp";
    }

    private String handleNhapHang(HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        if(AuthUtils.isAdmin(request)){
        HttpSession session = request.getSession();
        List<InventoryDTO> list = (List<InventoryDTO>) session.getAttribute("list");
        InventoryDAO idao = new InventoryDAO();
        int row = 0;
        for (InventoryDTO i : list) {
            String repThayDoi = i.getInventoryId() + "va" + i.getProductId();
            int nhapHang = 0;
            try {
                nhapHang = Integer.parseInt(request.getParameter(repThayDoi));
            } catch (Exception e) {
                nhapHang = 0;
            }
            if (nhapHang > 0) {
                if (idao.themSoLuong(i, nhapHang)) {
                    row++;
                }
            }
        }
        //row la so luong dong ma khi so luong nhap hang != 0
        request.setAttribute("row", row);
        url =  handleStore(request, response);
    }
    else{
        url = "login.jsp";
    }
    return url;

}
}
