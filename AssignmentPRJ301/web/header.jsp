<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="model.UserDTO" %>
<%@ page import="utils.AuthUtils" %>
<%
    UserDTO user = (UserDTO) session.getAttribute("user");
    boolean isLoggedIn = (user != null);
    boolean isAdmin = isLoggedIn && "ADMIN".equals(user.getRole());
%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    .navbar-custom {
        background:rgb(245, 192, 27) !important;
        border-radius: 0 0 18px 18px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }
    .navbar-brand {
        color: #f9eeeb !important;
        font-weight: bold;
        font-size: 1.7rem;
        letter-spacing: 1px;
    }
    .nav-link, .navbar-text {
        color: #fff !important;
        font-size: 1.12rem;
        transition: color 0.2s;
    }
    .nav-link:hover, .navbar-text:hover {
        color: #e19f8d !important;
    }
    .navbar-toggler {
        border-color: #e19f8d;
    }
    
</style>

<nav class="navbar navbar-expand-lg navbar-custom mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="welcome.jsp">RESTAURANT</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="cart.jsp">Giỏ Hàng</a></li>
                <li class="nav-item"><a class="nav-link" href="MainController?action=viewOrders">Đơn Hàng</a></li>
                <% if (isAdmin) { %>
                    <li class="nav-item"><a class="nav-link" href="MainController?action=store">Kho</a></li>
                    <li class="nav-item"><a class="nav-link" href="productForm.jsp">Thêm Món</a></li>
                <% } %>
            </ul>
            <span class="navbar-text d-flex align-items-center">
                <% if (isLoggedIn) { %>
                    Xin Chào, <span style="color:#000000;font-weight:bold;margin:0 5px;"><%= user.getFullName() %></span>
                    <a href="MainController?action=paymentHistory" style="background:#e19f8d;color:#7b4a10;border-radius:18px;" class="btn btn-sm ms-2">Nạp Tiền</a>
                    <a href="MainController?action=logout" class="btn btn-sm ms-3" style="background:#e19f8d;color:#7b4a10;border-radius:18px;">Đăng Xuất</a>
                <% } else { %>
                    <a href="login.jsp" class="btn btn-sm" style="background:#e19f8d;color:#7b4a10;border-radius:18px;">Đăng Nhập</a>
                    <a href="register.jsp" class="btn btn-sm ms-2" style="background:#e19f8d;color:#7b4a10;border-radius:18px;">Đăng Ký</a>
                <% } %>
            </span>
        </div>
    </div>
</nav>
