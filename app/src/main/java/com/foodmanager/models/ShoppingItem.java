package com.foodmanager.models;

public class ShoppingItem {

    private int iditem;
    private byte[] productImage;
    private String productName;


    public ShoppingItem(int iditem, byte[] productImage, String productName) {
        this.iditem = iditem;
        this.productImage = productImage;
        this.productName = productName;
    }

    public ShoppingItem() {}

    public byte[] getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public int getIditem() {
        return iditem;
    }

    public void setIditem(int iditem) {
        this.iditem = iditem;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
