package com.foodmanager.models;

public class RecipeItem {

    private int productImage;
    private String productName;
    private String productDescription;

    //todo: tentar fazer para ir buscar a data calcular a data que expira se tiver perto de expirar para mudar a cor de lado consuante a se o produto ta bom ou nao

    public RecipeItem(int productImage, String productName, String productDescription) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public int getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

}
