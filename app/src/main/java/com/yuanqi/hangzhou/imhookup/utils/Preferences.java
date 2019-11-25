package com.yuanqi.hangzhou.imhookup.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.yuanqi.hangzhou.imhookup.config.Constants;

/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_USER_PAY_PASS_SET = "payPassSet";

    public static void saveUserPayPassSet(boolean flag){
        if(TextUtils.isEmpty(NimUIKit.getAccount())){
            return;
        }else{
            saveBoolean(KEY_USER_PAY_PASS_SET +"_"+ NimUIKit.getAccount() ,flag);
        }

    }

    public static boolean getUserPayPassSet(){
        if(TextUtils.isEmpty(NimUIKit.getAccount())){
            return false;
        }else{
            return getBoolean(KEY_USER_PAY_PASS_SET + "_" + NimUIKit.getAccount());
        }


    }

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static void saveBoolean(String key,boolean flag){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key,flag);
        editor.commit();
    }

    private static boolean getBoolean(String key){
        return getSharedPreferences().getBoolean(key,false);
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }
}
