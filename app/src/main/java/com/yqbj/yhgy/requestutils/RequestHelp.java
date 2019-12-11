package com.yqbj.yhgy.requestutils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.netease.nim.uikit.common.util.SPUtils;
import com.yqbj.yhgy.MyApplication;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.utils.CrypticUtil;
import com.yqbj.yhgy.utils.LogUtil;
import com.yqbj.yhgy.utils.StringUtil;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 网络请求的简单，统一处理
 */
public class RequestHelp {
    private static final String TAG = "RequestHelp";
    private static String lastUrl = "";

    /**
     * 取消网络请求.如果tag为null，则取消所有的
     *
     * @param tag
     */
    public static void cancelRequest(Object tag) {
        if (null == tag) {
            OkGo.getInstance().cancelAll();
        } else {
            OkGo.getInstance().cancelTag(tag);
        }
    }

    /**
     * Get   请求    APP第一个接口对参数加密
     */
    public static void getRequest(String url, Object tag, Map<String, String> map, StringCallback callback) {
        Map<String, String> fullParam = getKeyHeaders(url,map);
        LogUtil.e("okgo", "getRequest----->" + url + ">>>>>>>>>>>" + fullParam.toString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.headersMap.putAll(fullParam);
        GetRequest<String> getRequest = OkGo.<String>get(url)
                .cacheMode(CacheMode.NO_CACHE)//设置缓存模式
//                .cacheKey(url)//作为缓存的key
                .tag(tag)
                .headers(httpHeaders);
//        if (!url.equals(ApiUrl.CONFIGINFO)){
//            getRequest.params(map);
//        }
        getRequest.execute(callback);
    }

    /**
     * 普通Get请求    微信登录时调用
     */
    public static void getReq(String url, Object tag, Map<String, String> map, StringCallback callback) {
        LogUtil.e(TAG, "getReq----->" + map.toString());
//        OkGo.getInstance().setOkHttpClient(NimApplication.getInstance().getOkHttpClinetInstance());//增加此代码   为了重新初始化一下，不去认证微信SSL证书，因为暂时只有微信用的get请求
        GetRequest<String> getRequest = OkGo.<String>get(url);
        getRequest.cacheMode(CacheMode.NO_CACHE);
        getRequest.tag(tag);
        getRequest.params(map);
        getRequest.execute(callback);
    }

    /**
     * Post   请求
     */
    public static void postRequest(String url, final Object tag, Map<String, String> map, StringCallback callback) {
        Map<String, String> paramsMap = new HashMap<>();
        if (map != null) {
            paramsMap.putAll(map);
        }
        Map<String, String> headersMap = appendCommonParam(url, paramsMap);
        LogUtil.e(TAG, "Post(headers)----->" + headersMap.toString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.headersMap.putAll(headersMap);
        PostRequest postRequest = OkGo.<String>post(url)
                .cacheMode(CacheMode.NO_CACHE)//设置缓存模式
                .tag(tag)
                .headers(httpHeaders)
                .params(getSortedMapByKey(map));
        postRequest.execute(callback);
    }


    public static void downLoadFile(String url, final Object tag, FileCallback fileCallback) {

        OkGo.<File>get(url).tag(tag).execute(fileCallback);

    }

    /**
     * 构造完整的请求参数，包括base64、md5
     * 弃用
     *
     * @param map
     * @return
     */
    private static Map<String, String> buildFullParam(String url, Map<String, String> map) {
        map = appendCommonParam(url, map);

        //进行Key=value的格式排版得到需要加密的字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            if (null == entry.getValue() || entry.getValue().equals("")) // 如果是null，则跳过
                continue;

            sb.append(entry.getKey())
                    .append("=")
                    .append(String.valueOf(entry.getValue()))
                    .append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        String md5Str = CrypticUtil.md5(sb.toString());
        String rsaStr = "";
        try {
            rsaStr = CrypticUtil.RSAEncrypt(md5Str.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("Sign", rsaStr);
        return map;

    }

    /**
     * 获取Key接口的请求头参数
     *
     *
     * @param url
     * @param map
     * @return
     */
    private static Map<String, String> getKeyHeaders(String url, Map<String, String> map) {
        map.put("AppId", "xialiao_v1");
//        if (!url.equals(ApiUrl.OVERALL_GET_KEY)){
//            map.put("VersionNo", StringUtil.getAppVersionName(NimApplication.getInstance()));
//        }
//        if (url.equals(ApiUrl.CONFIGINFO)){
//            //检查全局配置接口的头信息字段不同
//            map.put("Token",SPUtils.getInstance().getString(Constants.USER_TYPE.USERTOKEN));
//        }else {
//            String RandomNu = getRandomNu();
//            map.put("Nonce", RandomNu);//6位随机数
//            map.put("CurTime", String.valueOf(System.currentTimeMillis() / 1000));//刷新时间
//        }

        //进行Key=value的格式排版得到需要加密的字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            if (null == entry.getValue() || entry.getValue().equals("")) // 如果是null，则跳过
                continue;

            sb.append(entry.getKey())
                    .append("=")
                    .append(String.valueOf(entry.getValue()))
                    .append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        String md5Str = CrypticUtil.md5(sb.toString());
        String rsaStr = "";
        try {
            rsaStr = CrypticUtil.RSAEncrypt(md5Str.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("Sign", rsaStr);
        return map;
    }


    private static Map<String, String> appendCommonParam(String url, Map<String, String> map) {
        //进行Key=value的格式排版得到需要加密的字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            if (null == entry.getValue() || entry.getValue().equals("")) // 如果是null，则跳过
                continue;

            sb.append(entry.getKey())
                    .append("=")
                    .append(String.valueOf(entry.getValue()))
                    .append("&");
        }
        String key = "";
        try {
            key = CrypticUtil.RSADecrypt(SPUtils.getInstance().getString(Constants.USER_TYPE.KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNotEmpty(sb.toString())) {
            sb.delete(sb.length() - 1, sb.length());
            sb.append("&").append("key").append("=").append(key);
        } else {
            sb.append("key").append("=").append(key);
        }
        String md5Str = CrypticUtil.md5(sb.toString());
        map.clear();
        map.put("Sign", md5Str.toUpperCase());
        map.put("AppId", "xialiao_v1");
        map.put("VersionNo", StringUtil.getAppVersionName(MyApplication.getInstance()));
//        if (url.equals(ApiUrl.USER_LOGIN) || url.equals(ApiUrl.USER_SIGNUP) || url.equals(ApiUrl.USER_PHONE_LOGIN_CODE) || url.equals(ApiUrl.USER_PHONE_LOGIN)) {
//            //登录注册接口用APITOKEN
//            map.put("Token", SPUtils.getInstance().getString(Constants.USER_TYPE.APITOKEN));
//        } else {
//            //否则使用USERTOKEN
//            map.put("Token", SPUtils.getInstance().getString(Constants.USER_TYPE.USERTOKEN));
//        }
        return map;
    }

    private static String getRandomNu() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    /**
     * 按照key的自然顺序进行排序，并返回
     *
     * @param map
     * @return
     */
    private static Map<String, String> getSortedMapByKey(Map<String, String> map) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        Map<String, String> treeMap = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }

}
