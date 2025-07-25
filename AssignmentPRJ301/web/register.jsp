<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
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
    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-lg-6 main-block">
          <h2 class="main-title mb-4">Đăng ký</h2>
          <form action="MainController" method="post">
            <input type="hidden" name="action" value="register"/>
            <div class="mb-3">
              <label class="form-label" for="userName">Tên đăng nhập</label>
              <input type="text" class="form-control" id="userName" name="userName" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="fullName">Họ tên</label>
              <input type="text" class="form-control" id="fullName" name="fullName" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="email">Email</label>
              <input type="email" class="form-control" id="email" name="email" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="password">Mật khẩu</label>
              <input type="password" class="form-control" id="password" name="password" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="phone">Số điện thoại</label>
              <input type="text" class="form-control" id="phone" name="phone"/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="address">Địa chỉ</label>
              <input type="text" class="form-control" id="address" name="address"/>
            </div>
            <button type="submit" class="btn main-btn w-100">Đăng ký</button>
          </form>
          <%
            String checkError = (String) request.getAttribute("checkError");
            String message = (String) request.getAttribute("message");
            if (checkError != null && !checkError.isEmpty()) {
          %>
            <div class="alert alert-danger mt-3"><%= checkError %></div>
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
    <%@include file="footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>