package com.test.kingofclick;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Staff2 on 23.05.2018.
 */

public class ItemShopAdapter extends RecyclerView.Adapter{
    public static OnItemClickListener listener;
    public static class ItemShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtName;
        TextView txtItemCost;
        Button btnAddCount;
        ImageView ivItemIcon;
        ImageView ivMoneyIcon;
        ItemShop itemShop;

        public ItemShopViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtItemName);
            txtItemCost = itemView.findViewById(R.id.txtItemCost);
            btnAddCount = itemView.findViewById(R.id.btnAddCount);
            ivItemIcon = itemView.findViewById(R.id.ivItemIcon);
            ivMoneyIcon = itemView.findViewById(R.id.ivMoneyIcon);
            btnAddCount.setOnClickListener(this);
        }

        public void setItem(ItemShop item) {
            itemShop = item;
            btnAddCount.setEnabled(item.isEnable());


        }

        @Override
        public void onClick(View v) {
            ItemShopAdapter.listener.onItemClick(itemView,getLayoutPosition());
            txtItemCost.setText(String.format(Locale.ENGLISH, "%.2f", (itemShop.getCost())));
        }
    }
    public static class HeaderItemShopHolder extends RecyclerView.ViewHolder{

        TextView txtHeader;

        public HeaderItemShopHolder(View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txtHeader);
        }
    }

    List<ItemShop> items;
    ItemShopAdapter(List<ItemShop> items){
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View itemLayoutView;

        if (viewType==TypeOfShopItem.TITLE.getI()){
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stop_header, parent, false);
            vh = new HeaderItemShopHolder(itemLayoutView);
        }else{
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
            vh = new ItemShopViewHolder(itemLayoutView);
        }

        return vh;

    }



    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType().getI()==TypeOfShopItem.TITLE.getI()){
            return TypeOfShopItem.TITLE.getI();
        }else
            return TypeOfShopItem.ACTIVE.getI();

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (this.getItemViewType(position)==TypeOfShopItem.TITLE.getI()){
            HeaderItemShopHolder headerItemShopHolder = (HeaderItemShopHolder) holder;
             headerItemShopHolder.txtHeader.setText(items.get(position).getName());
        }else{
            ItemShopViewHolder itemShopViewHolder = (ItemShopViewHolder) holder;
            itemShopViewHolder.setItem(items.get(position));
            itemShopViewHolder.ivMoneyIcon.setImageResource(R.drawable.money_icon);
            itemShopViewHolder.ivItemIcon.setImageResource(items.get(position).getImg());
            itemShopViewHolder.btnAddCount.setText(String.format(Locale.ENGLISH,"+%.2f",items.get(position).getAddCount()));
            itemShopViewHolder.txtItemCost.setText(String.format(Locale.ENGLISH,"+%.2f",items.get(position).getCost()));
            itemShopViewHolder.txtName.setText(items.get(position).getName());

        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
