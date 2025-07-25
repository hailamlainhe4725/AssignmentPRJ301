/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class PaymentDTO {
    private int so;
    private String phone;
    private int money;
    private String ma;

    public PaymentDTO() {
    }

    public PaymentDTO(int so,String phone, int money, String ma) {
        this.so = so;
        this.phone = phone;
        this.money = money;
        this.ma = ma;
    }

    public int getSo() {
        return so;
    }

    public void setSo(int so) {
        this.so = so;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }
    
    
}
