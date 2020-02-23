package com.yqbj.yhgy.bean;

public class PayInfoBean {

    /**
     * payInfo : alipay_sdk=alipay-sdk-java-4.8.73.ALL&app_id=2019032063603734&biz_content=%7B%22out_trade_no%22%3A%22202002181116181447425961019%22%2C%22passback_params%22%3A%22144742596_2_2%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E8%A7%A3%E9%94%81%E7%9B%B8%E5%86%8C%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221%22%7D&charset=UTF-8&format=JSON&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2F139.196.106.67%2Fnotify%2Falipay&sign=HYB5muLs1C0O0Rx6SVNzBNWoPdBGVEsdNi%2FASEubSO5PRFlXn%2BitpGbZcfgSq%2FoBcv%2BC59QfcaGWvQ2x8x2o9S7MLKbc3EcuSU2ygwdIX6MB8yS4oYonskChtYsawB1%2Bu3ssQLSYOPxUH1C5pnFGAl9%2Fge%2FGYJY8ivEY1RUmShDCmdj7hGLL0Xitdj%2FvGL%2BLijmLcGD8ZAOn5OMo5Go4yhTSjGsxdveOeQkquOTHe2k4XGwvUDZhlvc77FBLQD428oVABARk%2BD5CcgiKrs533VNsXxu3j1ALY01HPeHeuAC0mlnGqwqzIwQDzfB3oNK4205W33y5ARwrCuy6xxQIqg%3D%3D&sign_type=RSA2×tamp=2020-02-18+11%3A16%3A18&version=1.0
     */

    private String payInfo;

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }
}
