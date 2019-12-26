package com.yqbj.yhgy.bean;

import java.io.Serializable;

public class PhotoBean implements Serializable {
    private String photoUrl;
    private boolean burnAfterReading;
    private boolean redEnvelopePhotos;
    private boolean burnedDown;
    private boolean redEnvelopePhotosPaid;

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
