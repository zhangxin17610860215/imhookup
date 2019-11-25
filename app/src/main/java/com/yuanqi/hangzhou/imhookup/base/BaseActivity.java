package com.yuanqi.hangzhou.imhookup.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.umeng.socialize.UMShareAPI;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.config.Constants;
import com.yuanqi.hangzhou.imhookup.utils.StringUtil;
import com.yuanqi.hangzhou.imhookup.view.DialogUtils;
import com.yuanqi.hangzhou.imhookup.view.LoadingDialog;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    public static boolean isActive; //全局变量  app是否进入后台

    protected LoadingDialog mProgressDialog;

    private static Handler handler;

    private boolean destroyed = false;

    public void setToolbar(final Activity activity, int leftLogo, String title) {
        TextView tvTitle = activity.findViewById(R.id.tv_tooblertitle);
        ImageView imgBack = activity.findViewById(R.id.img_tooblerback);
        tvTitle.setText(title);
        if (leftLogo == 0){
            imgBack.setImageResource(R.mipmap.back_icon);
        }else {
            imgBack.setImageResource(leftLogo);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setRightImg(final Activity activity,int imgRes,final onToolBarRightImgListener rightImgListener){
        if(imgRes!=0){
            ImageView img_right = activity.findViewById(R.id.img_toobleRight);
            img_right.setVisibility(View.VISIBLE);
            img_right.setImageResource(imgRes);
            if(rightImgListener!=null){
                img_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rightImgListener.onRight(view);
                    }
                });
            }
        }
    }

    public void onInitRightSure(final Activity activity,String textRightSure,final onToolBarRightTextListener onToolBarRightTextListener) {
        if(StringUtil.isNotEmpty(textRightSure)){
            TextView tv_right = activity.findViewById(R.id.tv_toobleRight);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(textRightSure);
            if(onToolBarRightTextListener!=null){
                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onToolBarRightTextListener.onRight(view);
                    }
                });
            }
        }
    }

    public void setRightSureClick() {

    }

    public interface onToolBarRightImgListener{
        void onRight(View view);
    }

    public interface onToolBarRightTextListener{
        void onRight(View view);
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.ui("ui" + getClass().getSimpleName() + " onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            //执行冷启动处理
        }
    }

    @Override
    protected void onStop() {
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;//记录当前已经进入后台
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
    }

    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public void toast(String tips) {
        Toast.makeText(this, tips, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Loading对话框
     *
     * @param cancelable 是否可取消
     */
    protected void showProgress(boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDialog(this, cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setCancelable(cancelable);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SHARE_REQUESTCODE){
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }

}
