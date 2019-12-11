package com.yqbj.yhgy.requestutils.api;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.BaseBean;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.RequestHelp;
import com.yqbj.yhgy.utils.GsonHelper;
import com.yqbj.yhgy.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import static com.yqbj.yhgy.config.Constants.ERROR_REQUEST_EXCEPTION_MESSAGE;

public class UserApi {

    private final static String TAG = "UserApi";

    /**
     * 获取群升级价格列表
     * */
    public static void getTeamAllocationPrice(String tid, Object object, final RequestCallback callback){
        Map<String,String> map = new HashMap<>();
        map.put("tid",tid);
        RequestHelp.postRequest(ApiUrl.USER_LOGIN, object, map, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                LogUtil.e(TAG, "getTeamAllocationPrice--------->onSuccess" + response.body());
                try {
                    BaseBean bean = GsonHelper.getSingleton().fromJson(response.body(), BaseBean.class);
                    if (bean.getStatusCode() == Constants.SUCCESS_CODE){
//                        TeamAllocationPriceBean priceBean = GsonHelper.getSingleton().fromJson(bean.getData(), TeamAllocationPriceBean.class);
//                        callback.onSuccess(bean.getStatusCode(),priceBean);
                    } else {
                        callback.onFailed(bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailed(e.getMessage());
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                LogUtil.e(TAG, "getTeamAllocationPrice--------->onError" + response.body());
                callback.onFailed(ERROR_REQUEST_EXCEPTION_MESSAGE);

            }

        });
    }

}
