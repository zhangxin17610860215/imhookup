package com.yqbj.yhgy.bean;

public class MyAlbumBean {

    /**
     * id : 1057
     * type : 1
     * statusFlag : 1
     * labelFlag : 0
     * payFlag : 1
     * currencyType : 2
     * fee : 3
     * url : https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc5MTQ0ODkzMDAxXzhhYzlhYTI3LWJlOWYtNDRiMC04ZGJmLTczYzAwZDZjMzcxZg==
     * checkFlag : 1
     */

    private int id;
    private int type;
    private int statusFlag;
    private int labelFlag;
    private int payFlag;
    private int currencyType;
    private int fee;
    private String url;
    private int checkFlag;
    private int paidFlag;
    private int setOnFire;

    public int getPaidFlag() {
        return paidFlag;
    }

    public void setPaidFlag(int paidFlag) {
        this.paidFlag = paidFlag;
    }

    public int getSetOnFire() {
        return setOnFire;
    }

    public void setSetOnFire(int setOnFire) {
        this.setOnFire = setOnFire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public int getLabelFlag() {
        return labelFlag;
    }

    public void setLabelFlag(int labelFlag) {
        this.labelFlag = labelFlag;
    }

    public int getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(int payFlag) {
        this.payFlag = payFlag;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(int checkFlag) {
        this.checkFlag = checkFlag;
    }
}
