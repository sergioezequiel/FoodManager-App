package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.foodmanager.R;
import com.foodmanager.adapters.InventoryAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Random;

public class ManualItemActivity extends AppCompatActivity {

    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    //private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;

    // TODO: adaptar para os novos modelos

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
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(26) + 75;
            //inventoryItems.add(0, new InventoryItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---", random));
        }

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(this);
        //inventoryAdapter = new InventoryAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }
}