package com.foodmanager.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foodmanager.R;
import com.foodmanager.adapters.InventoryAdapter;
import com.foodmanager.listeners.DespensaListener;
import com.foodmanager.models.ItemDespensa;
import com.foodmanager.models.SingletonDatabaseManager;
import com.foodmanager.models.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class InventoryFragment extends Fragment implements DespensaListener {

    //variaveis privadas
    private FloatingActionButton fabAdd, fabScan, fabManual;
    private Animation fabOpen, fabClose, fabClock, fabAnticlock;
    private Boolean isOpen = false;
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ItemTouchHelper.SimpleCallback inventoryCallBack;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Dialog Edit
    private EditText txtNameEdit, txtQuantidadeEdit;
    private DatePicker validade;

    //Esta funcao inicia quando o fragmento é chamado para chamara o seu xml
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        inventoryAdapter = new InventoryAdapter(new ArrayList<ItemDespensa>());
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    //Funcao que guarda as coisas
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: modelo não existente
        //inventoryItems = new ArrayList<>(inventoryItems);
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

        SingletonDatabaseManager.getInstance(getContext()).setDespensaListener(this);
        SingletonDatabaseManager.getInstance(getContext()).getDespensa(getContext());


        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.nivel6));

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.d("gosto de pipi",  "Inventory Lista Update");
                        SingletonDatabaseManager.getInstance(getContext()).getDespensa(getContext());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
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
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(view.getContext());
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
                        SingletonDatabaseManager.getInstance(getContext()).eliminarItem(inventoryAdapter.InventoryList.get(position).getIdItemDespensa(), position, getContext());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        inventoryAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_eliminar).setTitle("Eliminar Item").setMessage("Tem a certeza que quer eliminar o item " + inventoryAdapter.InventoryList.get(position).getNome() + "?").setPositiveButton("Sim", dialogClickListener)
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
                if (!Utils.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ManualItemActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
            }
        });

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isConnected(getContext())) {
                    Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), ScanItemActivity.class);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
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
        editInventoryDialog(inventoryAdapter.InventoryList.get(position));
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
                if (inventoryAdapter != null) {
                    inventoryAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    /*Edit Values Dialog*/
    public void editInventoryDialog(ItemDespensa item) {
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.alert_dialog_edit_inventory_title, null);

        final AlertDialog diag = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ItemDespensa editedItem = item;
                        editedItem.setNome(txtNameEdit.getText().toString());
                        editedItem.setQuantidade(Float.parseFloat(txtQuantidadeEdit.getText().toString()));
                        editedItem.setValidade(validade.getYear() + "-" + (validade.getMonth() + 1) + "-" + validade.getDayOfMonth());
                        SingletonDatabaseManager.getInstance(getContext()).editarItem(editedItem, getContext());
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

        txtNameEdit = diag.findViewById(R.id.etProductName);
        txtQuantidadeEdit = diag.findViewById(R.id.etQuant);
        ImageView imagem = diag.findViewById(R.id.imageView);
        validade = diag.findViewById(R.id.datePicker1);
        String[] dataValidade = item.getValidade().split("-");

        txtNameEdit.setText(item.getNome());
        txtQuantidadeEdit.setText(item.getQuantidade() + "");
        Glide.with(getContext()).load(item.getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagem);
        validade.updateDate(Integer.parseInt(dataValidade[0]), Integer.parseInt(dataValidade[1]) - 1, Integer.parseInt(dataValidade[2]));

        /*Find views By Id / Set Text*/
        // etUpPressed = diag.findViewById(R.id.dual_chanel_ediText_up_pressed);

    }

    @Override
    public void onUpdateDespensa(ArrayList<ItemDespensa> despensa) {
        Log.d("InventoryFragment", "onUpdateDespensa called");
        inventoryAdapter = new InventoryAdapter(despensa);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

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
    }

    @Override
    public void onDelete(int position) {
        inventoryAdapter.notifyItemRemoved(position);
        inventoryAdapter.InventoryList.remove(position);
    }
}