package com.foodmanager.listeners;

import com.foodmanager.models.ShoppingItem;

import java.util.ArrayList;

public interface ShoppingListListener {
    void onChangeList(ArrayList<ShoppingItem> itens);
}
