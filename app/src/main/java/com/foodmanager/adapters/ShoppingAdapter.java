package com.foodmanager.adapters;

import android.graphics.BitmapFactory;
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
import com.foodmanager.models.ShoppingItem;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    public final ArrayList<ShoppingItem> InventoryList;
    private final ArrayList<ShoppingItem> InventoryListFull;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public Button productDeleteButton;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            productImage = itemView.findViewById(R.id.shopping_image);
            productName = itemView.findViewById(R.id.shopping_name);

            productDeleteButton = itemView.findViewById(R.id.shoppingRemove);

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

    public ShoppingAdapter(ArrayList<ShoppingItem> inventoryList) {
        InventoryList = inventoryList;
        InventoryListFull = new ArrayList<>(InventoryList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, clickListener);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem currentItem = InventoryList.get(position);

        //Glide.with(holder.productImage.getContext()).load(currentItem.getProductImage()).placeholder(R.drawable.logo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.productImage);
        holder.productName.setText(currentItem.getProductName());
        holder.productImage.setImageBitmap(BitmapFactory.decodeByteArray(currentItem.getProductImage(), 0, currentItem.getProductImage().length));
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
            ArrayList<ShoppingItem> inventoryListFiltered = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                inventoryListFiltered.addAll(InventoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ShoppingItem item : InventoryListFull) {
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
