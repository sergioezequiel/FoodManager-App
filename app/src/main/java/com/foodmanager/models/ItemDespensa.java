package com.foodmanager.models;

// Table ItensDespensa
public class ItemDespensa {
    private int idItemDespensa;
    private String nome;
    private float quantidade;
    private long validade; // Ainda não tenho a certeza como vou guardar a data, por enquanto fica em Unix time

    // Foreign Keys
    private int idProduto;
    private int idUtilizador;

    public ItemDespensa(int idItemDespensa, String nome, float quantidade, long validade, int idProduto, int idUtilizador) {
        this.idItemDespensa = idItemDespensa;
        this.nome = nome;
        this.quantidade = quantidade;
        this.validade = validade;
        this.idProduto = idProduto;
        this.idUtilizador = idUtilizador;
    }

    public int getIdItemDespensa() {
        return idItemDespensa;
    }

    public void setIdItemDespensa(int idItemDespensa) {
        this.idItemDespensa = idItemDespensa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public long getValidade() {
        return validade;
    }

    public void setValidade(long validade) {
        this.validade = validade;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }
}
