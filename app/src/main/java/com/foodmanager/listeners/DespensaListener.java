package com.foodmanager.listeners;

import com.foodmanager.models.ItemDespensa;

import java.util.ArrayList;

public interface DespensaListener {
    void onUpdateDespensa(ArrayList<ItemDespensa> despensa);

    void onDelete(int position);
}
