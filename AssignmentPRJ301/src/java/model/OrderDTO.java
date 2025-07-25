package model;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    private int orderId;
    private int userId;
    private double totalAmount;
    private Date orderDate;
    private String status; // PENDING, CONFIRMED, DELIVERED, CANCELED
    private String shippingAddress;

    private List<OrderItemDTO> items;  

    public OrderDTO() {
    }

    public OrderDTO(int orderId, int userId, double totalAmount, Date orderDate, String status, String shippingAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
