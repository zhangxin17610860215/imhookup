package com.netease.nim.uikit.common;

import android.content.Context;

public class ModuleUIComFn {

    private static ModuleUIComFn instance;

    /**
     * 跳转举报页面
     * */
    public ToReportActivity toReportActivityInstance;

    public static ModuleUIComFn getInstance() {
        if (instance == null) {
            instance = new ModuleUIComFn();
        }
        return instance;
    }

    public void toReportActivityClick(Context context) {
        if (toReportActivityInstance != null) {
            toReportActivityInstance.toReportActivityInstance(context);
        }
    }

    public interface ToReportActivity {
        void toReportActivityInstance(Context context);
    }

}
