package com.foodmanager.models;

public class Produto {
    private int idProduto;
    private String nome;
    private String tipoUnidade;

    // Foreign Key
    private int idCategoria;

    public Produto(int idProduto, String nome, String tipoUnidade, int idCategoria) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.tipoUnidade = tipoUnidade;
        this.idCategoria = idCategoria;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoUnidade() {
        return tipoUnidade;
    }

    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
