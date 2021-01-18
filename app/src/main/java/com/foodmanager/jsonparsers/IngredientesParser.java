package com.foodmanager.jsonparsers;

import com.foodmanager.models.Ingrediente;
import com.foodmanager.models.Receita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IngredientesParser {
    public static ArrayList<Ingrediente> jsonToIngredientes(JSONArray response) {
        ArrayList<Ingrediente> itens = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                itens.add(new Ingrediente(item.getInt("idingrediente"), item.getString("nome"), item.getInt("quantnecessaria"), item.getString("quantstring"), item.getInt("tipopreparacao"), item.getInt("idproduto"), item.getInt("idreceita")));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
