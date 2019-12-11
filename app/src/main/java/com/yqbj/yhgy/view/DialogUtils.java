package com.yqbj.yhgy.view;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class DialogUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static LoadingDialog createProgressDialog(Activity activity, boolean cancelable) {
        LoadingDialog.Builder dialog = new LoadingDialog.Builder(activity);
        dialog.setCancelable(true);
        dialog.setCancelOutside(true);
        return dialog.create();
    }

}
