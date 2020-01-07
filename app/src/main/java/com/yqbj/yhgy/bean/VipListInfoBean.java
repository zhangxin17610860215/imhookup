package com.yqbj.yhgy.bean;

public class VipListInfoBean {

    /**
     * id : 10019
     * value : 118/15
     * label : 半个月:236元/月
     * type : vip_price_list_android
     * sort : 10
     * description : vip价格表
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
