package com.yqbj.yhgy.requestutils;

public abstract class RequestCallback {
    public abstract void onSuccess(Object object);
    public abstract void onFailed(String errMessage);
}

