package com.foodmanager;

public class InventoryItem {

    private int productImage;
    private String productName;
    private String productDescription;
    private int productQuantity;

    //todo: tentar fazer para ir buscar a data calcular a data que expira se tiver perto de expirar para mudar a cor de lado consuante a se o produto ta bom ou nao

    public InventoryItem(int productImage, String productName, String productDescription, int productQuantity) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
