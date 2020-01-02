package com.yqbj.yhgy.requestutils;

public abstract class RequestCallback {
    public abstract void onSuccess(int code, Object object);
    public abstract void onFailed(String errMessage);
}

