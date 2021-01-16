package com.foodmanager.listeners;

import com.foodmanager.models.Produto;

import java.util.ArrayList;

public interface ManualItemListener {
    void onChangeCategory(ArrayList<Produto> produtos);

    void onGetCategorias(ArrayList<String> categorias);
}
