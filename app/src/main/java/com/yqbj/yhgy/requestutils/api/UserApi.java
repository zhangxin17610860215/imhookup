package com.yqbj.yhgy.requestutils.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yqbj.yhgy.bean.HomeDataBean;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.bean.UserInfoBean;
import com.yqbj.yhgy.bean.VipListInfoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.BaseBean;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.RequestHelp;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.GsonHelper;
import com.yqbj.yhgy.utils.LogUtil;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (signupType.equals("2")){
            map.put("wxtoken",wxtoken);
            map.put("openid",openid);
            map.put("uuid",uuid);
        }
        RequestHelp.postRequest(ApiUrl.USER_SIGNUP, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "signup--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        UserBean userBean = JSON.parseObject(JSON.toJSONString(data.get("info")),UserBean.class);
                        userBean.setLoginType(signupType);
                        userBean.setPassword(password);
                        userBean.setWxToken(Constants.USER_ATTRIBUTE.WXTOKEN);
                        userBean.setWxOpenid(Constants.USER_ATTRIBUTE.OPENID);
                        userBean.setWxUuid(Constants.USER_ATTRIBUTE.WXUUID);
                        userBean.setHeadImag(Constants.USER_ATTRIBUTE.WXHEADIMG);
                        DemoCache.setAccount(userBean.getAccid());
                        Preferences.saveUserData(userBean);
                        callback.onSuccess(bean.getCode(),userBean);
                    } else {
                        callback.onFailed(bean.getMsg());
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
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
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
     * 校验验证码
     * */
    public static void checkVfCode(String smsCodeType,String mobile,String smsCode,
                              Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("smsCodeType",smsCodeType);
        map.put("mobile",mobile);
        map.put("smsCode",smsCode);
        RequestHelp.postRequest(ApiUrl.USER_CHECKVFCODE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "checkVfCode--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "checkVfCode--------->onError" + response.body());
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
        if (loginType.equals("1")){
            map.put("account",account);
            map.put("password",password);
        }else if (loginType.equals("2")){
            map.put("wxtoken",wxtoken);
            map.put("openid",openid);
            map.put("uuid",uuid);
        }
        RequestHelp.postRequest(ApiUrl.USER_LOGIN, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "login--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        UserBean userBean = JSON.parseObject(JSON.toJSONString(data.get("info")),UserBean.class);
                        userBean.setLoginType(loginType);
                        userBean.setPassword(password);
                        userBean.setWxToken(wxtoken);
                        userBean.setWxOpenid(openid);
                        userBean.setWxUuid(uuid);
                        userBean.setHeadImag(Constants.USER_ATTRIBUTE.WXHEADIMG);
                        DemoCache.setAccount(userBean.getAccid());
                        Preferences.saveUserData(userBean);

                        String longitude = Preferences.getLongitude();
                        String latitude = Preferences.getLatitude();
                        String city = Preferences.getCity();
                        if (StringUtil.isNotEmpty(longitude) && StringUtil.isNotEmpty(latitude)){
                            updateLocation(latitude,longitude,city,object);
                        }

                        callback.onSuccess(bean.getCode(),userBean);
                    } else {
                        callback.onFailed(bean.getMsg());
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

    /**
     * 更新经纬度
     * */
    public static void updateLocation(String latitude,String longitude,String city, Object object){
        Map<String,String> map = new HashMap<>();
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("region",city);
        RequestHelp.postRequest(ApiUrl.LOCATION_UPDATE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "updateLocation--------->onSuccess" + response.body());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "updateLocation--------->onError" + response.body());
            }
        });
    }

    /**
     * 重置密码
     * */
    public static void resetPwd(String smsCodeType,String mobile,String smsCode,String passsword,
                             Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("smsCodeType",smsCodeType);
        map.put("mobile",mobile);
        map.put("smsCode",smsCode);
        map.put("passsword",passsword);
        RequestHelp.postRequest(ApiUrl.USER_RESETPWD, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "resetPwd--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "resetPwd--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 更新用户信息
     * */
    public static void upDateUserInfo(String name,String headUrl,String birthday,String height,String weight,
                                   String job,String cities,String description,String datingPrograms,
                                   String desiredGoals,String hidecontactinfo,String qq,String wechat,
                             Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("headUrl",headUrl);
        map.put("birthday",birthday);
        map.put("height",height);
        map.put("weight",weight);
        map.put("job",job);
        map.put("cities",cities);
        map.put("description",description);
        map.put("datingPrograms",datingPrograms);
        map.put("desiredGoals",desiredGoals);
        map.put("hidecontactinfo",hidecontactinfo);
        map.put("qq",qq);
        map.put("wechat",wechat);
        RequestHelp.postRequest(ApiUrl.USER_UPDATEINFO, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "upDateUserInfo--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        String longitude = Preferences.getLongitude();
                        String latitude = Preferences.getLatitude();
                        String city = Preferences.getCity();
                        if (StringUtil.isNotEmpty(longitude) && StringUtil.isNotEmpty(latitude)){
                            updateLocation(latitude,longitude,city,object);
                        }
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "upDateUserInfo--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取首页数据
     * */
    public static void index(String gender,String region,String onlinePriority,
                             String queryType,String pageNum,String pageSize,
                                   Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("gender",gender);
        map.put("region",region);
        map.put("onlinePriority",onlinePriority);
        map.put("queryType",queryType);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        RequestHelp.postRequest(ApiUrl.HOME_INDEX, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "index--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        HomeDataBean homeDataBean = JSON.parseObject(JSON.toJSONString(data.get("PageResult")),HomeDataBean.class);
                        callback.onSuccess(bean.getCode(),homeDataBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "index--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 更新隐私设置
     * */
    public static void upPrivacySetting(String hidelocation,String hideonline,String privacystate,
                             String viewphotofee,String invisible,String hidecontactinfo,
                                   Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("hidelocation",hidelocation);
        map.put("hideonline",hideonline);
        map.put("privacystate",privacystate);
        if (privacystate.equals("2")){
            map.put("viewphotofee",viewphotofee);
            map.put("currencyType","2");
        }
        map.put("invisible",invisible);
        map.put("hidecontactinfo",hidecontactinfo);
        RequestHelp.postRequest(ApiUrl.USER_UPDATEPRIVACYSETTING, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "upPrivacySetting--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "upPrivacySetting--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取VIP价格列表
     * */
    public static void getVipListInfo(Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        RequestHelp.getRequest(ApiUrl.GETVIPLISTINFO, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getVipListInfo--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        List<VipListInfoBean> list = JSON.parseObject(JSON.toJSONString(data.get("priceList")), new TypeReference<ArrayList<VipListInfoBean>>(){});
                        callback.onSuccess(bean.getCode(),list);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getVipListInfo--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取用户详情
     * */
    public static void getUserDetails(Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        RequestHelp.getRequest(ApiUrl.GETUSERDETAILS, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getUserDetails--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        UserInfoBean userInfoBean = new UserInfoBean();
                        UserInfoBean.UserDetailsBean userDetails = JSON.parseObject(JSON.toJSONString(data.get("userDetails")), UserInfoBean.UserDetailsBean.class);
                        UserInfoBean.WalletBean wallet = JSON.parseObject(JSON.toJSONString(data.get("wallet")), UserInfoBean.WalletBean.class);
                        UserInfoBean.ContactInfoBean contactInfo = JSON.parseObject(JSON.toJSONString(data.get("contactInfo")), UserInfoBean.ContactInfoBean.class);
                        UserInfoBean.ConfigBean config = JSON.parseObject(JSON.toJSONString(data.get("config")), UserInfoBean.ConfigBean.class);
                        List<UserInfoBean.PhotoAlbumBean> photoAlbum = JSON.parseObject(JSON.toJSONString(data.get("photoAlbum")), new TypeReference<ArrayList<UserInfoBean.PhotoAlbumBean>>(){});
                        userInfoBean.setConfig(config);
                        userInfoBean.setContactInfo(contactInfo);
                        userInfoBean.setPhotoAlbum(photoAlbum);
                        userInfoBean.setWallet(wallet);
                        userInfoBean.setUserDetails(userDetails);
                        callback.onSuccess(bean.getCode(),userInfoBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getUserDetails--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

}
