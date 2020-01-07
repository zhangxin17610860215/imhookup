package com.yqbj.yhgy.requestutils;

import com.netease.nim.uikit.common.ToastHelper;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.GsonHelper;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class RequestInterceptor implements Interceptor {
    private String url = "";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        url = String.valueOf(request.url());

        Response originalResponse = null;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        if (null != originalResponse) {
            ResponseBody responseBody = originalResponse.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            MediaType contentType = responseBody.contentType();
            String bodyString = buffer.clone().readString(Charset.forName("UTF-8"));
            ResponseBody responseBodyDecode = ResponseBody.create(contentType, bodyString);

            //检查key是否失效
            isKeyInvalid(bodyString);

            return originalResponse.newBuilder().body(responseBodyDecode).build();
        } else {
            return null;
        }
    }

    private void isKeyInvalid(String bodyString) {
        try {
            if (StringUtil.isNotEmpty(bodyString)){
                BaseBean bean = GsonHelper.getSingleton().fromJson(bodyString, BaseBean.class);
                if(null != bean){
                    if (bean.getCode() == Constants.RESPONSE_CODE.CODE_10002){
                        String loginType = Preferences.getLoginType();
                        String wxToken = Preferences.getWxToken();
                        String openId = Preferences.getWxOpenid();
                        String uuId = Preferences.getWxUuid();
                        String phone = Preferences.getAccount();
                        String psw = Preferences.getPassword();
                        UserApi.login(loginType, phone,psw, wxToken, openId, uuId, DemoCache.getContext(), new RequestCallback() {
                            @Override
                            public void onSuccess(int code, Object object) {
                                if (code == Constants.SUCCESS_CODE){
                                    ToastHelper.showToast(DemoCache.getContext(),"认证过期，请重试");
                                }
                            }

                            @Override
                            public void onFailed(String errMessage) {}
                        });
                    }
                }
            }
        }catch (Exception e){

        }

    }
}
