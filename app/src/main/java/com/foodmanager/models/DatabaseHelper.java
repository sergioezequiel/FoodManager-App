package com.foodmanager.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.NetworkOnMainThreadException;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    // Propriedades da base de dados
    private static final String DB_NAME = "FoodManDB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_ITENS_DESPENSA = "ItensDespensa";

    private static final String ITENS_IDITEMDESPENSA = "iditemdespensa";
    private static final String ITENS_NOME = "nome";
    private static final String ITENS_QUANTIDADE = "quantidade";
    private static final String ITENS_VALIDADE = "validade";
    private static final String ITENS_IMAGEM = "imagem";
    private static final String ITENS_UNIDADE = "unidade";
    private static final String ITENS_IDPRODUTO = "idproduto";
    private static final String ITENS_IDUTILIZADOR = "idutilizador";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TABLE_ITENS_DESPENSA + " (" +
                ITENS_IDITEMDESPENSA + " INTEGER PRIMARY KEY, " +
                ITENS_NOME           + " TEXT NOT NULL, " +
                ITENS_QUANTIDADE     + " FLOAT NOT NULL, " +
                ITENS_VALIDADE       + " TEXT NOT NULL, " +
                ITENS_IMAGEM         + " TEXT NOT NULL, " +
                ITENS_UNIDADE        + " TEXT NOT NULL );";
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDrop = "DROP TABLE IF EXISTS " + TABLE_ITENS_DESPENSA;
        db.execSQL(sqlDrop);
        this.onCreate(db);
    }

    public void adicionarItemDespensa(ItemDespensa item) {
        ContentValues values = new ContentValues();

        values.put(ITENS_IDITEMDESPENSA, item.getIdItemDespensa());
        values.put(ITENS_NOME, item.getNome());
        values.put(ITENS_QUANTIDADE, item.getQuantidade());
        values.put(ITENS_VALIDADE, item.getValidade());
        values.put(ITENS_IMAGEM, item.getImagem());
        values.put(ITENS_UNIDADE, item.getUnidade());

        db.insert(TABLE_ITENS_DESPENSA, null, values);
    }

    public boolean editarItemDespensa(ItemDespensa item) {
        ContentValues values = new ContentValues();

        values.put(ITENS_NOME, item.getNome());
        values.put(ITENS_QUANTIDADE, item.getQuantidade());
        values.put(ITENS_VALIDADE, item.getValidade());

        return db.update(TABLE_ITENS_DESPENSA, values, ITENS_IDITEMDESPENSA + "=?", new String[]{item.getIdItemDespensa() + ""}) > 0;
    }

    public boolean removerItemDespensa(int id) {
        return db.delete(TABLE_ITENS_DESPENSA, ITENS_IDITEMDESPENSA + "=?", new String[]{id + ""}) > 0;
    }

    public void removerTodosItensDespensa() {
        db.delete(TABLE_ITENS_DESPENSA, null, null);
    }

    public ArrayList<ItemDespensa> getItensDespensa() {
        ArrayList<ItemDespensa> itens = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ITENS_DESPENSA, new String[]{ITENS_IDITEMDESPENSA, ITENS_NOME, ITENS_QUANTIDADE, ITENS_VALIDADE, ITENS_IMAGEM, ITENS_UNIDADE},
                null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                itens.add(new ItemDespensa(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return itens;
    }
}
