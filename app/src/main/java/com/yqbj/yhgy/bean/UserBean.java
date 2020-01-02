package com.yqbj.yhgy.bean;

public class UserBean {

    /**
     * accid : 144742596
     * account : 17610860215
     * nikename : 😂 😚 😌 xgv
     * yunxinToken : 9d97434a1413dd685584ffe49d412465
     * userToken : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJcdUQ4M0RcdURFMDIgXHVEODNEXHVERTFBIFx1RDgzRFx1REUwQyB4Z3YiLCJuYW1lIjoiXHVEODNEXHVERTAyIFx1RDgzRFx1REUxQSBcdUQ4M0RcdURFMEMgeGd2IiwibW9iaWxlIjoiMTc2MTA4NjAyMTUiLCJpc3MiOiJzZC1sb2dpbi1zZXJ2ZXIiLCJhY2NpZCI6IjE0NDc0MjU5NiIsImV4cCI6MTU3NzgwMDM2MiwiaWF0IjoxNTc3NzkzMTYyfQ.ZeA9C4YpqLrk7wcrsFGHE4kufulsX7ibWM5DtKbN7Wsrq1CD0Ru2_MYHjQ0E-wvFJdrO3c-Qi7D9GboYu7WVyA
     * serverDomain : 192.168.1.114:8085
     * gender : 1
     * certification : 0
     * labeltype : 0
     */

    private String accid;
    private String account;
    private String nikename;
    private String yunxinToken;
    private String userToken;
    private String serverDomain;
    private String loginType;
    private String password;
    private String wxToken;
    private String wxUuid;
    private String wxOpenid;
    private String headImag;
    private int gender;
    private int certification;
    private int labeltype;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    public String getWxUuid() {
        return wxUuid;
    }

    public void setWxUuid(String wxUuid) {
        this.wxUuid = wxUuid;
    }

    public String getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(String wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public String getHeadImag() {
        return headImag;
    }

    public void setHeadImag(String headImag) {
        this.headImag = headImag;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNikename() {
        return nikename;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }

    public String getYunxinToken() {
        return yunxinToken;
    }

    public void setYunxinToken(String yunxinToken) {
        this.yunxinToken = yunxinToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getCertification() {
        return certification;
    }

    public void setCertification(int certification) {
        this.certification = certification;
    }

    public int getLabeltype() {
        return labeltype;
    }

    public void setLabeltype(int labeltype) {
        this.labeltype = labeltype;
    }
}
