package com.yqbj.yhgy.requestutils.api;

public class ApiUrl {

    //    public static String BASE_URL_HEAD = "http://";
//    public static String BASE_URL = "139.196.106.67";                       //测试服务器
//    public static String BASE_URL = "192.168.1.199";                      //服务端本地IP

    public static boolean isDebug = true;                        //是否是测试环境


    public static String BASE_URL_HEAD;
    public static String BASE_URL;

    public static String CHECK_VERSION;
    public static String OVERALL_GET_KEY;
    static {
        if (isDebug){
            BASE_URL_HEAD = "http://";
            BASE_URL = "139.196.106.67";
            OVERALL_GET_KEY = BASE_URL_HEAD + BASE_URL + "/IM_key_server/app/im/access";
            CHECK_VERSION = BASE_URL_HEAD + BASE_URL + "/IM_key_server/app/version";
        }else {
            BASE_URL_HEAD = "https://";
            BASE_URL = "im.wulewan.cn";
            CHECK_VERSION = BASE_URL_HEAD + "gate.wulewan.cn" + "/app/version";
            OVERALL_GET_KEY = BASE_URL_HEAD + "gate.wulewan.cn" + "/app/im/access";
        }
    }

    public static String USER_LOGIN = BASE_URL_HEAD + BASE_URL + "/interactive/user/login";
}
