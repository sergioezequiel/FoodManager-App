package com.foodmanager.models;

// Table ItensDespensa
public class ItemDespensa {
    private int idItemDespensa;
    private String nome;
    private float quantidade;
    private String validade;
    private String imagem;
    private String unidade;

    public ItemDespensa(int idItemDespensa, String nome, float quantidade, String validade, String imagem, String unidade) {
        this.idItemDespensa = idItemDespensa;
        this.nome = nome;
        this.quantidade = quantidade;
        this.validade = validade;
        this.imagem = imagem;
        this.unidade = unidade;
    }

    public ItemDespensa() {}

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

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
