package com.yqbj.yhgy.bean;

public class UpLoadPhotoBean {
    private int type;
    private int statusFlag;
    private int redPacketFee;
    private String url;

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

    public int getRedPacketFee() {
        return redPacketFee;
    }

    public void setRedPacketFee(int redPacketFee) {
        this.redPacketFee = redPacketFee;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
