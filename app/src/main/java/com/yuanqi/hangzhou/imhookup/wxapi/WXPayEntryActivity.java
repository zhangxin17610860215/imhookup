package com.yuanqi.hangzhou.imhookup.wxapi;

import android.app.Activity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuanqi.hangzhou.imhookup.MyApplication;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    /**
     *调起微信支付的方法
     **/
    public static void toWXPay(final String partnerId, final String prepayId, final String packageValue,
                               final String nonceStr, final String timeStamp, final String sign) {

        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                IWXAPI wxapi = WXAPIFactory.createWXAPI(MyApplication.getInstance(), MyApplication.WXAPP_ID);  //应用ID 即微信开放平台审核通过的应用APPID
                wxapi.registerApp(MyApplication.WXAPP_ID);    //应用ID
                PayReq payReq = new PayReq();
                payReq.appId = MyApplication.WXAPP_ID;        //应用ID
                payReq.partnerId = partnerId;      //商户号 即微信支付分配的商户号
                payReq.prepayId = prepayId;        //预支付交易会话ID
                payReq.packageValue = packageValue;    //扩展字段
                payReq.nonceStr = nonceStr;        //随机字符串不长于32位。
                payReq.timeStamp = "" + timeStamp; //时间戳
                payReq.sign = sign;             //签名
                wxapi.sendReq(payReq);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if(resp.getType()==ConstantsAPI.COMMAND_PAY_BY_WX){
            if(resp.errCode==0){
//                ToastUtil.showMsg("支付成功");
            }
            else {
//                ToastUtil.showMsg("支付失败");
            }
            finish();
        }
    }
}
