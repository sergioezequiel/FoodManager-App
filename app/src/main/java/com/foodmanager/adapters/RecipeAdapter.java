package com.foodmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foodmanager.R;
import com.foodmanager.models.Receita;
import com.foodmanager.models.RecipeItem;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final ArrayList<Receita> InventoryList;
    private final ArrayList<Receita> InventoryListFull;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipeImage;
        public TextView recipeName;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemCLick(position);
                        }
                    }
                }
            });

        }
    }

    public RecipeAdapter(ArrayList<Receita> inventoryList) {
        InventoryList = inventoryList;
        InventoryListFull = new ArrayList<>(InventoryList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, clickListener);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receita currentItem = InventoryList.get(position);

        Glide.with(holder.recipeImage.getContext()).load(currentItem.getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.recipeImage);
        holder.recipeName.setText(currentItem.getTitulo());
    }

    @Override
    public int getItemCount() {
        return InventoryList.size();
    }

    public  Filter getFilter() {
        return nameFilter;
    }

    private final Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Receita> inventoryListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                inventoryListFiltered.addAll(InventoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Receita item : InventoryListFull) {
                    if (item.getTitulo().toLowerCase().contains(filterPattern)) {
                        inventoryListFiltered.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = inventoryListFiltered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            InventoryList.clear();
            InventoryList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
