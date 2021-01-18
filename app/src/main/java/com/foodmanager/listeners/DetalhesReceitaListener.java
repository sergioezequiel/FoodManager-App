package com.foodmanager.listeners;

import com.foodmanager.models.Ingrediente;

import java.util.ArrayList;

public interface DetalhesReceitaListener {
    void onGetIngredientesEmFalta(ArrayList<Ingrediente> ingredientes);
    void onGetIngredientesCorretos(ArrayList<Ingrediente> ingredientes);
}
