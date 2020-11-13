package com.foodmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private final ArrayList<InventoryItem> InventoryList;
    private final ArrayList<InventoryItem> InventoryListFull;
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

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productQuantity = itemView.findViewById(R.id.product_quantity);

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

    public InventoryAdapter(ArrayList<InventoryItem> inventoryList) {
        InventoryList = inventoryList;
        InventoryListFull = new ArrayList<>(InventoryList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, clickListener);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryItem currentItem = InventoryList.get(position);

        holder.productImage.setImageResource(currentItem.getProductImage());
        holder.productName.setText(currentItem.getProductName());
        holder.productDescription.setText(currentItem.getProductDescription());
        holder.productQuantity.setText("QTY: "+ currentItem.getProductQuantity());
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
            ArrayList<InventoryItem> inventoryListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                inventoryListFiltered.addAll(InventoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (InventoryItem item : InventoryListFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
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
