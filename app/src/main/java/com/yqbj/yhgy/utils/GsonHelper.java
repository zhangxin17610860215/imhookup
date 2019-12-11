package com.yqbj.yhgy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by wangzl on 2017/12/13.
 */

public class GsonHelper {
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Gson singleton;

    public static Gson getSingleton() {
        if (singleton == null) {
            init();
        }
        return singleton;
    }

    private static void init() {
        singleton = new GsonBuilder()
                .serializeNulls()
                .generateNonExecutableJson()
                .create();
    }

}
