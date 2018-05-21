package com.test.kingofclick;

/**
 * Created by Staff2 on 20.05.2018.
 */

public enum Consts {
    PASSIVE_COUNT("passiveCount"),
    COUNT_PER_CLICK("countPerClick"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    COUNT("count")
    ;


    private String s;
    Consts(String s) {
        this.s=s;
    }


    @Override
    public String toString() {
        return s;
    }
}
