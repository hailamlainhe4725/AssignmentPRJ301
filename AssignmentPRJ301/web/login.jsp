<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
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
        <div class="col-lg-5 main-block">
          <h2 class="main-title mb-4">Đăng nhập</h2>
          <form action="MainController" method="post">
            <input type="hidden" name="action" value="login"/>
            <div class="mb-3">
              <label class="form-label" for="strUserName">Tên đăng nhập</label>
              <input type="text" class="form-control" id="strUserName" name="strUserName" required/>
            </div>
            <div class="mb-3">
              <label class="form-label" for="strPassword">Mật khẩu</label>
              <input type="password" class="form-control" id="strPassword" name="strPassword" required/>
            </div>
            <button type="submit" class="btn main-btn w-100">Đăng nhập</button>
          </form>
          <%
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
          %>
            <div class="alert alert-danger mt-3"><%= message %></div>
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