package com.yqbj.yhgy.utils.pay;

public interface AliPaySuccess {
    void getSuccessListener(AliPayResult payResult);
    void getErrorListener(AliPayResult payResult);
}
