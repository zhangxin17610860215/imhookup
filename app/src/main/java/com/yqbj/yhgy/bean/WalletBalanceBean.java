package com.yqbj.yhgy.bean;

public class WalletBalanceBean {

    /**
     * currency : 0
     * money : 0.0
     * unassignableTotalCurrency : 0
     * unassignableTotalMoney : 0.0
     */

    private int currency;
    private String money;
    private int unassignableTotalCurrency;
    private String unassignableTotalMoney;
    private String aliAccount;
    private String aliRealName;

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public String getAliRealName() {
        return aliRealName;
    }

    public void setAliRealName(String aliRealName) {
        this.aliRealName = aliRealName;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getUnassignableTotalCurrency() {
        return unassignableTotalCurrency;
    }

    public void setUnassignableTotalCurrency(int unassignableTotalCurrency) {
        this.unassignableTotalCurrency = unassignableTotalCurrency;
    }

    public String getUnassignableTotalMoney() {
        return unassignableTotalMoney;
    }

    public void setUnassignableTotalMoney(String unassignableTotalMoney) {
        this.unassignableTotalMoney = unassignableTotalMoney;
    }
}
