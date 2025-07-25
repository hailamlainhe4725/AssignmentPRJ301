<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="utils.AuthUtils" %>
<%@page import="java.util.List" %>
<%@page import="model.PaymentDTO" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thanh toán</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: #f8f4ec;
        }
        .main-block {
            background: #7b4a10;
            border-radius: 24px;
            margin-bottom: 36px;
            margin-top: 40px;
            padding: 32px 20px;
            box-shadow: 0 2px 18px rgba(0,0,0,0.07);
            transition: box-shadow .3s;
        }
        .main-block:hover {
            box-shadow: 0 6px 32px rgba(123,74,16,0.12);
        }
        .title {
            color: #e19f8d;
            font-size: 2.2rem;
            font-weight: bold;
            text-align: center;
            letter-spacing: 1px;
            margin-bottom: 20px;
        }
        .info-text {
            color: #e19f8d;
            font-size: 1.1rem;
            text-align: center;
            margin-bottom: 20px;
        }
        .qr-img {
            display: block;
            margin: 0 auto 16px auto;
            max-width: 200px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.06);
            transition: transform .2s;
        }
        .qr-img:hover {
            transform: scale(1.06) rotate(-2deg);
        }
        .payment-form .form-label {
            color: #e19f8d;
            font-weight: 500;
        }
        .payment-form .form-control {
            border-radius: 16px;
            border: 2px solid #e19f8d;
            margin-bottom: 18px;
            background: #f8f4ec;
        }
        .main-btn {
            color: #e19f8d;
            border: 2px solid #e19f8d;
            border-radius: 20px;
            background: transparent;
            font-weight: 600;
            padding: 10px 32px;
            font-size: 1.08rem;
            margin-bottom: 8px;
            margin-top: 10px;
            transition: background 0.2s, color 0.2s, transform .13s;
        }
        .main-btn:hover {
            background: #e19f8d;
            color: #7b4a10;
            transform: translateY(-2px) scale(1.04);
        }
        .alert-info {
            background: #e19f8d;
            color: #7b4a10;
            border: none;
            border-radius: 16px;
            font-weight: 500;
            box-shadow: 0 1px 6px rgba(123,74,16,0.07);
        }
        .table-striped>tbody>tr:nth-of-type(odd) {
            background: #fff6ef;
        }
        .table th, .table td {
            border-color: #e19f8d;
        }
        .card-header {
            background: #7b4a10 !important;
            color: #e19f8d !important;
            border-radius: 24px 24px 0 0 !important;
            font-size: 1.1rem;
            font-weight: bold;
            letter-spacing: 1px;
        }
        .card {
            border-radius: 24px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.04);
        }
    </style>
</head>
<body>
    <%if(AuthUtils.isLoggedIn(request)){%>
        <%@include file="header.jsp" %>

    <div class="container py-4">
        <div class="main-block">
            <div class="title">Thanh toán</div>
<img src="<%= request.getContextPath() + "/assets/image/qr.jpg" %>" 
     style="display: block; margin: 0 auto;" alt="QR Code" />

            <div class="info-text">Vui lòng ghi số điện thoại vào phần nội dung chuyển khoản</div>
            <% String message = (String)request.getAttribute("message"); %>
            <% if(AuthUtils.isAdmin(request)){ %>
            <form action="MainController" method="post" class="payment-form mx-auto" style="max-width:400px;">
                <input type="hidden" name="action" value="amtc"/>
                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại</label>
                    <input type="text" name="phone" id="phone" class="form-control" placeholder="Nhập số điện thoại"/>
                </div>
                <div class="mb-3">
                    <label for="money" class="form-label">Số tiền</label>
                    <input type="number" name="money" id="money" class="form-control" placeholder="Nhập số tiền"/>
                </div>
                <div class="mb-3">
                    <label for="ma" class="form-label">Mã giao dịch</label>
                    <input type="text" name="ma" id="ma" class="form-control" placeholder="Nhập mã giao dịch"/>
                </div>
                <button type="submit" class="main-btn w-100">Nạp tiền</button>
            </form>
            <% if(message!=null && !message.isEmpty()) { %>
                <div class="alert alert-info mt-3" role="alert">
                    <%=message%>
                </div>
            <% } %>
            <% } %>
        </div>
        <% String action = (String) request.getAttribute("action");
        if(action !=null && action.equals("paymentHistory")){ %>
        <div class="row justify-content-center mt-4">
            <div class="col-md-9">
                <div class="card shadow-sm">
                    <div class="card-header">
                        Lịch sử nạp tiền
                    </div>
                    <div class="card-body p-0">
                        <table class="table table-striped mb-0 text-center">
                            <thead>
                                <tr>
                                    <th scope="col">Số điện thoại</th>
                                    <th scope="col">Số tiền</th>
                                    <th scope="col">Mã giao dịch</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% List<PaymentDTO> list = (List<PaymentDTO>) session.getAttribute("list");
                                if(list!=null && !list.isEmpty()){
                                    for(PaymentDTO p : list){%>
                                    <tr>
                                        <td><%=p.getPhone()%></td>
                                        <td><%=p.getMoney()%></td>
                                        <td><%=p.getMa()%></td>
                                    </tr>
                                <%  }
                                } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>
        <%@include file="footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<%}else{
response.sendRedirect("login.jsp");
}%>
</body>
</html>