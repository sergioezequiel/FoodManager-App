package com.foodmanager.models;

public class Receita {
    private int idReceita;
    private String titulo;
    private int duracaoReceita;
    private int duracaoPreparacao;
    private String passos;
    private int idUtilizador;

    public Receita(int idReceita, String titulo, int duracaoReceita, int duracaoPreparacao, String passos, int idUtilizador) {
        this.idReceita = idReceita;
        this.titulo = titulo;
        this.duracaoReceita = duracaoReceita;
        this.duracaoPreparacao = duracaoPreparacao;
        this.passos = passos;
        this.idUtilizador = idUtilizador;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracaoReceita() {
        return duracaoReceita;
    }

    public void setDuracaoReceita(int duracaoReceita) {
        this.duracaoReceita = duracaoReceita;
    }

    public int getDuracaoPreparacao() {
        return duracaoPreparacao;
    }

    public void setDuracaoPreparacao(int duracaoPreparacao) {
        this.duracaoPreparacao = duracaoPreparacao;
    }

    public String getPassos() {
        return passos;
    }

    public void setPassos(String passos) {
        this.passos = passos;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }
}
