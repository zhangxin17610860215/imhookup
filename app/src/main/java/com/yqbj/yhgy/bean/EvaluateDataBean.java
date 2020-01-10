package com.yqbj.yhgy.bean;

public class EvaluateDataBean {

    /**
     * id : 1
     * label : 礼貌/友好
     * totalValue : 0
     * sort : 10
     */

    private String id;
    private String label;
    private int totalValue;
    private int sort;
    private boolean onClick;

    public boolean isOnClick() {
        return onClick;
    }

    public void setOnClick(boolean onClick) {
        this.onClick = onClick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
