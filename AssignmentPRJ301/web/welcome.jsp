<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.CategoryDTO" %>
<%@page import="utils.AuthUtils" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: #f8f4ec;
        }
        .main-block {
            background: #7b4a10;
            border-radius: 24px;
            margin-bottom: 40px;
            padding: 0;
            box-shadow: 0 2px 12px rgba(0,0,0,0.03);
        }
        .block-content {
            min-height: 360px;
            display: flex;
            flex-direction: row;
            align-items: stretch;
        }
        .category-left {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding: 32px;
        }
        .category-title {
            color: #e19f8d;
            font-size: 2.2rem;
            font-weight: bold;
            margin-bottom: 28px;
            letter-spacing: 1px;
            text-align: center;
        }
        .main-btn {
            color: #e19f8d;
            border: 2px solid #e19f8d;
            border-radius: 20px;
            background: transparent;
            font-weight: 600;
            padding: 10px 32px;
            font-size: 1.05rem;
            margin-bottom: 16px;
            margin-top: 8px;
            transition: background 0.2s, color 0.2s;
        }
        .main-btn:hover {
            background: #e19f8d;
            color: #7b4a10;
        }
        .category-right {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }
        .category-img {
            max-width: 100%;
            max-height: 340px;
            border-radius: 16px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.07);
            object-fit: cover;
        }
        .welcome-title {
            color: #e19f8d;
            font-size: 2.5rem;
            font-weight: bold;
            text-align: center;
            margin-top: 36px;
            margin-bottom: 34px;
            letter-spacing: 2px;
        }
        @media(max-width: 900px){
            .block-content{flex-direction:column;}
            .category-right{padding-top:0;}
        }
    </style>
</head>
<body>
    <%@include file="header.jsp" %>
    <div class="container py-4">
        <div class="welcome-title">Chào mừng bạn đến với nhà hàng</div>
        <%-- Auto load categories --%>
        <%
            Boolean loaded = (Boolean) session.getAttribute("loaded");
            if (loaded == null || !loaded) {
        %>
            <form id="autoForm" action="MainController" method="post">
                <input type="hidden" name="action" value="openCategory"/>
            </form>
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    document.getElementById("autoForm").submit();
                });
            </script>
        <%
            } 
            List<CategoryDTO> list = (List<CategoryDTO>) session.getAttribute("categoryList");
            if(list!=null){
                for(CategoryDTO l : list){
        %>
        <div class="main-block">
            <div class="block-content">
                <div class="category-left">
                    <div class="category-title"><%=l.getCategoryName()%></div>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="toProduct"/>
                        <input type="hidden" name="categoryId" value="<%=l.getCategoryId()%>"/>
                        <button type="submit" class="main-btn">Xem thực đơn</button>
                    </form>
                </div>
                <div class="category-right">
                    <img src="<%= request.getContextPath() + "/assets/image/" + l.getImage().replaceFirst("^.*/", "") %>" 
                        alt="Category Image"
                        class="category-img"/>
                </div>
            </div>
        </div>
        <%}
            }%>

        <%-- Nếu là admin: --%>
        <%
        if(AuthUtils.isAdmin(request)){
        %>
        <div class="main-block text-center p-4">
            <a href="productForm.jsp" class="main-btn">Thêm món mới</a>
            <form action="MainController" method="post" class="d-inline-block mt-2">
                <input type="hidden" name="action" value="store"/>
                <button type="submit" class="main-btn">Xem kho</button>
            </form>
        </div>
        <%}%>
    </div>
    <%@include file="footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>