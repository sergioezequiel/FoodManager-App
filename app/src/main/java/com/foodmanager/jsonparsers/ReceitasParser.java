package com.foodmanager.jsonparsers;

import com.foodmanager.models.Produto;
import com.foodmanager.models.Receita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceitasParser {
    public static ArrayList<Receita> jsonToReceitas(JSONArray response) {
        ArrayList<Receita> itens = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                itens.add(new Receita(item.getInt("idreceita"), item.getString("nome"), item.getInt("duracaoreceita"), item.getInt("duracaopreparacao"), item.getString("passos"), item.getString("imagem"), item.getInt("idutilizador")));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
