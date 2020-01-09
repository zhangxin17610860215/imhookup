package com.yqbj.yhgy.config;

import com.netease.nim.uikit.common.util.CityBean;
import com.yqbj.yhgy.MyApplication;
import com.yqbj.yhgy.utils.AppUtils;

import java.util.ArrayList;

public class Constants {

    //用于项目中有用到APP英文名称
    public static final String APP_NAME = "YueHui";

    public static final String APPSECRET = "jhk779c4pu93t68c";

    //第三方分享登录是用到的code值
    public static final int SHARE_REQUESTCODE = 2001;

    // DEBUG模式。影响log级别输出
    public static boolean DEBUG = true;//BuildConfig.DEBUG;

    public static boolean REFRESH = false;//控制我的页面是否可以刷新

    public static final String WX_LOGIN_API = "https://api.weixin.qq.com/sns/oauth2/access_token";//微信登录接口
    public static final String ERROR_REQUEST_FAILED_MESSAGE = "网络请求出现异常";//REQUEST_FAILED"; // 网络请求失败，出现onerror
    public static final String ERROR_REQUEST_EXCEPTION_MESSAGE = "服务返回数据异常";//REQUEST_EXCEPTION"; // 网络请求数据异常

    public static final int VERSIONCODE = AppUtils.getCurrentVersion(MyApplication.getInstance()).versionCode;//当前版本号

    public static final int SUCCESS_CODE = 200;//REQUEST_EXCEPTION"; // 网络请求成功的code值
    //自定义Token失效
    public static final String TOKEN_INCALID = "TokenInvalid";

    /**
     * 用户相关的属性
     */
    public static class USER_ATTRIBUTE {
        public static String SIGNUPTYPE = "";                   // 注册类型
        public static String WXUUID = "";                       // 微信uuid
        public static String OPENID = "";                       // 微信openid
        public static String WXTOKEN = "";                      // 微信wxtoken
        public static String WXHEADIMG = "";                    // 微信头像
        public static String WXNAME = "";                       // 微信昵称
        public static String PHONE = "";                        // 用户手机号
        public static String VFCODE = "";                       // 用户手机验证码
        public static String PSW = "";                          // 用户密码
        public static String GENDER = "";                       // 用户性别
    }

    /**
     * 城市数据
     * */
    public static ArrayList<CityBean> CITYBEANLIST = new ArrayList<>();

    /**
     * 职业数据
     * */
    public static ArrayList<CityBean> OCCUPATIONBEANLIST = new ArrayList<>();

    /**
     * 期望对象  数据源
     * */
    public static final String[] EXPECTLIST = new String[]{"看脸","有趣","土豪","关爱我","看感觉","无所谓"};

    /**
     * 错误码
     */
    public class RESPONSE_CODE {
        public static final int CODE_400 = 400;            // 操作失败
        public static final int CODE_401 = 401;            // 未知错误
        public static final int CODE_402 = 402;            // Header信息错误
        public static final int CODE_403 = 403;            // 服务太忙了，请稍后再试！
        public static final int CODE_404 = 404;            // 签名错误
        public static final int CODE_500 = 500;            // 服务器升级中
        public static final int CODE_501 = 501;            // 客户端升级中
        public static final int CODE_10001 = 10001;            // 注册失败
        public static final int CODE_10002 = 10002;            // 认证过期
        public static final int CODE_10003 = 10001;            // 用户token错误
        public static final int CODE_10004 = 10004;            // AppId错误
        public static final int CODE_10005 = 10005;            // 未找到用户
        public static final int CODE_10006 = 10006;            // 参数错误
        public static final int CODE_10007 = 10007;            // 用户已注册
        public static final int CODE_10008 = 10008;            // 用户名或者密码错误
        public static final int CODE_10009 = 10009;            // 微信登录失败
        public static final int CODE_10010 = 10010;            // 短信验证码类型错误
        public static final int CODE_10011 = 10011;            // 手机号未绑定
        public static final int CODE_10012 = 10012;            // 请求手机验证码失败
        public static final int CODE_10013 = 10013;            // 验证码错误
        public static final int CODE_10014 = 10014;            // 手机验证码超时
        public static final int CODE_10015 = 10015;            // 请稍后再请求验证码
        public static final int CODE_10016 = 10016;            // 手机号码不规范
        public static final int CODE_10017 = 10017;            // 手机号已注册
        public static final int CODE_10018 = 10018;            // 微信号已绑定
        public static final int CODE_20001 = 20001;            // 邀请码审核未通过
        public static final int CODE_20002 = 20002;            // 用户重复申请邀请码
        public static final int CODE_20003 = 20001;            // 邀请码正在审核中
        public static final int CODE_20004 = 20004;            // 邀请码输入错误
        public static final int CODE_20005 = 20005;            // 用户邀请码已通过
        public static final int CODE_20006 = 20006;            // 用户支付金额不足
        public static final int CODE_20007 = 20007;            // 支付配置异常
        public static final int CODE_20008 = 20008;            // 交易类型异常
        public static final int CODE_20009 = 20009;            // 交易重复提交
        public static final int CODE_20010 = 20010;            // 用户重复购买
        public static final int CODE_20011 = 20011;            // 用户资源为null
        public static final int CODE_20012 = 20012;            // 支付类型异常
        public static final int CODE_20013 = 20013;            // 币种类型异常
        public static final int CODE_20014 = 20014;            // 未解锁用户信息
        public static final int CODE_20015 = 20015;            // 用户信息不完善
    }

}
