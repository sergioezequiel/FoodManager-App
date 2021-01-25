package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.foodmanager.R;

public class FeedbackActivity extends AppCompatActivity {

    private AutoCompleteTextView dropDownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nivel6)));
        setContentView(R.layout.activity_feedback);

        dropDownText = findViewById(R.id.dropdown_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIPO);

        dropDownText.setAdapter(adapter);
    }

    private static final String[] TIPO = new String[] {
            "Sugestão de Receita", "Melhoria na App", "Sugestões", "Produto em falta (código de barras)", "Feedback Geral", "Outro"
    };
}