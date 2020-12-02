package com.foodmanager.models;

public class CodigoBarras {
    // O código de barras em si vai ser a primary key, visto que é único em todos os produtos do mundo
    private int codigoBarras;

    private String nome;
    private String marca;
    private float quantidade;

    public CodigoBarras(int codigoBarras, String nome, String marca, float quantidade) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.marca = marca;
        this.quantidade = quantidade;
    }

    public int getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(int codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }
}
