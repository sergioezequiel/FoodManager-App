package com.foodmanager.adapters;

import android.util.Log;
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
import com.foodmanager.models.ItemDespensa;
import com.foodmanager.models.ProductItem;
import com.foodmanager.models.Produto;


import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final ArrayList<Produto> InventoryList;
    private final ArrayList<Produto> InventoryListFull;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productDescription;
        public TextView productQuantity;
        public Button productAddButton;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productAddButton = itemView.findViewById(R.id.inventoryEdit);

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

    public ProductAdapter(ArrayList<Produto> inventoryList) {
        InventoryList = inventoryList;
        InventoryListFull = new ArrayList<>(InventoryList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto currentItem = InventoryList.get(position);
        System.out.println("Position: " + position);
        Glide.with(holder.productImage.getContext()).load(currentItem.getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.productImage);
        Log.d("Imagem", currentItem.getImagem());
        holder.productName.setText(currentItem.getNome());
    }

    @Override
    public int getItemCount() {
        return InventoryList.size();
    }

    public Filter getFilter() {
        return nameFilter;
    }

    private final Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Produto> inventoryListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                inventoryListFiltered.addAll(InventoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Produto item : InventoryListFull) {
                    if (item.getNome().toLowerCase().contains(filterPattern)) {
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
