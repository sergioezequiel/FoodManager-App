package com.foodmanager.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.foodmanager.R;
import com.foodmanager.adapters.InventoryAdapter;
import com.foodmanager.adapters.ProductAdapter;
import com.foodmanager.adapters.RecipeAdapter;
import com.foodmanager.listeners.ReceitaListener;
import com.foodmanager.models.ProductItem;
import com.foodmanager.models.Receita;
import com.foodmanager.models.RecipeItem;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class RecipesFragment extends Fragment implements ReceitaListener {

    private Animation fabClock, fabAnticlock;
    private RecyclerView inventoryRecyclerView;
    private RecipeAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    public static ArrayList<Receita> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private FloatingActionButton allReceitas;
    private Boolean isOpen = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inventoryRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        prepareRecyclerView();

        SingletonDatabaseManager.getInstance(getContext()).setReceitaListener(this);
        SingletonDatabaseManager.getInstance(getContext()).getReceitasDisponiveis(getContext());

        allReceitas = view.findViewById(R.id.fabAllReceitas);

        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);

        titulo("Available Recipes");
        allReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    SingletonDatabaseManager.getInstance(getContext()).getReceitasDisponiveis(getContext());
                    allReceitas.startAnimation(fabAnticlock);
                    titulo("Available Recipes");
                    isOpen = false;
                } else {
                    SingletonDatabaseManager.getInstance(getContext()).getTodasReceitas(getContext());
                    allReceitas.startAnimation(fabClock);
                    isOpen = true;
                    titulo("All Recipes");
                }
            }
        });

    }

    //Funcao para criar um menu de search
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(getContext());
        inventoryAdapter = new RecipeAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }

    @Override
    public void onReceitasDisponiveis(ArrayList<Receita> receitas) {
        inventoryAdapter = new RecipeAdapter(receitas);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        inventoryItems = receitas;

        inventoryAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {

            @Override
            public void onItemCLick(int position) {
                Intent intent = new Intent(getContext(), RecipesDetailsActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                intent.putExtra("POS", position);
                startActivity(intent);
            }
        });
    }

    public void titulo(String titulo) {
        ((MainActivity) getActivity()).setActionBarTitle(titulo);
    }
}