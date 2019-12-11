package com.yqbj.yhgy.utils.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.yqbj.yhgy.utils.Base64;
import com.yqbj.yhgy.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * zhangxin
 */

public class MyALipayUtils {

    private static final int SDK_PAY_FLAG = 1;
    private Activity context;
    private ALiPayBuilder builder;
    private AliPaySuccess aliPaySuccess;

    private MyALipayUtils(ALiPayBuilder builder) {
        this.builder = builder;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

//            返回码	含义
//            9000	订单支付成功
//            8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//            4000	订单支付失败
//            5000	重复请求
//            6001	用户中途取消
//            6002	网络连接出错
//            6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//            其它	其它支付错误
            AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
            switch (payResult.getResultStatus()) {
                case "9000":
                    aliPaySuccess.getSuccessListener(payResult);
                    Toast.makeText(context,"支付成功",Toast.LENGTH_LONG).show();
                    break;
                case "8000":
                    Toast.makeText(context,"正在处理中",Toast.LENGTH_LONG).show();
                    break;
                case "4000":
                    Toast.makeText(context,"订单支付失败",Toast.LENGTH_LONG).show();
                    break;
                case "5000":
                    Toast.makeText(context,"重复请求",Toast.LENGTH_LONG).show();
                    break;
                case "6001":
                    Toast.makeText(context,"已取消支付",Toast.LENGTH_LONG).show();
                    break;
                case "6002":
                    Toast.makeText(context,"网络连接出错",Toast.LENGTH_LONG).show();
                    break;
                case "6004":
                    Toast.makeText(context,"正在处理中",Toast.LENGTH_LONG).show();
                    break;
                default:
                    aliPaySuccess.getErrorListener(payResult);
                    Toast.makeText(context,"支付失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * 未签名的订单信息
     * */
    public String getOrderParams(boolean isSort){
        Map<String, String> params = buildOrderParamMap(true,"");
        return buildOrderParam(params,isSort);
    }


    public void goAliPay(final String tradeType, final Activity mContext){
        final Map<String,String> oriMap = buildOrderParamMap(true,tradeType);

        final String orderParams = buildOrderParam(oriMap,false);
        String baseOrderParams = "";
        try {
            baseOrderParams = Base64.encode(orderParams.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DialogMaker.showProgressDialog(mContext, null, true);
//        UserApi.singParams(tradeType, baseOrderParams, opt,rid,money,targetIds,targetType,name,paymentPwd,number,targetId,mContext, new requestCallback() {
//            @Override
//            public void onSuccess(int code, Object object) {
//                DialogMaker.dismissProgressDialog();
//                if (code == Constants.SUCCESS_CODE){
//                    SignParamsBean bean = (SignParamsBean) object;
//                    toALiPay(mContext, buildOrderParam(oriMap,true), bean.getSign(), new AliPaySuccess() {
//                        @Override
//                        public void getSuccessListener(AliPayResult payResult) {
//                            if(mListener!=null){
//                                mListener.onPaySuccess();
//                            }
//                        }
//                    });
//                }else {
//                    ToastUtil.showToast(mContext, (String) object);
//                }
//            }
//
//            @Override
//            public void onFailed(String errMessage) {
//                DialogMaker.dismissProgressDialog();
//                ToastUtil.showToast(mContext, errMessage);
//                if(mListener!=null){
//                    mListener.onPayFailed();
//                }
//            }
//        });


    }


    /**
     * 签名在服务端来做
     * @param context
     * @param sign
     */
    public void toALiPay(final Activity context,final String orderParams,final String sign,AliPaySuccess aliPaySuccess) {
        this.context = context;
        this.aliPaySuccess = aliPaySuccess;
        String encode = "";
        try {
            encode = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String orderInfo = orderParams + "&sign=" + encode;
//        final String orderInfo = "app_id=2019032063603734&biz_content={\"timeout_express\"%3A\"30m\"%2C\"seller_id\"%3A\"\"%2C\"product_code\"%3A\"QUICK_MSECURITY_PAY\"%2C\"total_amount\"%3A\"0.01\"%2C\"subject\"%3A\"%E7%88%B1%E8%81%8A%E7%BA%A2%E5%8C%85%E6%94%AF%E4%BB%98\"%2C\"body\"%3A\"%E7%88%B1%E8%81%8A%E7%BA%A2%E5%8C%85%E6%94%AF%E4%BB%98\"%2C\"out_trade_no\"%3A\"2019041515415722110000008\"}&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F139.196.106.67%2Fnotify%2Falipay&sign_type=RSA2&timestamp=2019-04-15+15%3A55%3A26&version=1.0&sign=G2p0Ob9fzJ4so1Tt6qC5woewWTyZeR%2FVIYnvlGGFayBMIrtT6b1FKR8b5XFolfTR983aU%2BoK94iJ0I4U%2BqsaAylXyjBx8hpBOhw88D1jxJCPVrgZhd8PH67BZ67x9Q%2FvdWe7wMEXTHON2J7UyE%2FilScetiP1iSk9%2FnB0kto1lbwtRfm1N59EnyxK1qBA1wkby9%2FUnwpGAL8eEKQRcMRO0H9oADAukjr3JSJRlbY9FYbhiHjiN5RQDf8JmmYoha9oENNVfOiYCbxWip8rRpPMLvRJlVGrsTX9Ocf%2FtIHWLmTe7YJisQEKVzj08zBeipD%2BUglSECGxwV9z58HTNibTrg%3D%3D";
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2
                        (orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * 构造支付订单参数列表
     *
     * @param
     * @param
     * @return
     */
    public Map<String, String> buildOrderParamMap(boolean rsa2,String tradeType) {

        String accid_tradeType = "";
        if (StringUtil.isNotEmpty(tradeType)){
            accid_tradeType = NimUIKit.getAccount() + "_" + tradeType;
        }else {
            accid_tradeType = NimUIKit.getAccount();
        }
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", builder.appid);

        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"seller_id\":\"\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + builder.money + "\",\"subject\":\"" + builder.title + "\",\"body\":\"" + accid_tradeType + "\",\"out_trade_no\":\"" + builder.orderTradeId + "\",\"enable_pay_channels\":\"balance,moneyFund,debitCardExpress,bankPay\"}");

        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");
        //回调接口
        keyValues.put("notify_url", builder.getNotifyUrl());

        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        keyValues.put("timestamp", getCurrentTimeString());

        keyValues.put("version", "1.0");


        return keyValues;
    }
    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    public String buildOrderParam(Map<String, String> map,boolean isSort) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, isSort));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, isSort));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private  String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
    /**
     * 获取当前日期字符串
     * @return
     */
    private  String getCurrentTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }


    public interface AlipayListener{
        void onPaySuccess();
        void onPayFailed();
    }

    public static class ALiPayBuilder {
        private String rsa2 = "";
        private String rsa = "";
        private String appid;           //AppId
        private String money;           //金额
        private String title;           //商品名称
        private String body;            //商品描述
        private String notifyUrl;       //服务器异步通知页面路径
        private String orderTradeId;    //订单号

        public MyALipayUtils build() {
            return new MyALipayUtils(this);
        }

        public String getOrderTradeId() {
            return orderTradeId;
        }

        public ALiPayBuilder setOrderTradeId(String orderTradeId) {
            this.orderTradeId = orderTradeId;
            return this;
        }

        public String getRsa2() {
            return rsa2;
        }

        public ALiPayBuilder setRsa2(String rsa2) {
            this.rsa2 = rsa2;
            return this;
        }

        public String getRsa() {
            return rsa;
        }

        public ALiPayBuilder setRsa(String rsa) {
            this.rsa = rsa;
            return this;
        }

        public String getAppid() {
            return appid;
        }

        public ALiPayBuilder setAppid(String appid) {
            this.appid = appid;
            return this;
        }

        public String getMoney() {
            return money;
        }

        public ALiPayBuilder setMoney(String money) {
            this.money = money;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public ALiPayBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getBody() {
            return body;
        }

        public ALiPayBuilder setBody(String body) {
            this.body = body;
            return this;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public ALiPayBuilder setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }
    }
}
