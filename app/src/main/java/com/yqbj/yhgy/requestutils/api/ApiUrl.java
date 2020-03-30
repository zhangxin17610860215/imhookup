package com.yqbj.yhgy.requestutils.api;
import com.yqbj.yhgy.utils.Preferences;

public class ApiUrl {

    //    public static String BASE_URL_HEAD = "http://";
//    public static String BASE_URL = "139.196.106.67";                       //测试服务器
//    public static String BASE_URL = "192.168.1.199";                      //服务端本地IP

    public static String BASE_URL_HEAD = "http://";
    public static String BASE_URL = "139.196.106.67:7075";

    public static String USER_LOGIN = BASE_URL_HEAD + BASE_URL + "/user/login";
    public static String USER_SIGNUP = BASE_URL_HEAD + BASE_URL + "/user/signup";
    public static String USER_GETVFCODE = BASE_URL_HEAD + BASE_URL + "/mobile/send/code";
    public static String USER_CHECKVFCODE = BASE_URL_HEAD + BASE_URL + "/mobile/check/code";
    public static String USER_RESETPWD = BASE_URL_HEAD + BASE_URL + "/user/reset/pwd";
    public static String USER_UPDATEINFO = BASE_URL_HEAD + "%s" + "/user/info/update";
    public static String USER_UPDATEPRIVACYSETTING = BASE_URL_HEAD + "%s" + "/user/privacy/setting";
    public static String LOCATION_UPDATE = BASE_URL_HEAD + "%s" + "/location/user/update";
    public static String HOME_INDEX = BASE_URL_HEAD + "%s" + "/home/index";
    public static String HOME_SEARCH = BASE_URL_HEAD + "%s" + "/home/search";
    public static String GETVIPLISTINFO = BASE_URL_HEAD + "%s" + "/price/list/vip";
    public static String GETUSERDETAILS = BASE_URL_HEAD + "%s" + "/user/details/self";
    public static String GETTARGETDETAILS = BASE_URL_HEAD + "%s" + "/user/details/target";
    public static String GETCURRENCYPRICELIST = BASE_URL_HEAD + "%s" + "/price/list/recharge";
    public static String GETEVALUALEDATA = BASE_URL_HEAD + "%s" + "/estimate/user/info";
    public static String EVALUALEUSER = BASE_URL_HEAD + "%s" + "/estimate/user";
    public static String OPERATORENJOY = BASE_URL_HEAD + "%s" + "/tendency/operator/enjoy";
    public static String GETENJOYLIST = BASE_URL_HEAD + "%s" + "/tendency/enjoy/list";
    public static String OPERATORBLACKLIST= BASE_URL_HEAD + "%s" + "/tendency/operator/blacklist";
    public static String GETBLACKLIST= BASE_URL_HEAD + "%s" + "/tendency/black/list";
    public static String GETBALANCE= BASE_URL_HEAD + "%s" + "/wallet/details";
    public static String BINDALIACCOUNT= BASE_URL_HEAD + "%s" + "/wallet/binding/aliaccount";
    public static String GETMOBILECODE= BASE_URL_HEAD + "%s" + "/mobile/send/code";
    public static String CHANGEACCOUNT= BASE_URL_HEAD + "%s" + "/user/change/account";
    public static String RESETPASSWORD= BASE_URL_HEAD + "%s" + "/user/reset/password";
    public static String UPLOADPHOTO= BASE_URL_HEAD + "%s" + "/multimedia/info/upload";
    public static String GETMYALBUM= BASE_URL_HEAD + "%s" + "/multimedia/self/photo/album";
    public static String UPDATEPHOTO= BASE_URL_HEAD + "%s" + "/multimedia/info/update";
    public static String DELETEPHOTO= BASE_URL_HEAD + "%s" + "/multimedia/delete";
    public static String GETVFTOKEN= BASE_URL_HEAD + "%s" + "/authentication/real/person/token";
    public static String AUTHENTICATIONPASS= BASE_URL_HEAD + "%s" + "/authentication/real/person/verify";
    public static String GODDESSPASS= BASE_URL_HEAD + "%s" + "/authentication/goddess";
    public static String UNLOCKALBUM= BASE_URL_HEAD + "%s" + "/trade/unlock/photo/album";
    public static String CHECKSHOWALBUM= BASE_URL_HEAD + "%s" + "/multimedia/check/show/photoalbum";
    public static String UNLOCKUSERINFO= BASE_URL_HEAD + "%s" + "/trade/unlock/user/info";
    public static String CHARGEPHOTO= BASE_URL_HEAD + "%s" + "/trade/charge/photo";
    public static String RECHARGE= BASE_URL_HEAD + "%s" + "/trade/user/recharge";
    public static String WITHDRAWDEPOSIT= BASE_URL_HEAD + "%s" + "/trade/withdraw/deposit";
    public static String SENDREDPACKET= BASE_URL_HEAD + "%s" + "/redpacket/send";
    public static String GETREDPACKET= BASE_URL_HEAD + "%s" + "/redpacket/get";
    public static String GETREDPACKETDETAILS= BASE_URL_HEAD + "%s" + "/redpacket/details";
    public static String GETORDERLIST= BASE_URL_HEAD + "%s" + "/wallet/trade/order/list";
}
