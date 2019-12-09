package com.yuanqi.hangzhou.imhookup.config;

import com.yuanqi.hangzhou.imhookup.MyApplication;
import com.yuanqi.hangzhou.imhookup.utils.AppUtils;

public class Constants {

    //用于项目中有用到APP英文名称
    public static final String APP_NAME = "YueHui";

    //第三方分享登录是用到的code值
    public static final int SHARE_REQUESTCODE = 2001;

    // DEBUG模式。影响log级别输出
    public static boolean DEBUG = true;//BuildConfig.DEBUG;

    public static final String WX_LOGIN_API = "https://api.weixin.qq.com/sns/oauth2/access_token";//微信登录接口
    public static final String ERROR_REQUEST_FAILED_MESSAGE = "网络请求失败";//REQUEST_FAILED"; // 网络请求失败，出现onerror
    public static final String ERROR_REQUEST_EXCEPTION_MESSAGE = "服务返回数据异常";//REQUEST_EXCEPTION"; // 网络请求数据异常

    public static final int VERSIONCODE = AppUtils.getCurrentVersion(MyApplication.getInstance()).versionCode;//当前版本号

    public static final int SUCCESS_CODE = 200;//REQUEST_EXCEPTION"; // 网络请求成功的code值
    //自定义Token失效
    public static final String TOKEN_INCALID = "TokenInvalid";

    /**
     * 用户相关的属性
     */
    public class USER_TYPE {
        public static final String APITOKEN = "apiToken";               // 应用服务端接口令牌
        public static final String USERTOKEN = "userToken";             // 用户Token
        public static final String YUNXINTOKEN = "yunxinToken";         // 云信Token
        public static final String ACCID = "accid";                     // 云信account
        public static final String KEY = "key";                         // 签名时使用的key
    }

    /**
     * 期望对象  数据源
     * */
    public static final String[] expectList = new String[]{"看脸","有趣","土豪","关爱我","看感觉","无所谓"};

    /**
     * 错误码
     */
    public class RESPONSE_CODE {
        public static final int CODE_10000 = 10000;            // 请求失败
        public static final int CODE_10001 = 10001;            // 未知错误
        public static final int CODE_10003 = 10003;            // 认证过期
        public static final int CODE_10004 = 10004;            // 请求超时
        public static final int CODE_10005 = 10005;            // 签名错误
        public static final int CODE_10006 = 10006;            // 重复提交
        public static final int CODE_10007 = 10007;            // AppId错误
        public static final int CODE_10008 = 10008;            // token is null
        public static final int CODE_10010 = 10010;            // 注册失败
        public static final int CODE_10011 = 10011;            // 微信授权错误
        public static final int CODE_10012 = 10012;            // 用户已经存在
        public static final int CODE_10013 = 10013;            // 手机号未绑定
        public static final int CODE_10014 = 10014;            // 手机验证码错误
        public static final int CODE_10015 = 10015;            // 用户非法操作
        public static final int CODE_10016 = 10016;            // 用户信息异常
        public static final int CODE_10017 = 10017;            // 参数错误
        public static final int CODE_10018 = 10018;            // 手机号码不规范
        public static final int CODE_10019 = 10019;            // 请求手机验证码失败
        public static final int CODE_10020 = 10020;            // 手机已绑定
        public static final int CODE_10021 = 10021;            // 认证失败
        public static final int CODE_10022 = 10022;            // 用户未注册
        public static final int CODE_10023 = 10023;            // 用户uuid与微信uuid不匹配
        public static final int CODE_10024 = 10024;            // 不是绑定的手机号码
        public static final int CODE_10028 = 10028;            // 登录账号异常

        public static final int CODE_20000 = 20000;            // 封禁用户错误
        public static final int CODE_20001 = 20001;            // 解封用户错误
        public static final int CODE_20002 = 20002;            // 添加好友失败
        public static final int CODE_20003 = 20003;            // 更新好友信息失败
        public static final int CODE_20004 = 20004;            // 删除好友失败
        public static final int CODE_20005 = 20005;            // 查询好友失败
        public static final int CODE_20006 = 20006;            // 设置黑名单或静音失败
        public static final int CODE_20007 = 20007;            // 查看指定用户的黑名单和静音列表失败

