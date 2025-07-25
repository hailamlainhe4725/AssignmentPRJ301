<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.CartDTO"%>
<%@page import="model.CartItemDTO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Your Cart</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
          body {background: #f8f4ec;}
          .main-block {background: #7b4a10; border-radius: 20px; padding: 30px;}
          .main-title {color: #e19f8d;}
          .main-btn {color: #e19f8d; border: 2px solid #e19f8d; border-radius: 20px;}
          .main-btn:hover {background: #e19f8d; color: #7b4a10;}

          /* Đảm bảo body chiếm đủ chiều cao để đẩy footer xuống đáy */
          html, body {
              height: 100%;
              margin: 0;
              display: flex;
              flex-direction: column;
          }

          main {
              flex: 1;
          }
        </style>
    </head>
    <body>
        <%@include file="header.jsp" %>

        <!-- Bọc phần nội dung chính vào <main> -->
        <main>
        <div class="container py-5">
        <%
            if (AuthUtils.isLoggedIn(request)) {
                CartDTO cart = (CartDTO) session.getAttribute("cart");
                String error = (String) request.getAttribute("checkError");
                String message = (String) request.getAttribute("message");
        %>
        <div class="main-block">
        <h2 class="main-title mb-4">Giỏ hàng của bạn</h2>
        <a href="menu.jsp" class="btn main-btn mb-3">← Quay lại Menu</a>
        <% if (error != null && !error.isEmpty()) { %>
          <div class="alert alert-danger"><%= error %></div>
        <% } %>
        <% if (message != null && !message.isEmpty()) { %>
          <div class="alert alert-success"><%= message %></div>
        <% } %>
        <%
        if (cart == null || cart.getItems().isEmpty()) {
        %>
          <p class="text-light">Giỏ hàng đang trống.</p>
        <%
        } else {
        %>
        <div class="table-responsive">
          <table class="table table-bordered table-striped align-middle">
            <thead>
              <tr>
                <th>Sản phẩm</th>
                <th>Đơn giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
                <th>Ghi chú</th>
              </tr>
            </thead>
            <tbody>
            <%
                for (CartItemDTO item : cart.getItems()) {
            %>
              <tr>
                <td><%= item.getProductName() %></td>
                <td><%= item.getUnitPrice() %></td>
                <td>
                  <form action="MainController" method="post" class="d-inline">
                    <input type="hidden" name="action" value="updateQuantity"/>
                    <input type="hidden" name="productId" value="<%= item.getProductId() %>"/>
                    <input type="number" name="change" value="1" class="form-control d-inline w-25"/>
                    <button type="submit" class="btn btn-sm btn-info">Cập nhật</button>
                  </form>
                  <span>x <%= item.getQuantity() %></span>
                </td>
                <td><%= item.getUnitPrice() * item.getQuantity() %></td>
                <td>
                  <form action="MainController" method="post" class="d-inline">
                    <input type="hidden" name="action" value="updateNote"/>
                    <input type="hidden" name="productId" value="<%= item.getProductId() %>"/>
                    <input type="text" name="note" value="<%= item.getNote() %>" class="form-control d-inline w-75"/>
                    <button type="submit" class="btn btn-sm btn-success">Lưu</button>
                  </form>
                </td>
              </tr>
            <%
                }
            %>
              <tr>
                <td colspan="3" class="text-end fw-bold">Tổng cộng:</td>
                <td colspan="2" class="fw-bold"><%= cart.getTotalAmount() %> VND</td>
              </tr>
            </tbody>
          </table>
        </div>
        <form action="MainController" method="post" class="mt-3">
            <input type="hidden" name="action" value="checkout"/>
            <label class="text-light">Địa chỉ giao hàng:</label>
            <input type="text" name="shippingAddress" required class="form-control mb-2"/>
            <button type="submit" class="btn main-btn">Thanh toán</button>
        </form>
        <%
            }
        %>
        </div>
        <% } else { 
        response.sendRedirect("login.jsp");
} %>
        </div>
        </main>

        <%@include file="footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
