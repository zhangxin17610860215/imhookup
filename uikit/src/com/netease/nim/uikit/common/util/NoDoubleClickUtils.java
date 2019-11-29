package com.netease.nim.uikit.common.util;

/**
 * 防止按钮 连续多次点击
 * Created by zhouxu on 2018/9/17.
 */
public class NoDoubleClickUtils {
    private final static int SPACE_TIME = 500;//2次点击的间隔时间，单位ms
    private static long lastClickTime;

    public synchronized static boolean isDoubleClick(int spaceTime) {
        long currentTime = System.currentTimeMillis();
        boolean isClick;
        if (currentTime - lastClickTime > spaceTime) {
            isClick = false;
        } else {
            isClick = true;
        }
        lastClickTime = currentTime;
        return isClick;
    }
}