        public static final int CODE_30001 = 30001;            // 客户端设置推送失败
        public static final int CODE_30002 = 30002;            // 禁言失败
        public static final int CODE_30003 = 30003;            // 禁用音视频失败
        public static final int CODE_30004 = 30004;            // 更新用户名片失败
        public static final int CODE_30005 = 30005;            // 获取用户名片失败

        public static final int CODE_40001 = 40001;            // 群组解散失败
        public static final int CODE_40002 = 40002;            // 获取群组信息失败
        public static final int CODE_40003 = 40003;            // 本地存储群组信息失败
        public static final int CODE_40004 = 40004;            // 转换群主失败
        public static final int CODE_40005 = 40005;            // 群组不存在
        public static final int CODE_40006 = 40006;            // 用户主动离群失败
        public static final int CODE_40007 = 40007;            // 踢用户离群失败
        public static final int CODE_40008 = 40008;            // 群配置信息不存在

        public static final int CODE_50001 = 50001;            // 用户钱包账户已存在
        public static final int CODE_50002 = 50002;            // 用户钱包账户不存在
        public static final int CODE_50003 = 50003;            // 用户金额不足
        public static final int CODE_50004 = 50004;            // 红包已领完
        public static final int CODE_50005 = 50005;            // 用户支付密码错误
        public static final int CODE_50006 = 50006;            // 两次输入密码不等
        public static final int CODE_50007 = 50007;            // 支付配置异常
        public static final int CODE_50008 = 50008;            // 支付宝订单失败
        public static final int CODE_50009 = 50009;            // 提现金额超出限度
        public static final int CODE_50010 = 50010;            // 红包已领过
        public static final int CODE_50011 = 50011;            // 禁止用户领取红包
        public static final int CODE_50012 = 50012;            // 生成支付宝签名异常
        public static final int CODE_50013 = 50013;            // 生成订单号异常
        public static final int CODE_50014 = 50014;            // 每日最大提现不能超2万
        public static final int CODE_50021 = 50021;            // 微信提现失败
        public static final int CODE_50022 = 50022;            // 支付宝提现失败

        public static final int CODE_60002 = 60002;            // 未绑定群助手
    }

    /**
     * 构建红包收发数据Key（与iOS统一）
     */
    public class BUILDREDSTRUCTURE {
        /**
         * ========================================红包发送================================================
         */
        public static final String REDPACKET_ID = "redpacketID";                    //红包id
        public static final String REDPACKET_TYPE = "redpacketType";                //红包类型
        public static final String REDPACKET_TYPESTR = "redpacketTypeStr";          //红包字符串类型
        public static final String REDPACKET_GROUPID = "groupID";                   //群ID  （群红包再设置值）
        public static final String REDPACKET_MONEY = "money";                       //金额
        public static final String REDPACKET_COUNT = "count";                       //红包数量     (群红包再设置值)
        public static final String REDPACKET_GREETING = "greeting";                 //祝福语
        public static final String REDPACKET_RECEIVER = "receivers";                //红包接收者(对象userID，userName，avatar)
        public static final String REDPACKET_SENDER = "sender";                     //红包发送者(对象userID，userName，avatar)
        public static final String REDPACKET_SENDERID = "senderId";                 //红包发送者Id

        /**
         * ========================================红包领取=================================================
         */
        public static final String REDPACKET_STATUSTYPE = "statusType";             //红包状态
        public static final String REDPACKET_RECEIVEMONEY = "receiveMoney";         //当前用户收到的红包金额
        public static final String REDPACKET_ISSENDER = "isSender";                 //当前用户是否是红包发送者
        public static final String REDPACKET_TAKENUSERS = "takenUsers";             //群红包领取人们
        public static final String REDPACKET_ADVERINFO = "adverInfo";               //广告红包信息
        public static final String REDPACKET_TOTALTAKENCOUNT = "totalTakenCount";   //被领取数量
        public static final String REDPACKET_TOTALTAKENMONEY = "totalTakenMoney";   //领取金额
        public static final String REDPACKET_TAKENCOSTTIME = "takenCostTime";       //领取所用时间
    }

}
