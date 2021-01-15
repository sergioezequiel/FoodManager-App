package com.foodmanager.jsonparsers;

import com.foodmanager.models.ItemDespensa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItensDespensaParser {
    public static ArrayList<ItemDespensa> jsonToItensDespensa(JSONArray response) {
        ArrayList<ItemDespensa> itens = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                itens.add(new ItemDespensa(item.getInt("iditemdespensa"), item.getString("nome"), (float) item.getDouble("quantidade"), item.getString("validade"), item.getString("imagem"), item.getString("unidade")));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return itens;
    }

    public static ItemDespensa jsonToItemDespensa(String response) {
        try {
            JSONObject item = new JSONObject(response);
            return new ItemDespensa(item.getInt("iditemdespensa"), item.getString("nome"), (float) item.getDouble("quantidade"), item.getString("validade"), item.getString("imagem"), item.getString("unidade"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
