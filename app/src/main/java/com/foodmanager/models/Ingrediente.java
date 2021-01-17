package com.foodmanager.models;

public class Ingrediente {
    // Tipos de preparação
    public static final int PICADO = 1;
    public static final int AS_RODELAS = 2;
    public static final int AOS_CUBOS = 3;

    private int idIngrediente;
    private String nome;
    private int quantidadeNecessaria;
    private int tipoPreparacao;
    private int idProduto;
    private int idReceita;

    public Ingrediente(int idIngrediente, String nome, int quantidadeNecessaria, int tipoPreparacao, int idProduto, int idReceita) {
        this.idIngrediente = idIngrediente;
        this.nome = nome;
        this.quantidadeNecessaria = quantidadeNecessaria;
        this.tipoPreparacao = tipoPreparacao;
        this.idProduto = idProduto;
        this.idReceita = idReceita;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeNecessaria() {
        return quantidadeNecessaria;
    }

    public void setQuantidadeNecessaria(int quantidadeNecessaria) {
        this.quantidadeNecessaria = quantidadeNecessaria;
    }

    public int getTipoPreparacao() {
        return tipoPreparacao;
    }

    public void setTipoPreparacao(int tipoPreparacao) {
        this.tipoPreparacao = tipoPreparacao;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }
}
