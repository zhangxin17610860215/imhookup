package com.yqbj.yhgy.requestutils.api;

public class ApiUrl {

    //    public static String BASE_URL_HEAD = "http://";
//    public static String BASE_URL = "139.196.106.67";                       //测试服务器
//    public static String BASE_URL = "192.168.1.199";                      //服务端本地IP

    public static boolean isDebug = true;                        //是否是测试环境


    public static String BASE_URL_HEAD = "http://";
    public static String BASE_URL = "192.168.1.112:7075";

    public static String USER_LOGIN = BASE_URL_HEAD + BASE_URL + "/interactive/user/login";
    public static String USER_SIGNUP = BASE_URL_HEAD + BASE_URL + "/domain/user/signup";
}
