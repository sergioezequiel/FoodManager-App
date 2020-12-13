package com.foodmanager.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodmanager.R;
import com.foodmanager.adapters.ProductAdapter;
import com.foodmanager.adapters.RecipeAdapter;
import com.foodmanager.models.ProductItem;
import com.foodmanager.models.RecipeItem;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class RecipesFragment extends Fragment {

    private RecyclerView inventoryRecyclerView;
    private RecipeAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<RecipeItem> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView dropDownText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        return root;
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

                return false;
            }
        });
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView() {

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(26) + 75;
            inventoryItems.add(0, new ProductItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---"));
        }

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(getContext());
        inventoryAdapter = new RecipeAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }
}