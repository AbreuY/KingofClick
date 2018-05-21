package com.test.kingofclick;

public class ItemShop {
    private double cost;
    private double procent;
    private int countOfBuy;
    private double plus=0.1;

    public double getPlus() {
        return plus;
    }

    public void setPlus(double plus) {
        plus*=1.05;
        this.plus = plus;
    }

    public ItemShop(double cost, double procent) {
        this.cost = cost;
        this.procent = procent;

    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        cost*=1.05;
        this.cost =cost;
    }

    public double getProcent() {

        return procent;
    }

    public void setProcent(double procent) {
        procent-=(procent*0.05);
        this.procent = procent;
    }

    public int getCountOfBuy() {
        return countOfBuy;
    }

    public void setCountOfBuy(int countOfBuy) {
        this.countOfBuy = countOfBuy;
    }

}
