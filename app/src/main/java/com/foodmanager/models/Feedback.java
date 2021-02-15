package com.foodmanager.models;

public class Feedback {
    // Tipos de Feedback
    public static final int SUGESTAO_RECEITA = 0;
    public static final int MELHORIA = 1;
    public static final int SUGESTOES = 2;
    public static final int PRODUTO_EM_FALTA = 3;
    public static final int FEEDBACK_GERAL = 4;
    public static final int OUTRO = 5;

    private int idFeedback;
    private String nome;
    private String subject;
    private String texto;
    private int tipo;
    private int idUtilizador;

    public int getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }
}
