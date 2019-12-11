package com.yqbj.yhgy.wxapi;

import android.content.Context;

import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yqbj.yhgy.MyApplication;
import com.yqbj.yhgy.R;

public class WXUtil {

    public static void weiChatPay(final Context context) {
        DialogMaker.showProgressDialog(context, context.getString(R.string.empty), true);

        IWXAPI wxapi = WXAPIFactory.createWXAPI(MyApplication.getInstance(), MyApplication.WXAPP_ID);  //应用ID 即微信开放平台审核通过的应用APPID
        wxapi.registerApp(MyApplication.WXAPP_ID);    //应用ID
        PayReq payReq = new PayReq();
        payReq.appId = MyApplication.WXAPP_ID;        //应用ID
        payReq.partnerId = "1221121221";      //商户号 即微信支付分配的商户号
        payReq.prepayId = "1212121212";        //预支付交易会话ID
        payReq.packageValue = "1212121221";    //扩展字段
        payReq.nonceStr = "1212121212";        //随机字符串不长于32位。
        payReq.timeStamp = "" + System.currentTimeMillis(); //时间戳
        payReq.sign = "0";             //签名
        wxapi.sendReq(payReq);
    }

}
