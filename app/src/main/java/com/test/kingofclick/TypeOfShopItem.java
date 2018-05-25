package com.test.kingofclick;

/**
 * Created by Staff2 on 23.05.2018.
 */

public enum TypeOfShopItem {
    PASSIVE(1),
    ACTIVE(2),
    TITLE(3);

    private int i;

    public int getI() {
        return i;
    }

    TypeOfShopItem(int i){
        this.i=i;

    }
}
