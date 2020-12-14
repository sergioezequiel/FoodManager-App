package com.foodmanager.models;

public class Utilizador {
    private String nome;
    private String email;
    private String apikey;

    public Utilizador(String nome, String email, String apikey) {
        this.nome = nome;
        this.email = email;
        this.apikey = apikey;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
