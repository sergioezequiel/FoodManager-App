package com.foodmanager.models;

public class Feedback {
    // Tipos de Feedback
    public static final int SUGESTAO_RECEITA = 1;
    public static final int MELHORIA = 2;
    public static final int SUGESTOES = 3;
    public static final int PRODUTO_EM_FALTA = 4;
    public static final int FEEDBACK_GERAL = 5;
    public static final int OUTRO = 6;

    private int idFeedback;
    private String texto;
    private int tipo;
    private int idUtilizador;
}
