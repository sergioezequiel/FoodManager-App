package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.foodmanager.R;
import com.foodmanager.models.Feedback;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class FeedbackActivity extends AppCompatActivity {

    private AutoCompleteTextView dropDownText;
    private FloatingActionButton fabEnviar;
    private TextInputEditText tiNome, tiAssunto, tiTexto;

    private int tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nivel6)));
        setContentView(R.layout.activity_feedback);

        dropDownText = findViewById(R.id.dropdown_text);
        fabEnviar = findViewById(R.id.fabEnviar);
        tiNome = findViewById(R.id.txtNone);
        tiAssunto = findViewById(R.id.txtAssunto);
        tiTexto = findViewById(R.id.txtTexto);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TIPO);

        dropDownText.setAdapter(adapter);

        dropDownText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipo = position;
            }
        });

        fabEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback feedback = new Feedback();
                feedback.setNome(tiNome.getText().toString());
                feedback.setTipo(tipo);
                feedback.setSubject(tiAssunto.getText().toString());
                feedback.setTexto(tiTexto.getText().toString());

                SingletonDatabaseManager.getInstance(getApplicationContext()).adicionarFeedback(feedback, getApplicationContext());
            }
        });
    }

    private static final String[] TIPO = new String[] {
            "Sugestão de Receita", "Melhoria na App", "Sugestões", "Produto em falta (código de barras)", "Feedback Geral", "Outro"
    };
}