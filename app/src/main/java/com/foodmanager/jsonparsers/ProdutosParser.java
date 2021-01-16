package com.foodmanager.jsonparsers;

import com.foodmanager.models.ItemDespensa;
import com.foodmanager.models.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProdutosParser {
    public static ArrayList<Produto> jsonToProdutos(JSONArray response) {
        ArrayList<Produto> itens = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                itens.add(new Produto(item.getInt("idproduto"), item.getString("nomeproduto"), item.getString("unidade"), item.getString("imagem"), item.getInt("idcategoria")));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
