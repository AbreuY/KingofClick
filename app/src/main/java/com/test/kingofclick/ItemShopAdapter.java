package com.test.kingofclick;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Staff2 on 23.05.2018.
 */

public class ItemShopAdapter extends RecyclerView.Adapter<ItemShopAdapter.ItemShopViewHolder>{
    public static class ItemShopViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtItemCost;
        TextView txtAddCount;
        ImageView ivItemIcon;
        ImageView ivMoneyIcon;

        public ItemShopViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtItemName);
            txtItemCost = itemView.findViewById(R.id.txtItemCost);
            txtAddCount = itemView.findViewById(R.id.txtAddCount);
            ivItemIcon = itemView.findViewById(R.id.ivItemIcon);
            ivMoneyIcon = itemView.findViewById(R.id.ivMoneyIcon);
        }
    }

    List<ItemShop> items;
    ItemShopAdapter(List<ItemShop> items){
        this.items = items;
    }


    @NonNull
    @Override
    public ItemShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        ItemShopViewHolder pvh = new ItemShopViewHolder(v);
        return pvh;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemShopViewHolder holder, int position) {
        holder.ivMoneyIcon.setImageResource(R.drawable.money_icon);
        holder.ivItemIcon.setImageResource(items.get(position).getImg());
        holder.txtAddCount.setText(String.format(Locale.ENGLISH,"%.2f",items.get(position).getAddCount()));
        holder.txtItemCost.setText(String.format(Locale.ENGLISH,"%.2f",items.get(position).getCost()));
        holder.txtName.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
