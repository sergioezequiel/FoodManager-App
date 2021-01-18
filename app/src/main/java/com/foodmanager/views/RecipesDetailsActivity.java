package com.foodmanager.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.foodmanager.listeners.DetalhesReceitaListener;
import com.foodmanager.models.Ingrediente;
import com.foodmanager.models.Receita;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodmanager.R;

import java.util.ArrayList;

public class RecipesDetailsActivity extends AppCompatActivity implements DetalhesReceitaListener {

    private LinearLayout layoutIngredientes;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;
    private TextView receitaText;
    private Receita receita;
    private ArrayList<Ingrediente> ingredientesEmFaltaArray;
    private boolean ingredientesEmFalta = false;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        pos = intent.getIntExtra("POS", 0);
        this.setTitle(RecipesFragment.inventoryItems.get(pos).getTitulo());

        setContentView(R.layout.activity_recipes_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        receita = RecipesFragment.inventoryItems.get(pos);

        layoutIngredientes = findViewById(R.id.ingredientes);
        fab = findViewById(R.id.fab);
        appBarLayout = findViewById(R.id.app_bar);
        receitaText = findViewById(R.id.receitaText);

        Glide.with(getApplicationContext()).load(RecipesFragment.inventoryItems.get(pos).getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                appBarLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredientesEmFalta) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    SingletonDatabaseManager.getInstance(getApplicationContext()).adicionarIngredientesListaCompras(ingredientesEmFaltaArray);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipesDetailsActivity.this);
                    builder.setIcon(R.drawable.ic_baseline_add_shopping_cart_24).setTitle("Ingredientes Em Falta").setMessage("Faltam ingredientes para a receita. Pretende adicioná-los ao carrinho de compras?").setPositiveButton("Sim", dialogClickListener)
                            .setNegativeButton("Não", dialogClickListener).show();
                }
                else {
                    Snackbar.make(view, R.string.sbAllIngredientsPresent, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        receitaText.setText(Html.fromHtml(RecipesFragment.inventoryItems.get(pos).getPassos()));
        ViewCompat.setBackgroundTintList(fab, ColorStateList.valueOf(Color.GREEN));
        fab.setImageResource(R.drawable.ic_ingrediente_correto);

        SingletonDatabaseManager.getInstance(getApplicationContext()).setDetalhesReceitaListener(this);
        SingletonDatabaseManager.getInstance(getApplicationContext()).getIngredientesEmFalta(RecipesFragment.inventoryItems.get(pos).getIdReceita(), getApplicationContext());
        SingletonDatabaseManager.getInstance(getApplicationContext()).getIngredientesCorretos(RecipesFragment.inventoryItems.get(pos).getIdReceita(), getApplicationContext());
    }

    @Override
    public void onGetIngredientesEmFalta(ArrayList<Ingrediente> ingredientes) {
        for (Ingrediente ingrediente : ingredientes) {
            TextView temp = new TextView(this);
            temp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ingrediente_em_falta, 0, 0, 0);
            temp.setText(quantidades(ingrediente));
            temp.setTextColor(getResources().getColor(R.color.white));
            layoutIngredientes.addView(temp);
        }
        ViewCompat.setBackgroundTintList(fab, ColorStateList.valueOf(Color.RED));
        fab.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
        ingredientesEmFalta = true;
        ingredientesEmFaltaArray = ingredientes;
    }

    @Override
    public void onGetIngredientesCorretos(ArrayList<Ingrediente> ingredientes) {
        for (Ingrediente ingrediente : ingredientes) {
            TextView temp = new TextView(this);
            temp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ingrediente_correto, 0, 0, 0);
            temp.setText(quantidades(ingrediente));
            temp.setTextColor(getResources().getColor(R.color.white));
            layoutIngredientes.addView(temp);
        }
    }

    public String quantidades(Ingrediente ingrediente) {
        String ingredienteFinal;

        if(ingrediente.getQuantidadeNecessaria() == 0) {
            if(ingrediente.getTipoPreparacao() == 0) {
                ingredienteFinal = ingrediente.getNome();
            }
            else {
                String preparacao;
                switch (ingrediente.getTipoPreparacao()) {
                    case Ingrediente.PICADO:
                        preparacao = "Picado";
                        break;
                    case Ingrediente.AOS_CUBOS:
                        preparacao = "Aos Cubos";
                        break;
                    case Ingrediente.AS_RODELAS:
                        preparacao = "Ás Rodelas";
                        break;
                    default:
                        preparacao = "Desconhecido";
                        break;
                }
                ingredienteFinal = ingrediente.getNome() + " " + preparacao;
            }
        }
        else {
            if(ingrediente.getTipoPreparacao() == 0) {
                ingredienteFinal = ingrediente.getNome() + " (" + ingrediente.getQuantString() + ")";
            }
            else {
                String preparacao;
                switch (ingrediente.getTipoPreparacao()) {
                    case Ingrediente.PICADO:
                        preparacao = "Picado";
                        break;
                    case Ingrediente.AOS_CUBOS:
                        preparacao = "Aos Cubos";
                        break;
                    case Ingrediente.AS_RODELAS:
                        preparacao = "Ás Rodelas";
                        break;
                    default:
                        preparacao = "Desconhecido";
                        break;
                }
                ingredienteFinal = ingrediente.getNome() + " " + preparacao + " (" + ingrediente.getQuantString() + ")";
            }
        }

        return ingredienteFinal;
    }
}