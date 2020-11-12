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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class InventoryFragment extends Fragment {

    private FloatingActionButton fab_add;
    private Animation fab_anticlock;
    private RecyclerView inventoryRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private RecyclerView.LayoutManager inventoryLayoutManager;
    private ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_add = view.findViewById(R.id.fab_add);
        fab_anticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_add.startAnimation(fab_anticlock);
                insertItem(0);
            }
        });

        inventoryRecyclerView = view.findViewById(R.id.inventory_recycler_view);
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
        inventoryItems.add(0, new InventoryItem(R.drawable.ic_baseline_add_24, "New: " + random, "Description: ---"));
        inventoryAdapter.notifyItemInserted(position);

        LinearLayoutManager layoutManager = (LinearLayoutManager) inventoryRecyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    public void removeItem(int position) {
        inventoryItems.remove(position);
        inventoryAdapter.notifyItemRemoved(position);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Snackbar.make(getView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}