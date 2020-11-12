package com.foodmanager;

public class InventoryItem {

    private  int productImage;
    private  String productName;
    private  String productDescription;


    public InventoryItem(int productImage, String productName, String productDescription) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public int getProductImage() {  return productImage; }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }
}
