package com.foodmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<InventoryItem> InventoryList;

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

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);

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
    }

    @Override
    public int getItemCount() {
        return InventoryList.size();
    }
}
