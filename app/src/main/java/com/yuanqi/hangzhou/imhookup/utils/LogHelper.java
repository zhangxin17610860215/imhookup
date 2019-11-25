package com.yuanqi.hangzhou.imhookup.utils;

import android.util.Log;

import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.common.log.ILogUtil;

/**
 * Created by winnie on 2017/12/21.
 */

public class LogHelper {

    public static void init() {
        AVChatKit.setiLogUtil(new ILogUtil() {
            @Override
            public void ui(String msg) {
                Log.e("NIM", ">>>>>>>>" + msg);
            }

            @Override
            public void e(String tag, String msg) {
                LogUtil.e(tag, msg);
            }

            @Override
            public void i(String tag, String msg) {
                LogUtil.i(tag, msg);
            }

            @Override
            public void d(String tag, String msg) {
                LogUtil.d(tag, msg);
            }
        });
    }
}
