package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foodmanager.R;
import com.foodmanager.adapters.InventoryAdapter;
import com.foodmanager.adapters.ProductAdapter;
import com.foodmanager.listeners.ManualItemListener;
import com.foodmanager.models.ItemDespensa;
import com.foodmanager.models.ProductItem;
import com.foodmanager.models.Produto;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ManualItemActivity extends AppCompatActivity implements ManualItemListener {

    private RecyclerView inventoryRecyclerView;
    private ProductAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<Produto> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;
    private ArrayList<String> categorias;

    // Dialog adicionar
    private EditText productName, productQty;
    private DatePicker validade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nivel6)));

        inventoryRecyclerView = findViewById(R.id.inventoryRecyclerViewAddManual);
        prepareRecyclerView();

        textInputLayout = findViewById(R.id.text_input_layout);
        dropDownText = findViewById(R.id.dropdown_text);

        SingletonDatabaseManager.getInstance(getApplicationContext()).setManualItemListener(this);
        SingletonDatabaseManager.getInstance(getApplicationContext()).getCategoriasString(getApplicationContext());

        dropDownText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingletonDatabaseManager.getInstance(getApplicationContext()).getProdutosPelaCategoria(categorias.get(position), getApplicationContext());
            }
        });

        EditText etTest = findViewById(R.id.productName);
        etTest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inventoryAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(this);
        inventoryAdapter = new ProductAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }

    /*Edit Values Dialog*/
    public void addItemDialog(Produto produto) {
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View titleView = inflater.inflate(R.layout.alert_dialog_add_scan_inventory_title, null);

        final AlertDialog diag = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton(Html.fromHtml("<font color='#FEB117'><strong>Add Item</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ItemDespensa newItem = new ItemDespensa();
                        newItem.setNome(productName.getText().toString());
                        if(!productQty.getText().toString().trim().equals("")) {
                            newItem.setQuantidade(Float.parseFloat(productQty.getText().toString()));
                        }
                        else {
                            newItem.setQuantidade(0.0f);
                        }
                        newItem.setValidade(validade.getYear() + "-" + (validade.getMonth() + 1) + "-" + validade.getDayOfMonth());
                        Log.d("nome", newItem.getNome());
                        Log.d("quant", newItem.getQuantidade() + "");
                        Log.d("validade", newItem.getValidade());
                        Log.d("ID produto", produto.getIdProduto() + "");
                        SingletonDatabaseManager.getInstance(getApplicationContext()).adicionarItem(newItem, produto.getIdProduto(), getApplicationContext());
                        finish();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#FEB117'><strong>Cancel</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setView(R.layout.alert_dialog_edit_inventory_body)
                .create();
        diag.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(diag.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        diag.getWindow().setAttributes(lp);

        productName = diag.findViewById(R.id.etProductName);
        productQty = diag.findViewById(R.id.etQuant);
        validade = diag.findViewById(R.id.datePicker1);
        ImageView img = diag.findViewById(R.id.imageView);

        Glide.with(getApplicationContext()).load(produto.getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        productName.setText(produto.getNome());
    }

    @Override
    public void onChangeCategory(ArrayList<Produto> produtos) {
        Log.d("Listener", "onChangeCategory called");
        inventoryAdapter = new ProductAdapter(produtos);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
        inventoryAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                addItemDialog(produtos.get(position));
            }
        });
        inventoryItems = produtos;
    }

    @Override
    public void onGetCategorias(ArrayList<String> categorias) {
        Log.d("Listener", "onGetCategorias called");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ManualItemActivity.this, R.layout.dropdown_item, categorias);
        this.categorias = categorias;
        dropDownText.setAdapter(adapter);
    }

}