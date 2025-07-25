<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.ProductDTO"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm/Sửa sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
      body {background: #f8f4ec;}
      .main-block {background: #7b4a10; border-radius: 20px; padding: 30px;}
      .main-title {color: #e19f8d;}
      .main-btn {color: #e19f8d; border: 2px solid #e19f8d; border-radius: 20px;}
      .main-btn:hover {background: #e19f8d; color: #7b4a10;}
      .form-label {color: #e19f8d;}
    </style>
</head>
<body>
        <%@include file="header.jsp" %>
    <%if(AuthUtils.isAdmin(request)){%>

    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-lg-7 main-block">
          <h2 class="main-title mb-4">Thêm / Sửa sản phẩm</h2>
          <form action="MainController" method="post" >
            <input type="hidden" name="action" value="addProduct"/>
            <div class="mb-3">
              <label class="form-label" for="productName">Tên sản phẩm</label>
              <input type="text" class="form-control" id="productName" name="productName" value="<%= request.getAttribute("productName") != null ? request.getAttribute("productName") : "" %>" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="description">Mô tả</label>
              <textarea class="form-control" id="description" name="description"><%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %></textarea>
            </div>
            <div class="mb-3">
              <label class="form-label" for="price">Giá</label>
              <input type="number" class="form-control" id="price" name="price" value="<%= request.getAttribute("price") != null ? request.getAttribute("price") : "" %>" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="imageUrl">Hình ảnh</label>
              <input type="file" class="form-control" id="image" name="imageUrl"/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="categoryId">Loại</label>
              <input type="number" class="form-control" id="imagecategoryId" name="categoryId"/>
            </div>
            <button type="submit" class="btn main-btn w-100">Lưu sản phẩm</button>
          </form>
          <%
            String message = (String) request.getAttribute("message");
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) {
          %>
            <div class="alert alert-danger mt-3"><%= error %></div>
          <%
            }
            if (message != null && !message.isEmpty()) {
          %>
            <div class="alert alert-success mt-3"><%= message %></div>
          <%
            }
          %>
        </div>
      </div>
    </div>
 
    <%}else{%>
    <%=AuthUtils.getAccessDeniedMessage("This page")%> 
<%}%>
    <%@include file="footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>