package com.foodmanager.jsonparsers;

import com.foodmanager.models.Produto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriasParser {
    public static ArrayList<String> jsonToStringArray(JSONArray response) {
        ArrayList<String> itens = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                itens.add(item.getString("nome"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itens;
    }
}
