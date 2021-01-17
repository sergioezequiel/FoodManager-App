package com.foodmanager.models;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.foodmanager.R;
import com.foodmanager.jsonparsers.CategoriasParser;
import com.foodmanager.jsonparsers.CodigoBarrasParser;
import com.foodmanager.jsonparsers.ItensDespensaParser;
import com.foodmanager.jsonparsers.ProdutosParser;
import com.foodmanager.jsonparsers.UserParser;
import com.foodmanager.listeners.DespensaListener;
import com.foodmanager.listeners.LoginListener;
import com.foodmanager.listeners.ManualItemListener;
import com.foodmanager.listeners.ScannedBarcodeListener;
import com.foodmanager.listeners.ShoppingListListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingletonDatabaseManager {
    // TODO: Alterar o IP consoante onde a app é corrida
    // O 10.0.2.2 é usado no emulador para usar o endereço do computador local: https://stackoverflow.com/a/6310592/10294941
    private static final String WEBSITE_IP = "192.168.1.67";

    private static final String barcodeApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/codigosbarras/codigocomimagem";
    private static final String loginApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/user/login";
    private static final String despensaApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/itensdespensa/despensa";
    private static final String adicionarApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/itensdespensa/adicionaritem";
    private static final String defaultDespensaApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/itensdespensa";
    private static final String produtosCategoriaApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/produtos/pelacategoria";
    private static final String categoriasApi = "http://" + WEBSITE_IP + "/foodman/backend/web/api/categorias";

    private static SingletonDatabaseManager instance = null;
    private DatabaseHelper helper;
    private static RequestQueue volleyQueue;
    private String apikey;

    // Listeners
    private ScannedBarcodeListener scannedBarcodeListener;
    private LoginListener loginListener;
    private DespensaListener despensaListener;
    private ManualItemListener manualItemListener;
    private ShoppingListListener shoppingListListener;

    public static synchronized SingletonDatabaseManager getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonDatabaseManager(context);
            volleyQueue = Volley.newRequestQueue(context);
        }

        return instance;
    }

    private SingletonDatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
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
                if(error.networkResponse.statusCode == 404) {
                    Toast.makeText(context, R.string.noBarcodeToast, Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.d("URL codigobarras", request.getUrl());

        volleyQueue.add(request);
    }

    public void login(final String email, final String password, final Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, loginApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utilizador utilizador = UserParser.jsonToApiKey(response);

                if(utilizador == null) {
                    Toast.makeText(context, R.string.errorLogin, Toast.LENGTH_LONG).show();
                    return;
                }

                apikey = utilizador.getApikey();

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

    public void getDespensa(final Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();

            if(despensaListener != null) {
                despensaListener.onUpdateDespensa(helper.getItensDespensa());
            }
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, despensaApi + "/" + apikey, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<ItemDespensa> despensa = ItensDespensaParser.jsonToItensDespensa(response);

                despensaListener.onUpdateDespensa(despensa);
                helper.removerTodosItensDespensa();
                for (ItemDespensa item : despensa) {
                    helper.adicionarItemDespensa(item);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 404) {
                    Toast.makeText(context, R.string.errorLogin, Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.d("Singleton", "URL do request: " + request.getUrl());

        volleyQueue.add(request);
    }

    public void adicionarItem(final ItemDespensa item, final int idproduto, Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();

            if(despensaListener != null) {
                despensaListener.onUpdateDespensa(helper.getItensDespensa());
            }
            return;
        }
        Log.d("Pass", "Passou o check da internet e vai fazer o request");
        StringRequest request = new StringRequest(Request.Method.POST, adicionarApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respota add", response);
                ItemDespensa temp = ItensDespensaParser.jsonToItemDespensa(response);
                helper.adicionarItemDespensa(temp);

                if(despensaListener != null) {
                    despensaListener.onUpdateDespensa(helper.getItensDespensa());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erro Volley", " " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nome", item.getNome());
                params.put("quantidade", Float.toString(item.getQuantidade()));
                params.put("validade", item.getValidade());
                params.put("idproduto", Integer.toString(idproduto));
                params.put("apikey", apikey);

                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void eliminarItem(final int iditem, final int position, Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();

            if(despensaListener != null) {
                despensaListener.onUpdateDespensa(helper.getItensDespensa());
            }
            return;
        }
        StringRequest request = new StringRequest(Request.Method.DELETE, defaultDespensaApi + "/" + iditem, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                helper.removerItemDespensa(iditem);

                if(despensaListener != null) {
                    despensaListener.onDelete(position);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erro Volley", " " + error.getMessage());
            }
        });

        volleyQueue.add(request);
    }

    public void editarItem(ItemDespensa item, Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();

            if(despensaListener != null) {
                despensaListener.onUpdateDespensa(helper.getItensDespensa());
            }
            return;
        }
        StringRequest request = new StringRequest(Request.Method.PUT, defaultDespensaApi + "/" + item.getIdItemDespensa(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                helper.editarItemDespensa(item);

                if(despensaListener != null) {
                    despensaListener.onUpdateDespensa(helper.getItensDespensa());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erro Volley", " " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nome", item.getNome());
                params.put("quantidade", Float.toString(item.getQuantidade()));
                params.put("validade", item.getValidade());

                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void getCategoriasString(final Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, categoriasApi, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<String> categorias = CategoriasParser.jsonToStringArray(response);

                if(manualItemListener != null) {
                    manualItemListener.onGetCategorias(categorias);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 404) {
                    Toast.makeText(context, R.string.internalError, Toast.LENGTH_LONG).show();
                }
            }
        });

        volleyQueue.add(request);
    }

    public void getProdutosPelaCategoria(String categoria, final Context context) {
        if(!Utils.isConnected(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
            return;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, produtosCategoriaApi + "/" + categoria, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Produto> produtos = ProdutosParser.jsonToProdutos(response);

                if(manualItemListener != null) {
                    manualItemListener.onChangeCategory(produtos);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 404) {
                    Toast.makeText(context, R.string.internalError, Toast.LENGTH_LONG).show();
                }
            }
        });

        volleyQueue.add(request);
    }

    public void adicionarItemShopping(ShoppingItem item) {
        helper.adicionarItemShopping(item);
        if(shoppingListListener != null) {
            shoppingListListener.onChangeList(helper.getItensShopping());
        }
    }

    public ArrayList<ShoppingItem> getItensShopping() {
        return helper.getItensShopping();
    }

    public void deleteItemShopping(int item) {
        helper.removerItemShopping(item);
        if(shoppingListListener != null) {
            shoppingListListener.onChangeList(helper.getItensShopping());
        }
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    // Setters dos listeners

    public void setScannedBarcodeListener(ScannedBarcodeListener scannedBarcodeListener) {
        this.scannedBarcodeListener = scannedBarcodeListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setDespensaListener(DespensaListener despensaListener) {
        this.despensaListener = despensaListener;
    }

    public void setManualItemListener(ManualItemListener manualItemListener) {
        this.manualItemListener = manualItemListener;
    }

    public void setShoppingListListener(ShoppingListListener shoppingListListener) {
        this.shoppingListListener = shoppingListListener;
    }
}
