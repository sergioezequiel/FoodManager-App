package com.foodmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class InventoryFragment extends Fragment {

    //variaveis privadas
    private FloatingActionButton fabAdd, fabScan, fabManual;
    private Animation fabOpen, fabClose, fabClock, fabAnticlock;
    private Boolean isOpen = false;
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
    private ItemTouchHelper.SimpleCallback inventoryCallBack;

    //Esta funcao inicia quando o fragmento é chamado para chamara o seu xml
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    //Funcao que guarda as coisas
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        inventoryItems = new ArrayList<>(inventoryItems);
    }

    //Funcao é executada quando o fragmento é completamente creado
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Esta funcao é para ir buscar todas a views pelo id
        findViewsById(view);
        //Esta funcao server para preparar o recycler view para preencher os itens na lista
        prepareRecyclerView(view);
        //Funcao para ir buscar as funcoes de click
        clickFunctions(view);
        
        //Adicionar o touch helper ao recycler view
        new ItemTouchHelper(inventoryCallBack).attachToRecyclerView(inventoryRecyclerView);
    }

    //Funcao para ir buscar todas as views pelo id
    private void findViewsById(View view) {
        fabAdd = view.findViewById(R.id.inventoryAdd);
        fabScan = view.findViewById(R.id.inventoryAddScanProduct);
        fabManual = view.findViewById(R.id.inventoryAddManualProduct);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);
        inventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);
        
    }

    //Funcao para perparar o recycler view e por os itens dentro
    private void prepareRecyclerView(View view) {

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(26) + 75;
            inventoryItems.add(0, new InventoryItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---", random));
        }

        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(view.getContext());
        inventoryAdapter = new InventoryAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
    }

    //Funcao para remover os itens do recycler view
    public void removeItem(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        inventoryItems.remove(position);
                        inventoryAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Item " + position + " foi removido.", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        inventoryAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Tem a certeza?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    //funcao para chamar as funcoes de click de todos os butoes e eventos
    private void clickFunctions(final View view) {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fabManual.startAnimation(fabClose);
                    fabScan.startAnimation(fabClose);
                    fabAdd.startAnimation(fabAnticlock);
                    fabManual.setClickable(false);
                    fabScan.setClickable(false);
                    isOpen = false;
                } else {
                    fabManual.startAnimation(fabOpen);
                    fabScan.startAnimation(fabOpen);
                    fabAdd.startAnimation(fabClock);
                    fabManual.setClickable(true);
                    fabScan.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fabManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: adicionar manualmente o item
            }
        });

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: funcao para adicionar automaticamente o item
            }
        });

        inventoryAdapter.setOnItemClickListener(new InventoryAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                //todo: pensar em alguma coisa mais util para a funcao de item click
            }

            @Override
            public void onEditClick(int position) { 
                editItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

        inventoryCallBack = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //todo: futuramente os itens poderam ser movidos de posicao
                inventoryAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == 4) {
                    removeItem(viewHolder.getAdapterPosition());
                } else if (direction == 8) {
                    editItem(viewHolder.getAdapterPosition());
                }
            }
        };

    }

    private void editItem(int position) {
        inventoryAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Item: "+ inventoryItems.get(position).getProductName(), Toast.LENGTH_SHORT).show();
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
                inventoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}