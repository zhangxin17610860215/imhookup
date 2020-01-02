package com.yqbj.yhgy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.SPUtils;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.config.Constants;

/**
 * Created by zhnagxin on 2019/10/13.
 */
public class Preferences {

    private static final String KEY_USER_ACCID = "accid";
    private static final String KEY_YUNXIN_TOKEN = "yunxinToken";
    private static final String KEY_USER_PAY_PASS_SET = "payPassSet";
    private static final String KEY_LOGINYPE = "loginType";            // 用户的loginType(登录方式)
    private static final String KEY_USERACCOUNT = "account";           // 用户的account(手机号)
    private static final String KEY_PSW = "psw";                       // 用户的psw(密码)
    private static final String KEY_USERTOKEN = "userToken";           // 用户Token
    private static final String KEY_WXUUID = "wxUuid";                 // 微信uuid
    private static final String KEY_OPENID = "wxOpenId";               // 微信openid
    private static final String KEY_WXTOKEN = "wxToken";               // 微信wxtoken
    private static final String KEY_WXHEADIMG = "wxHeadImg";           // 微信头像
    private static final String KEY_NIKENAME = "nikename";             // 昵称
    private static final String KEY_GENDER = "gender";                 // 用户性别
    private static final String KEY_LABELTYPE = "labeltype";           // 标签类型（0普通  1女神）
    private static final String KEY_CERTIFICATION = "certification";   // 是否真人认证（1有图片ID表示通过   0未真人认证）
    private static final String KEY_SERVERDOMAIN = "serverDomain";     // 域名

    private static void saveUserPayPassSet(boolean flag){
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

    public static void saveUserData(UserBean userBean) {
        if (null == userBean){
            return;
        }
        saveString(KEY_USER_ACCID, userBean.getAccid());
        saveString(KEY_YUNXIN_TOKEN, userBean.getYunxinToken());
        saveString(KEY_LOGINYPE, userBean.getLoginType());
        saveString(KEY_USERACCOUNT, userBean.getAccount());
        saveString(KEY_PSW, userBean.getPassword());
        saveString(KEY_USERTOKEN, userBean.getUserToken());
        saveString(KEY_WXUUID, userBean.getWxUuid());
        saveString(KEY_OPENID, userBean.getWxOpenid());
        saveString(KEY_WXTOKEN, userBean.getWxToken());
        saveString(KEY_WXHEADIMG, userBean.getHeadImag());
        saveString(KEY_NIKENAME, userBean.getNikename());
        saveString(KEY_GENDER, userBean.getGender() + "");
        saveString(KEY_LABELTYPE, userBean.getLabeltype() + "");
        saveString(KEY_CERTIFICATION, userBean.getCertification() + "");
        saveString(KEY_SERVERDOMAIN, userBean.getServerDomain());
    }

    public static String getServerDomain() {
        return getString(KEY_SERVERDOMAIN);
    }

    public static String getUserAccId() {
        return getString(KEY_USER_ACCID);
    }

    public static String getYunxinToken() {
        return getString(KEY_YUNXIN_TOKEN);
    }

    public static String getLoginType() {
        return getString(KEY_LOGINYPE);
    }

    public static String getAccount() {
        return getString(KEY_USERACCOUNT);
    }

    public static String getPassword() {
        return getString(KEY_PSW);
    }

    public static String getUserToken() {
        return getString(KEY_USERTOKEN);
    }

    public static String getWxUuid() {
        return getString(KEY_WXUUID);
    }

    public static String getWxOpenid() {
        return getString(KEY_OPENID);
    }

    public static String getWxToken() {
        return getString(KEY_WXTOKEN);
    }

    public static String getHeadImag() {
        return getString(KEY_WXHEADIMG);
    }

    public static String getNikename() {
        return getString(KEY_NIKENAME);
    }

    public static String getGender() {
        return getString(KEY_GENDER);
    }

    public static String getLabeltype() {
        return getString(KEY_LABELTYPE);
    }

    public static String getCertification() {
        return getString(KEY_CERTIFICATION);
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
        return getSharedPreferences().getString(key, "");
    }

    static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

}
