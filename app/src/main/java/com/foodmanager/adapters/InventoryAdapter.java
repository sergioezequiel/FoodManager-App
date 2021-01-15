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
import com.foodmanager.models.ItemDespensa;
import com.foodmanager.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private final ArrayList<ItemDespensa> InventoryList;
    private final ArrayList<ItemDespensa> InventoryListFull;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemCLick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productDescription;
        public TextView productQuantity;
        public Button productEditButton, productDeleteButton;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productEditButton = itemView.findViewById(R.id.inventoryEdit);
            productDeleteButton = itemView.findViewById(R.id.inventoryRemove);

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

            productEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            productDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public InventoryAdapter(ArrayList<ItemDespensa> inventoryList) {
        InventoryList = inventoryList;
        InventoryListFull = new ArrayList<>(InventoryList);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDespensa currentItem = InventoryList.get(position);

        Glide.with(holder.productImage.getContext()).load(currentItem.getImagem()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.productImage);
        holder.productName.setText(currentItem.getNome());
        holder.productDescription.setText("Expires: " + currentItem.getValidade());
        holder.productQuantity.setText("QTY: " + currentItem.getQuantidade() + " " + currentItem.getUnidade());
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
            ArrayList<ItemDespensa> inventoryListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                inventoryListFiltered.addAll(InventoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemDespensa item : InventoryListFull) {
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
