package com.yqbj.yhgy.bean;

import java.util.List;

public class RedpacketDetailsBean {

    /**
     * id : 202002201733361447425961048
     * currencyType : 2
     * amount : 0.01
     * payerName : ? ? ? xgv
     * payerAmount : 0.0
     * status : 1
     * remark : 发送红包
     * createTime : 2020-02-20 17:33:36
     * getRedpacketDetailsList : []
     */

    private String id;
    private int currencyType;
    private String amount;
    private String payerName;
    private String payerAmount;
    private int status;
    private String remark;
    private String createTime;
    private List<?> getRedpacketDetailsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerAmount() {
        return payerAmount;
    }

    public void setPayerAmount(String payerAmount) {
        this.payerAmount = payerAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<?> getGetRedpacketDetailsList() {
        return getRedpacketDetailsList;
    }

    public void setGetRedpacketDetailsList(List<?> getRedpacketDetailsList) {
        this.getRedpacketDetailsList = getRedpacketDetailsList;
    }
}
