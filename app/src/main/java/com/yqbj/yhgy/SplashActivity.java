package com.yqbj.yhgy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.netease.nim.uikit.support.permission.BaseMPermission;
import com.netease.nim.uikit.support.permission.MPermission;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.login.GetReadyLoginActivity;
import com.yqbj.yhgy.view.EasyAlertDialogHelper;

import java.util.List;

/**
 * 开屏页
 * */
public class SplashActivity extends BaseActivity {

    private Activity activity;
    private Handler handler = new Handler();
    private static final String[] BASIC_PERMISSIONS = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //检查权限
            //获取被拒绝但没有勾选不再询问的权限（可以继续申请，会继续弹框）
            List<String> askAgainList = MPermission.getDeniedPermissionsWithoutNeverAskAgain(activity, BASIC_PERMISSIONS);
            //获取未授权的权限
            List<String> unauthorizedList = MPermission.getDeniedPermissions(activity, BASIC_PERMISSIONS);
            if (null != askAgainList && askAgainList.size() > 0){
                //有被拒绝的权限   没有勾选
                EasyAlertDialogHelper.showCommonDialog(activity, "权限已被拒绝", "您已经拒绝过我们申请授权,请您同意授权,否则功能无法正常使用!", "继续", "取消", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {
                        toast("权限已被拒绝");
                    }

                    @Override
                    public void doOkAction() {
                        checkPermission();
                    }
                }).show();
            }else if (null != unauthorizedList && unauthorizedList.size() == 0){
                //权限均已允许
                handler.postDelayed(readyShow, 2000);
            }else {
                checkPermission();
            }
        }else {
            handler.postDelayed(readyShow, 2000);
        }
    }

    Runnable readyShow = new Runnable() {
        @Override
        public void run() {
            GetReadyLoginActivity.start(activity);
            finish();
        }
    };

    private void checkPermission() {
        MPermission.with(activity)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        //获取被拒绝,且勾选不再询问的权限
        List<String> resolutelyRefuseList = MPermission.getNeverAskAgainPermissions(activity, BASIC_PERMISSIONS);
        //获取被拒绝但没有勾选不再询问的权限（可以继续申请，会继续弹框）
        List<String> askAgainList = MPermission.getDeniedPermissionsWithoutNeverAskAgain(activity, BASIC_PERMISSIONS);
        if (null != resolutelyRefuseList && resolutelyRefuseList.size() > 0){
            //有被拒绝的权限   且已勾选
            EasyAlertDialogHelper.showCommonDialog(activity, "权限申请失败", "我们需要的一些权限被您拒绝或者系统发生错误申请失败,请您到设置页面手动授权,否则功能无法正常使用!", "继续", "取消", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                @Override
                public void doCancelAction() {
                    toast("权限申请失败");
                }

                @Override
                public void doOkAction() {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    try{
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).show();
        }else if (null != askAgainList && askAgainList.size() > 0){
            //有被拒绝的权限   没有勾选
            toast("权限已被拒绝");
        }else {
            handler.postDelayed(readyShow, 2000);
        }

    }

    /**
     * 系统的"返回键"按下的时间戳。用来实现连点2次退出应用
     */
    private static long BACK_PRESS_TIME = 0;

    @Override
    public void onBackPressed() {
        if (BACK_PRESS_TIME + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            toast("再按一次退出快速交友");
        }
        BACK_PRESS_TIME = System.currentTimeMillis();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                BACK_PRESS_TIME = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }
}
