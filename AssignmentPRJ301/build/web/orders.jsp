<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="model.OrderDTO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f8f4ec; }
        .main-block { background: #7b4a10; border-radius: 20px; padding: 30px; }
        .main-title { color: #e19f8d; }
        .main-btn { color: #e19f8d; border: 2px solid #e19f8d; border-radius: 20px; }
        .main-btn:hover { background: #e19f8d; color: #7b4a10; }
        .table th, .table td { vertical-align: middle; }
    </style>
</head>
<body>
    <%@include file="header.jsp" %>
    <div class="container py-5">
        <div class="main-block">
            <h2 class="main-title mb-4">Tất cả đơn hàng</h2>
            <%
                if(AuthUtils.isLoggedIn(request)){
                user = AuthUtils.getCurrentUser(request);
                String msg = (String) request.getAttribute("message");
                String err = (String) request.getAttribute("checkError");
                if (msg != null) { %>
                    <div class="alert alert-success"><%= msg %></div>
            <% } else if (err != null) { %>
                    <div class="alert alert-danger"><%= err %></div>
            <% } %>
            <%
                List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
                if (orders != null && !orders.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Mã đơn</th>
                            <%if(AuthUtils.isAdmin(request)){%>
                            <th>User ID</th>
                            <%}%>
                            <th>Tổng tiền</th>
                            <th>Ngày đặt</th>
                            <th>Trạng thái</th>
                            <th>Địa chỉ giao hàng</th>
                            <%if(AuthUtils.isAdmin(request)){%>
                            <th>Thao tác</th>
                            <%}%>
                            
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        if(AuthUtils.isAdmin(request)){
                        for (OrderDTO o : orders) {
                        
                        
                    %>
                        <tr>
                            <td><%= o.getOrderId() %></td>
                            <td><%= o.getUserId() %></td>
                            <td><%= o.getTotalAmount() %></td>
                            <td><%= o.getOrderDate() %></td>
                            <td><%= o.getStatus() %></td>
                            <td><%= o.getShippingAddress() %></td>
                            <td>
                                <form action="MainController" method="post" class="d-flex align-items-center gap-2">
                                    <input type="hidden" name="action" value="updateOrderStatus"/>
                                    <input type="hidden" name="orderId" value="<%= o.getOrderId() %>"/>
                                    <select name="newStatus" class="form-select form-select-sm">
                                        <option value="PENDING" <%= o.getStatus().equals("PENDING") ? "selected" : "" %>>PENDING</option>
                                        <option value="CONFIRMED" <%= o.getStatus().equals("CONFIRMED") ? "selected" : "" %>>CONFIRMED</option>
                                        <option value="DELIVERED" <%= o.getStatus().equals("DELIVERED") ? "selected" : "" %>>DELIVERED</option>
                                        <option value="CANCELED" <%= o.getStatus().equals("CANCELED") ? "selected" : "" %>>CANCELED</option>
                                    </select>
                                    <button type="submit" class="btn main-btn btn-sm">Cập nhật</button>
                                </form>
                            </td>
                        </tr>
                    <%}%>
                        <%}else if(AuthUtils.isMember(request)){
                        for (OrderDTO o : orders) {  
                        if(user.getUserId()==o.getUserId()){
                            %>
                        <tr>
                            <td><%= o.getOrderId() %></td>
                            <td><%= o.getTotalAmount() %></td>
                            <td><%= o.getOrderDate() %></td>
                            <td><%= o.getStatus() %></td>
                            <td><%= o.getShippingAddress() %></td>
                        <%}}%>
                        
                        <%}%>
                    </tbody>
                </table>
            </div>
            <%
                } else {
            %>
            <div class="text-light mt-4">Không tìm thấy đơn hàng nào.</div>
            <%
                }
            %>
        </div>
    </div>
    <%@include file="footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <%}else{
response.sendRedirect("login.jsp");
}%>
</body>
</html>