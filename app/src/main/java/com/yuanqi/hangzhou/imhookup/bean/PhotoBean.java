package com.yuanqi.hangzhou.imhookup.bean;

import java.io.Serializable;

public class PhotoBean implements Serializable {
    private String photoUrl;
    private boolean burnAfterReading;

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
