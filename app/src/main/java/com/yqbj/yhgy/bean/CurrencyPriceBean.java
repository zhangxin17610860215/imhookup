package com.yqbj.yhgy.bean;

public class CurrencyPriceBean {

    /**
     * id : 10035
     * value : 60/6
     * label : 60个_￥6
     * type : recharge_price_list_android
     * sort : 10
     * description : android充值价格表
     */

    private String id;
    private String value;
    private String label;
    private String type;
    private int sort;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
