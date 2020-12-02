package com.foodmanager.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    // Propriedades da base de dados
    private static final String DB_NAME = "FoodManDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_PRODUTOS = "Produtos";
    private static final String TABLE_CATEGORIAS = "Categorias";
    private static final String TABLE_CODIGOS_BARRAS = "CodigosBarras";
    private static final String TABLE_FEEDBACK = "Feedback";
    private static final String TABLE_INGREDIENTES = "Ingredientes";
    private static final String TABLE_ITENS_DESPENSA = "ItensDespensa";
    private static final String TABLE_RECEITAS = "Receitas";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
