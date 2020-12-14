package com.foodmanager.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.foodmanager.R;
import com.foodmanager.jsonparsers.CodigoBarrasParser;
import com.foodmanager.jsonparsers.UserParser;
import com.foodmanager.listeners.LoginListener;
import com.foodmanager.listeners.ScannedBarcodeListener;

import java.util.HashMap;
import java.util.Map;

public class SingletonDatabaseManager {
    // TODO: Alterar o IP consoante a máquina em que isto está a ser corrido
    private static final String barcodeApi = "http://192.168.43.134/foodman/backend/web/api/codigosbarras";
    private static final String loginApi = "http://192.168.43.134/foodman/backend/web/api/user/login";

    private static SingletonDatabaseManager instance = null;
    private static RequestQueue volleyQueue;
    private String apikey;

    // Listeners
    private ScannedBarcodeListener scannedBarcodeListener;
    private LoginListener loginListener;

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

    public void login(final String email, final String password, final Context context) {
        StringRequest request = new StringRequest(Request.Method.POST, loginApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utilizador utilizador = UserParser.jsonToApiKey(response);

                if(utilizador == null) {
                    Toast.makeText(context, R.string.errorLogin, Toast.LENGTH_LONG).show();
                    return;
                }

                loginListener.onLogin(utilizador);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 404) {
                    Toast.makeText(context, R.string.errorLogin, Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        volleyQueue.add(request);
    }

    // Setters dos listeners

    public void setScannedBarcodeListener(ScannedBarcodeListener scannedBarcodeListener) {
        this.scannedBarcodeListener = scannedBarcodeListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
