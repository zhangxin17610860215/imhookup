package com.yuanqi.hangzhou.imhookup.utils.pay;

public interface AliPaySuccess {
    void getSuccessListener(AliPayResult payResult);
    void getErrorListener(AliPayResult payResult);
}
