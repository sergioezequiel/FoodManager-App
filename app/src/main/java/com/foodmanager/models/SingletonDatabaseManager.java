package com.foodmanager.models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.foodmanager.jsonparsers.CodigoBarrasParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingletonDatabaseManager {
    // TODO: Alterar o IP consoante a máquina em que isto está a ser corrido
    private static final String barcodeApi = "http://192.168.1.74/foodman/backend/web/api/codigosbarras";

    private static SingletonDatabaseManager instance = null;
    private ArrayList<ItemDespensa> itensDespensa;
    private RequestQueue volleyQueue;

    public static synchronized SingletonDatabaseManager getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonDatabaseManager(context);
        }

        return instance;
    }

    private SingletonDatabaseManager(Context context) {
        itensDespensa = new ArrayList<>();
    }

    public CodigoBarras getCodigoBarrasAPI(final String barcode, final Context context) {
        CodigoBarras api = null;

        StringRequest request = new StringRequest(Request.Method.GET, barcodeApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CodigoBarrasParser.jsonToCodigoBarras(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("codigobarras", barcode);

                return params;
            }
        };

        volleyQueue.add(request);
        return null;
    }
}
