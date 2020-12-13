package com.foodmanager.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.foodmanager.jsonparsers.CodigoBarrasParser;
import com.foodmanager.listeners.ScannedBarcodeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingletonDatabaseManager {
    // TODO: Alterar o IP consoante a máquina em que isto está a ser corrido
    private static final String barcodeApi = "http://192.168.1.74/foodman/backend/web/api/codigosbarras";

    private static SingletonDatabaseManager instance = null;
    private static RequestQueue volleyQueue;

    // Listeners
    private ScannedBarcodeListener scannedBarcodeListener;

    public static synchronized SingletonDatabaseManager getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonDatabaseManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }

        return instance;
    }

    private SingletonDatabaseManager(Context context) {

    }

    public void getCodigoBarrasAPI(final String barcode, final Context context) {
        Log.d("Debug", "Entrou no getCodigoBarrasAPI");
        StringRequest request = new StringRequest(Request.Method.GET, barcodeApi + "/" + barcode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CodigoBarras codigoBarras = CodigoBarrasParser.jsonToCodigoBarras(response);

                scannedBarcodeListener.openAddDialog(codigoBarras);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Log.d("Debug", "Build do request");

        volleyQueue.add(request);
        Log.d("Debug", "Adicionou na queue");
    }

    // Setters dos listeners

    public void setScannedBarcodeListener(ScannedBarcodeListener scannedBarcodeListener) {
        this.scannedBarcodeListener = scannedBarcodeListener;
    }
}
