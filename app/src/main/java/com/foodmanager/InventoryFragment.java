package com.foodmanager;

/* <Importar as bibliotecas> */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.util.ArrayList;
import java.util.Random;
/* </Importar as bibliotecas> */

public class InventoryFragment extends Fragment {

    /* <Criacao das variaveis> */
    private FloatingActionButton fab_add, fab_scan, fab_manual;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private Boolean isOpen = false;
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();
    /* </Criacao das variaveis> */

    /* <Funçao de quando o fragmento esta ser criado> */
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        inventoryItems = new ArrayList<>(inventoryItems);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_add = view.findViewById(R.id.inventory_add);
        fab_scan = view.findViewById(R.id.inventory_add_scan_product);
        fab_manual = view.findViewById(R.id.inventory_add_manual_product);
        inventoryRecyclerView = view.findViewById(R.id.inventory_recycler_view);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    fab_manual.startAnimation(fab_close);
                    fab_scan.startAnimation(fab_close);
                    fab_add.startAnimation(fab_anticlock);
                    fab_manual.setClickable(false);
                    fab_scan.setClickable(false);
                    isOpen = false;
                } else {
                    fab_manual.startAnimation(fab_open);
                    fab_scan.startAnimation(fab_open);
                    fab_add.startAnimation(fab_clock);
                    fab_manual.setClickable(true);
                    fab_scan.setClickable(true);
                    isOpen = true;
                }
            }
        });

        fab_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem(0);
            }
        });

        fab_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Scan", Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < 10; i++) {
            final int random = new Random().nextInt(26) + 75;
            inventoryItems.add(0, new InventoryItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---", random));
        }


        inventoryRecyclerView.setHasFixedSize(true);
        inventoryLayoutManager = new LinearLayoutManager(view.getContext());
        inventoryAdapter = new InventoryAdapter(inventoryItems);
        inventoryRecyclerView.setLayoutManager(inventoryLayoutManager);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        inventoryAdapter.setOnItemClickListener(new InventoryAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                String pos = String.valueOf(position);
                Toast.makeText(getActivity(), pos, Toast.LENGTH_SHORT).show();
            }
        });


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                inventoryAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == 4) {
                    Toast.makeText(getActivity(), "Remove", Toast.LENGTH_SHORT).show();
                    inventoryItems.remove(viewHolder.getAdapterPosition());
                    inventoryAdapter.notifyDataSetChanged();
                } else if (direction == 8) {
                    Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                    inventoryAdapter.notifyDataSetChanged();
                }

                String swipeDirection = String.valueOf(direction);
                Toast.makeText(getActivity(), "Swipe Direction: " + swipeDirection, Toast.LENGTH_SHORT).show();
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(inventoryRecyclerView);
    }

    public void insertItem(int position) {
        final int random = new Random().nextInt(26) + 75;
        inventoryItems.add(0, new InventoryItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---", random));
        inventoryAdapter.notifyItemInserted(position);
        //inventoryItems.notifyAll();

        LinearLayoutManager layoutManager = (LinearLayoutManager) inventoryRecyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    public void removeItem(int position) {
        inventoryItems.remove(position);
        inventoryAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                Snackbar.make(getView(), query, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inventoryAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}