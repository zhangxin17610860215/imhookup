package com.yqbj.yhgy.utils;

import android.content.Context;
import android.content.Intent;

import com.netease.nim.uikit.api.model.location.LocationProvider;
import com.yqbj.yhgy.main.LocationAmapActivity;
import com.yqbj.yhgy.main.LocationExtras;
import com.yqbj.yhgy.main.NavigationAmapActivity;
import com.yqbj.yhgy.view.EasyAlertDialogHelper;

/**
 * Created by zhoujianghua on 2015/8/11.
 */
public class NimDemoLocationProvider implements LocationProvider {
    @Override
    public void requestLocation(final Context context, Callback callback) {
        if (!NimLocationManager.isLocationEnable(context)) {

            EasyAlertDialogHelper.showCommonDialog(context, null, "位置信息未开启", "设置", "取消", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                @Override
                public void doCancelAction() {

                }

                @Override
                public void doOkAction() {



                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        LogUtil.e("LOC", "start ACTION_LOCATION_SOURCE_SETTINGS error");
                    }

                }
            }).show();
            return;
        }

        LocationAmapActivity.start(context, callback);
    }

    @Override
    public void openMap(Context context, double longitude, double latitude, String address) {
        Intent intent = new Intent(context, NavigationAmapActivity.class);
        intent.putExtra(LocationExtras.LONGITUDE, longitude);
        intent.putExtra(LocationExtras.LATITUDE, latitude);
        intent.putExtra(LocationExtras.ADDRESS, address);
        context.startActivity(intent);
    }
}
