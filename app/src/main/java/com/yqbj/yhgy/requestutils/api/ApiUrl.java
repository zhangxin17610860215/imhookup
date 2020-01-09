package com.yqbj.yhgy.requestutils.api;
import com.yqbj.yhgy.utils.Preferences;

public class ApiUrl {

    //    public static String BASE_URL_HEAD = "http://";
//    public static String BASE_URL = "139.196.106.67";                       //测试服务器
//    public static String BASE_URL = "192.168.1.199";                      //服务端本地IP

    public static boolean isDebug = true;                        //是否是测试环境

    public static String BASE_URL_HEAD = "http://";
    public static String BASE_URL = "192.168.1.110:7075";
    public static String SERVERDOMAIN = Preferences.getServerDomain();

    public static String USER_LOGIN = BASE_URL_HEAD + BASE_URL + "/user/login";
    public static String USER_SIGNUP = BASE_URL_HEAD + BASE_URL + "/user/signup";
    public static String USER_GETVFCODE = BASE_URL_HEAD + BASE_URL + "/mobile/send/code";
    public static String USER_CHECKVFCODE = BASE_URL_HEAD + BASE_URL + "/mobile/check/code";
    public static String USER_RESETPWD = BASE_URL_HEAD + BASE_URL + "/user/reset/pwd";
    public static String USER_UPDATEINFO = BASE_URL_HEAD + SERVERDOMAIN + "/user/info/update";
    public static String USER_UPDATEPRIVACYSETTING = BASE_URL_HEAD + SERVERDOMAIN + "/user/privacy/setting";
    public static String LOCATION_UPDATE = BASE_URL_HEAD + SERVERDOMAIN + "/location/user/update";
    public static String HOME_INDEX = BASE_URL_HEAD + SERVERDOMAIN + "/home/index";
    public static String GETVIPLISTINFO = BASE_URL_HEAD + SERVERDOMAIN + "/price/list/vip";
    public static String GETUSERDETAILS = BASE_URL_HEAD + SERVERDOMAIN + "/user/details/self";
    public static String GETTARGETDETAILS = BASE_URL_HEAD + SERVERDOMAIN + "/user/details/target";
}
