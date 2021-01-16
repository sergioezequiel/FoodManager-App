package com.foodmanager.views;

import androidx.annotation.NonNull;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foodmanager.R;
import com.foodmanager.adapters.InventoryAdapter;
import com.foodmanager.adapters.ProductAdapter;
import com.foodmanager.models.InventoryItem;
import com.foodmanager.models.ProductItem;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ManualItemActivity extends AppCompatActivity {

    private RecyclerView inventoryRecyclerView;
    private ProductAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<ProductItem> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;
    private TextInputLayout editTextEmail;

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

        String[] items = new String[]{
                "Item 1",
                "Item 2",
                "Item 3",
                "Others"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ManualItemActivity.this,
                R.layout.dropdown_item,
                items
        );

        dropDownText.setAdapter(adapter);

        inventoryAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                //todo: pensar em alguma coisa mais util para a funcao de item click
            }

            @Override
            public void onAddClick(int position) {
                addItemDialog(inventoryItems.get(position).getProductName());
            }
        });

        editTextEmail = findViewById(R.id.editTextEmail);

    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(26) + 75;
            inventoryItems.add(0, new ProductItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---"));
        }

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(this);
        inventoryAdapter = new ProductAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }

    /*Edit Values Dialog*/
    public void addItemDialog(CharSequence productName) {
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View titleView = inflater.inflate(R.layout.alert_dialog_add_scan_inventory_title, null);

        final AlertDialog diag = new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton(Html.fromHtml("<font color='#FEB117'><strong>Add Item</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setView(R.layout.alert_dialog_add_scan_inventory_body)
                .create();
        diag.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(diag.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        diag.getWindow().setAttributes(lp);

        /*Find views By Id*/
        Button btnAddQty = diag.findViewById(R.id.btn_alert_dialog_add_item_qty);
        Button btnRemoveQty = diag.findViewById(R.id.btn_alert_dialog_remove_item_qty);
        final TextView txtQty = diag.findViewById(R.id.txt_qty_item);
        TextView txtProductName = diag.findViewById(R.id.txt_alert_dialog_product_name);
        txtProductName.setText(productName);

        btnAddQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                qty += 1;
                txtQty.setText(Integer.toString(qty));
            }
        });

        btnRemoveQty.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt((String) txtQty.getText());
                if (qty > 1)
                    qty -= 1;
                txtQty.setText(Integer.toString(qty));
            }
        });

    }
}