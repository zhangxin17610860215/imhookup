package com.yqbj.yhgy.requestutils.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yqbj.yhgy.bean.CurrencyPriceBean;
import com.yqbj.yhgy.bean.EvaluateDataBean;
import com.yqbj.yhgy.bean.HomeDataBean;
import com.yqbj.yhgy.bean.MyAlbumBean;
import com.yqbj.yhgy.bean.MyLikeBean;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.bean.UserInfoBean;
import com.yqbj.yhgy.bean.VipListInfoBean;
import com.yqbj.yhgy.bean.WalletBalanceBean;
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
import java.util.Set;

import static com.yqbj.yhgy.config.Constants.ERROR_REQUEST_EXCEPTION_MESSAGE;
import static com.yqbj.yhgy.config.Constants.ERROR_REQUEST_FAILED_MESSAGE;

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
                        Preferences.saveServerDomain(userBean.getServerDomain());
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                        Preferences.saveServerDomain(userBean.getServerDomain());
                        LogUtil.e(TAG, "ServerDomain--------->" + Preferences.getServerDomain());
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
                            updateLocation(latitude, longitude, city, object, new RequestCallback() {
                                @Override
                                public void onSuccess(int code, Object object) {
                                    callback.onSuccess(code,bean);
                                }

                                @Override
                                public void onFailed(String errMessage) {
                                    callback.onSuccess(bean.getCode(),bean);
                                }
                            });
                        }
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
    public static void updateLocation(String latitude,String longitude,String city, Object object,
                                      RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("region",city);
        RequestHelp.postRequest(ApiUrl.LOCATION_UPDATE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "updateLocation--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "updateLocation--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                            updateLocation(latitude, longitude, city, object, new RequestCallback() {
                                @Override
                                public void onSuccess(int code, Object object) {
                                    callback.onSuccess(code,bean);
                                }

                                @Override
                                public void onFailed(String errMessage) {
                                    callback.onSuccess(bean.getCode(),bean);
                                }
                            });
                        }
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
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

    /**
     * 获取其他用户详情
     * */
    public static void getTargetDetails(String userAccid, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("userAccid",userAccid);
        RequestHelp.postRequest(ApiUrl.GETTARGETDETAILS, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getTargetDetails--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        UserInfoBean userInfoBean = new UserInfoBean();
                        UserInfoBean.UserDetailsBean userDetails = JSON.parseObject(JSON.toJSONString(data.get("userDetails")), UserInfoBean.UserDetailsBean.class);
                        UserInfoBean.ContactInfoBean contactInfo = JSON.parseObject(JSON.toJSONString(data.get("contactInfo")), UserInfoBean.ContactInfoBean.class);
                        UserInfoBean.ConfigBean config = JSON.parseObject(JSON.toJSONString(data.get("config")), UserInfoBean.ConfigBean.class);
                        List<UserInfoBean.PhotoAlbumBean> photoAlbum = JSON.parseObject(JSON.toJSONString(data.get("photoAlbum")), new TypeReference<ArrayList<UserInfoBean.PhotoAlbumBean>>(){});
                        userInfoBean.setConfig(config);
                        userInfoBean.setContactInfo(contactInfo);
                        userInfoBean.setPhotoAlbum(photoAlbum);
                        userInfoBean.setUserDetails(userDetails);
                        callback.onSuccess(bean.getCode(),userInfoBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getTargetDetails--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取虚拟币价格列表
     * */
    public static void getCurrencyPriceList(Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        RequestHelp.getRequest(ApiUrl.GETCURRENCYPRICELIST, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getCurrencyPriceList--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        List<CurrencyPriceBean> priceList = JSON.parseObject(JSON.toJSONString(data.get("priceList")), new TypeReference<ArrayList<CurrencyPriceBean>>(){});
                        callback.onSuccess(bean.getCode(),priceList);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getCurrencyPriceList--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取评价数据
     * */
    public static void getEvalualeData(String targetId, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("targetId",targetId);
        RequestHelp.postRequest(ApiUrl.GETEVALUALEDATA, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getEvalualeData--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        String gender = JSON.toJSONString(data.get("gender"));
                        List<EvaluateDataBean> dataList = JSON.parseObject(JSON.toJSONString(data.get("estimateInfo")), new TypeReference<ArrayList<EvaluateDataBean>>(){});
                        List<EvaluateDataBean> list = new ArrayList<>();
                        for (EvaluateDataBean dictData : dataList){
                            if (StringUtil.isNotEmpty(dictData.getLabel()) && dictData.getLabel().contains("/")){
                                String[] split = dictData.getLabel().split("/");
                                dictData.setLabel(gender.equals("1") ? split[0] : split[1]);
                            }else {
                                dictData.setLabel("");
                            }
                            dictData.setId(dictData.getId());
                            dictData.setTotalValue(dictData.getTotalValue());
                            dictData.setSort(dictData.getSort());
                            list.add(dictData);
                        }
                        callback.onSuccess(bean.getCode(),list);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getEvalualeData--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 评价用户
     * */
    public static void evalualeUser(String targetId, String estimateData, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("targetId",targetId);
        map.put("estimateData",estimateData);
        RequestHelp.postRequest(ApiUrl.EVALUALEUSER, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "evalualeUser--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "evalualeUser--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 操作喜欢（收藏）
     * */
    public static void operatorEnjoy(String targetId, int operate, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("targetId",targetId);
        map.put("operate",operate+"");
        RequestHelp.postRequest(ApiUrl.OPERATORENJOY, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "operatorEnjoy--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "operatorEnjoy--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 操作黑名单
     * */
    public static void operatorBlackList(String targetId, int operate, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("targetId",targetId);
        map.put("operate",operate+"");
        RequestHelp.postRequest(ApiUrl.OPERATORBLACKLIST, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "operatorBlackList--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "operatorBlackList--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取喜欢列表
     * */
    public static void getEnjoyList(int pageNum, int pageSize, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        RequestHelp.postRequest(ApiUrl.GETENJOYLIST, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getEnjoyList--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        MyLikeBean myLikeBean = JSON.parseObject(JSON.toJSONString(data.get("PageResult")), MyLikeBean.class);
                        callback.onSuccess(bean.getCode(),myLikeBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getEnjoyList--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取黑名单列表
     * */
    public static void getBlackList(int pageNum, int pageSize, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        RequestHelp.postRequest(ApiUrl.GETBLACKLIST, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getBlackList--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        MyLikeBean myLikeBean = JSON.parseObject(JSON.toJSONString(data.get("PageResult")), MyLikeBean.class);
                        callback.onSuccess(bean.getCode(),myLikeBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getBlackList--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取账户余额
     * */
    public static void getBalance(Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        RequestHelp.getRequest(ApiUrl.GETBALANCE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getBalance--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        WalletBalanceBean walletBalanceBean = JSON.parseObject(JSON.toJSONString(data.get("wallet")), WalletBalanceBean.class);
                        callback.onSuccess(bean.getCode(),walletBalanceBean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getBalance--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 绑定支付宝账户
     * */
    public static void bindALiAccount(String aliAccount, String aliRealName, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("aliAccount",aliAccount);
        map.put("aliRealName",aliRealName);
        RequestHelp.postRequest(ApiUrl.BINDALIACCOUNT, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "bindALiAccount--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "bindALiAccount--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 网关服获取短信验证码
     * */
    public static void getMobileCode(String smsCodeType, String mobile, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("smsCodeType",smsCodeType);
        map.put("mobile",mobile);
        RequestHelp.postRequest(ApiUrl.GETMOBILECODE, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getMobileCode--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getMobileCode--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 修改手机号
     * */
    public static void changeAccount(String smsCodeType, String mobile, String smsCode, String passsword, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("smsCodeType",smsCodeType);
        map.put("mobile",mobile);
        map.put("smsCode",smsCode);
        map.put("passsword",passsword);
        RequestHelp.postRequest(ApiUrl.CHANGEACCOUNT, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "changeAccount--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "changeAccount--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 重置密码
     * */
    public static void resetPassword(String oldPasssword, String passsword, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("oldPasssword",oldPasssword);
        map.put("passsword",passsword);
        RequestHelp.postRequest(ApiUrl.RESETPASSWORD, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "resetPassword--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "resetPassword--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 上传照片
     * */
    public static void upLoadPhoto(String multimediaeInfo, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("multimediaeInfo",multimediaeInfo);
        RequestHelp.postRequest(ApiUrl.UPLOADPHOTO, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "upLoadPhoto--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "upLoadPhoto--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 更新修改照片
     * */
    public static void updatePhoto(String multimediaeInfo, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("multimediaeInfo",multimediaeInfo);
        RequestHelp.postRequest(ApiUrl.UPDATEPHOTO, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "updatePhoto--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "updatePhoto--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 删除照片
     * */
    public static void deletePhoto(String multimediaeIds, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("multimediaeIds",multimediaeIds);
        RequestHelp.postRequest(ApiUrl.DELETEPHOTO, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "deletePhoto--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "deletePhoto--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取我的相册
     * */
    public static void getMyAlbum(Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        RequestHelp.getRequest(ApiUrl.GETMYALBUM, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getMyAlbum--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
                        Map<String, Object> data = bean.getData();
                        String photoAlbum = JSON.toJSONString(data.get("photoAlbum"));
                        List<MyAlbumBean> albumBeanList;
                        if (photoAlbum.equals("\"[]\"")){
                            albumBeanList = new ArrayList<>();
                        }else {
                            albumBeanList = JSON.parseObject(photoAlbum, new TypeReference<ArrayList<MyAlbumBean>>(){});
                        }
                        callback.onSuccess(bean.getCode(),albumBeanList);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getMyAlbum--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

    /**
     * 获取活体人脸认证TOKEN
     * */
    public static void getVfToken(String useType, String multimediaeInfo, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("useType",useType);
        if (useType.equals("2")){
            map.put("multimediaeInfo",multimediaeInfo);
        }
        RequestHelp.postRequest(ApiUrl.GETVFTOKEN, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getVfToken--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getCode() == Constants.SUCCESS_CODE){
//                        Map<String, Object> data = bean.getData();
//                        String photoAlbum = JSON.toJSONString(data.get("photoAlbum"));
//                        List<MyAlbumBean> albumBeanList;
//                        if (photoAlbum.equals("\"[]\"")){
//                            albumBeanList = new ArrayList<>();
//                        }else {
//                            albumBeanList = JSON.parseObject(photoAlbum, new TypeReference<ArrayList<MyAlbumBean>>(){});
//                        }
                        callback.onSuccess(bean.getCode(),bean);
                    } else {
                        callback.onFailed(bean.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(ERROR_REQUEST_FAILED_MESSAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getVfToken--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);
            }
        });
    }

}
