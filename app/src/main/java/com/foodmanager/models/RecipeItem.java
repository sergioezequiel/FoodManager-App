package com.foodmanager.models;

public class RecipeItem {

    private int recipeImage;
    private String recipeName;

    //todo: tentar fazer para ir buscar a data calcular a data que expira se tiver perto de expirar para mudar a cor de lado consuante a se o produto ta bom ou nao

    public RecipeItem(int recipeImage, String recipeName) {
        this.recipeImage = recipeImage;
        this.recipeName = recipeName;
    }

    public int getRecipeImage() {
        return recipeImage;
    }

    public String getRecipeName() {
        return recipeName;
    }

}
