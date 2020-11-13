package com.foodmanager;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class InventoryFragment extends Fragment {

    private FloatingActionButton fabAdd, fabScan, fabManual;
    private Animation fabOpen, fabClose, fabClock, fabAnticlock;
    private Boolean isOpen = false;
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    /* <Funçao de quando o fragmento esta ser criado> */
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_inventory, container, false);
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

        fabAdd = view.findViewById(R.id.inventoryAdd);
        fabScan = view.findViewById(R.id.inventoryAddScanProduct);
        fabManual = view.findViewById(R.id.inventoryAddManualProduct);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fabAnticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);
        inventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);

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
                insertItem(0);
            }
        });

        fabScan.setOnClickListener(new View.OnClickListener() {
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