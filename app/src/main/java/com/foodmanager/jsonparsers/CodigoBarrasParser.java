package com.foodmanager.jsonparsers;

import com.foodmanager.models.CodigoBarras;

import org.json.JSONException;
import org.json.JSONObject;

public class CodigoBarrasParser {
    public static CodigoBarras jsonToCodigoBarras(String response) {
        CodigoBarras convertido = null;

        try {
            JSONObject barcode = new JSONObject(response);

            convertido = new CodigoBarras(
                    barcode.getInt("codigobarras"),
                    barcode.getString("nome"),
                    barcode.getString("marca"),
                    (float) barcode.getDouble("quantidade"),
                    barcode.getInt("idproduto"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return convertido;
    }
}
