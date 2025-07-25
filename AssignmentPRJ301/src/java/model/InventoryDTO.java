/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class InventoryDTO {
    private int inventoryId;
    private int productId;
    private int quantityAvailable;


    public InventoryDTO() {
    }

    public InventoryDTO(int inventoryId, int productId, int quantityAvailable) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;

    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    
    
}
