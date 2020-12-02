package com.foodmanager.models;

import android.content.Context;

import java.util.ArrayList;

public class SingletonDatabaseManager {
    private static SingletonDatabaseManager instance = null;
    private ArrayList<ItemDespensa> itensDespensa;

    public static synchronized SingletonDatabaseManager getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonDatabaseManager(context);
        }

        return instance;
    }

    private SingletonDatabaseManager(Context context) {
        itensDespensa = new ArrayList<>();
    }
}
