<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.InventoryDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý kho</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
      body {background: #f8f4ec;}
      .main-block {background: #7b4a10; border-radius: 20px; padding: 30px;}
      .main-title {color: #e19f8d;}
      .main-btn {color: #e19f8d; border: 2px solid #e19f8d; border-radius: 20px;}
      .main-btn:hover {background: #e19f8d; color: #7b4a10;}
    </style>
</head>
<body>
    <%if(AuthUtils.isAdmin(request)){%>
    <%@include file="header.jsp" %>
    <div class="container py-5">
      <div class="main-block">
      <h2 class="main-title mb-4">Quản lý kho</h2>
      <form action="MainController" method="post">
        <input type="hidden" name="action" value="nhapHang"/>
        <div class="table-responsive">
          <table class="table table-bordered table-striped align-middle">
            <thead>
              <tr>
                <th>Kho</th>
                <th>Mã Sản Phẩm</th>
                <th>Số Lượng</th>
                <th>Thêm Số Lượng</th>
              </tr>
            </thead>
            <tbody>
            <%
              List<InventoryDTO> list = (List<InventoryDTO>) session.getAttribute("list");
              if(list!=null){
                for(InventoryDTO i : list){
            %>
              <tr>
                <td><%=i.getInventoryId()%></td>
                <td><%=i.getProductId()%></td>
                <td><%=i.getQuantityAvailable()%></td>
                <td>
                  <input type="number" name="<%=i.getInventoryId()%>va<%=i.getProductId()%>" min="0" value="0" step="1" class="form-control"/>
                </td>
              </tr>
            <%
                }
              }
            %>
            </tbody>
          </table>
        </div>
        <button type="submit" class="btn main-btn mt-2" style="background-color: #f8f4ec">Hoàn tất</button>
      </form>
      <%
        Integer row = (Integer) request.getAttribute("row");
        if (row != null && row != 0) {
      %>
      <div class="alert alert-success mt-3">Cập nhật thành công: <%= row %> dòng</div>
      <%
        } else if (row != null) {
      %>
      <div class="alert alert-danger mt-3">Cập nhật thất bại hoặc không có dòng nào thay đổi.</div>
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