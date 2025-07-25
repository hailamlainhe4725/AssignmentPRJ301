<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.ProductDTO"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
      body {background: #f8f4ec;}
      .main-title {color: #e19f8d;}
      .main-btn {color: #e19f8d; border: 2px solid #e19f8d; border-radius: 20px;}
      .main-btn:hover {background: #e19f8d; color: #7b4a10;}
        .back-link {
        color: #e17d64; /* hồng cam đậm */
        font-weight: bold;
        text-decoration: none;
        font-size: 16px;
        margin: 10px 20px;
        display: inline-block;
        transition: color 0.3s, text-shadow 0.3s;
    }

    .back-link:hover {
        color: #d65b4f; /* đậm hơn khi hover */
        text-shadow: 1px 1px 2px #e6a5a0;
    }
    </style>
</head>
<body style="background-color: #f3e5e0;">
    <%@include file="header.jsp" %>
    <a class="back-link" href="welcome.jsp"><h1>◀ Trở Lại</h1></a>
    <div class="container py-5">
      <h2 class="main-title mb-4">Thực đơn</h2>
      <% String message = (String) request.getAttribute("message"); %>
      <% if (message != null && !message.isEmpty()) { %>
        <div class="alert alert-success"><%= message %></div>
      <% } %>
      <div class="row g-4">
      <%
        List<ProductDTO> list = (List<ProductDTO>) session.getAttribute("list");
        if (list != null) {
            for (ProductDTO p : list) {
      %>
        <div class="col-md-4">
          <div class="card h-100 shadow">
            <img src="<%= request.getContextPath() + "/assets/image/" + p.getImageUrl().replaceFirst("^.*/", "") %>" class="card-img-top" style="object-fit:cover; height:200px;">
            <div class="card-body">
              <h5 class="card-title"><%= p.getProductName() %></h5>
              <p class="card-text"><%= p.getDescription() %></p>
              <p class="fw-bold text-success">Giá: <%= p.getPrice() %> VND</p>
              <form action="MainController" method="post" class="mt-2">
                <input type="hidden" name="action" value="addCart"/>
                <input type="hidden" name="productId" value="<%= p.getProductId() %>"/>
                <input type="number" name="quantity" value="1" min="1" class="form-control mb-2"/>
                <input type="text" name="note" placeholder="Ghi chú" class="form-control mb-2"/>
                <button type="submit" class="btn main-btn w-100">Thêm vào giỏ</button>
              </form>
            </div>
          </div>
        </div>
      <%
            }
        }
      %>
      </div>
    </div>
    <%@include file="footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>