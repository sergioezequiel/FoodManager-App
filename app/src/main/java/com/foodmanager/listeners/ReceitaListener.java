package com.foodmanager.listeners;

import com.foodmanager.models.Receita;

import java.util.ArrayList;

public interface ReceitaListener {
    void onReceitasDisponiveis(ArrayList<Receita> receitas);
}
