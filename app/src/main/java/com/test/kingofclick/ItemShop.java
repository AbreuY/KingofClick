package com.test.kingofclick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ItemShop implements Serializable{
    private double cost;
    private double addCount;
    private int img;
    private TypeOfShopItem type;
    private String name;

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    private boolean isEnable;

    public ItemShop(String name,double cost, double addCount, int img,TypeOfShopItem type) {
        this.cost = cost;
        this.addCount = addCount;
        this.img = img;
        this.type = type;
        this.name = name;
    }

    public ItemShop() {
        initItems();
    }

    private List<ItemShop> items;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        cost+=cost*0.02;
        this.cost = cost;
    }

    public double getAddCount() {
        return addCount;
    }

    public void setAddCount(double addCount) {
        this.addCount = addCount;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public TypeOfShopItem getType() {
        return type;
    }

    public void setType(TypeOfShopItem type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemShop> getItems() {
        return items;
    }

    public void setItems(List<ItemShop> items) {
        this.items = items;
    }

    private void initItems(){
        items = new ArrayList<>();
        items.add(new ItemShop("ACTIVE",1,1,R.drawable.circle_button,TypeOfShopItem.TITLE));
        items.add(new ItemShop("PerClick",50,1,R.drawable.test,TypeOfShopItem.ACTIVE));
        items.add(new ItemShop("PASSIVE",1,1,R.drawable.circle_button,TypeOfShopItem.TITLE));
        items.add(new ItemShop("+1",50,0.1,R.drawable.test,TypeOfShopItem.PASSIVE));
        items.add(new ItemShop("+3",100,0.3,R.drawable.test,TypeOfShopItem.PASSIVE));
        items.add(new ItemShop("+5",200,0.5,R.drawable.test,TypeOfShopItem.PASSIVE));
        items.add(new ItemShop("+10",500,1.0,R.drawable.test,TypeOfShopItem.PASSIVE));

    }
}
