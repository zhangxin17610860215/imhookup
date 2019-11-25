package com.yuanqi.hangzhou.imhookup.requestutils;

public abstract class RequestCallback {
    public abstract void onSuccess(Object object);
    public abstract void onFailed(String errMessage);
}

