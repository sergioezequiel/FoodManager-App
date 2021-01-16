package com.foodmanager.models;

public class Produto {
    private int idproduto;
    private String nome;
    private String tipoUnidade;
    private String imagem;

    // Foreign Key
    private int idCategoria;

    public Produto(int idproduto, String nome, String tipoUnidade, String imagem, int idCategoria) {
        this.idproduto = idproduto;
        this.nome = nome;
        this.tipoUnidade = tipoUnidade;
        this.idCategoria = idCategoria;
        this.imagem = imagem;
    }

    public int getIdProduto() {
        return idproduto;
    }

    public void setIdProduto(int idProduto) {
        this.idproduto = idProduto;
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
