package com.foodmanager.models;

public class ShoppingItem {

    private int productImage;
    private String productName;


    public ShoppingItem(int productImage, String productName) {
        this.productImage = productImage;
        this.productName = productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

}
