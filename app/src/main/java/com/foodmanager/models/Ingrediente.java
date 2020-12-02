package com.foodmanager.models;

public class Ingrediente {
    // Tipos de preparação
    public static final int PICADO = 1;
    public static final int AS_RODELAS = 2;
    public static final int AOS_CUBOS = 3;

    private int idIngrediente;
    private String nome;
    private float quantidadeNecessaria;
    private int tipoPreparacao;
    private int idProduto;
    private int idReceita;
}
