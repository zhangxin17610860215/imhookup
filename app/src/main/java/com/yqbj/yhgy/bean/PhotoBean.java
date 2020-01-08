package com.yqbj.yhgy.bean;

import java.io.Serializable;

public class PhotoBean implements Serializable {
    private String photoUrl;
    private boolean burnAfterReading;       //阅后即焚
    private boolean redEnvelopePhotos;      //红包照片
    private boolean burnedDown;             //阅后即焚是否已焚毁
    private boolean redEnvelopePhotosPaid;  //红包照片是否支付过
    private boolean oneself;                //本人
    private String fee;                     //需要支付的金额

    public boolean isOneself() {
        return oneself;
    }

    public void setOneself(boolean oneself) {
        this.oneself = oneself;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public boolean isRedEnvelopePhotosPaid() {
        return redEnvelopePhotosPaid;
    }

    public void setRedEnvelopePhotosPaid(boolean redEnvelopePhotosPaid) {
        this.redEnvelopePhotosPaid = redEnvelopePhotosPaid;
    }

    public boolean isRedEnvelopePhotos() {
        return redEnvelopePhotos;
    }

    public void setRedEnvelopePhotos(boolean redEnvelopePhotos) {
        this.redEnvelopePhotos = redEnvelopePhotos;
    }

    public boolean isBurnedDown() {
        return burnedDown;
    }

    public void setBurnedDown(boolean burnedDown) {
        this.burnedDown = burnedDown;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isBurnAfterReading() {
        return burnAfterReading;
    }

    public void setBurnAfterReading(boolean burnAfterReading) {
        this.burnAfterReading = burnAfterReading;
    }
}
