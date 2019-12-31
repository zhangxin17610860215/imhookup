package com.yqbj.yhgy.requestutils.api;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.BaseBean;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.RequestHelp;
import com.yqbj.yhgy.utils.GsonHelper;
import com.yqbj.yhgy.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import static com.yqbj.yhgy.config.Constants.ERROR_REQUEST_EXCEPTION_MESSAGE;

public class UserApi {

    private final static String TAG = "UserApi";

    /**
     * 注册
     * */
    public static void signup(String signupType,String mobile,String smsCode,
                              String password,String gender,String wxtoken,
                              String openid,String uuid,
                              Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("signupType",signupType);
        map.put("mobile",mobile);
        map.put("smsCode",smsCode);
        map.put("password",password);
        map.put("gender",gender);
        map.put("wxtoken",wxtoken);
        map.put("openid",openid);
        map.put("uuid",uuid);
        RequestHelp.postRequest(ApiUrl.USER_SIGNUP, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "signup--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getStatusCode() == Constants.SUCCESS_CODE){
                        UserBean userBean = GsonHelper.getSingleton().fromJson(bean.getData(), UserBean.class);
                        callback.onSuccess(userBean);
                    } else {
                        callback.onFailed(bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "signup--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取验证码
     * */
    public static void getVfCode(String smsCodeType,String mobile,
                              Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("smsCodeType",smsCodeType);
        map.put("mobile",mobile);
        RequestHelp.postRequest(ApiUrl.USER_GETVFCODE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getVfCode--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getStatusCode() == Constants.SUCCESS_CODE){
//                        TeamAllocationPriceBean priceBean = GsonHelper.getSingleton().fromJson(bean.getData(), TeamAllocationPriceBean.class);
//                        callback.onSuccess(bean.getStatusCode(),priceBean);
                    } else {
                        callback.onFailed(bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getVfCode--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 登录
     * */
    public static void login(String loginType,String account,String password,
                              String wxtoken,String openid,String uuid,
                              Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("loginType",loginType);
        map.put("account",account);
        map.put("password",password);
        map.put("wxtoken",wxtoken);
        map.put("openid",openid);
        map.put("uuid",uuid);
        RequestHelp.postRequest(ApiUrl.USER_LOGIN, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "login--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getStatusCode() == Constants.SUCCESS_CODE){
//                        TeamAllocationPriceBean priceBean = GsonHelper.getSingleton().fromJson(bean.getData(), TeamAllocationPriceBean.class);
//                        callback.onSuccess(bean.getStatusCode(),priceBean);
                    } else {
                        callback.onFailed(bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "login--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }
}
