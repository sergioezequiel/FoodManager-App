package com.foodmanager.jsonparsers;

import com.foodmanager.models.CodigoBarras;
import com.foodmanager.models.Utilizador;

import org.json.JSONException;
import org.json.JSONObject;

public class UserParser {
    public static Utilizador jsonToApiKey(String response) {
        Utilizador convertido = null;

        try {
            JSONObject apikey = new JSONObject(response);

            if(apikey.getBoolean("success")) {
                convertido = new Utilizador(apikey.getString("nome"), apikey.getString("email"), apikey.getString("key"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return convertido;
    }
}
