package com.yqbj.yhgy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.login.GetReadyLoginActivity;

/**
 * 开屏页
 * */
public class SplashActivity extends BaseActivity {

    private Activity activity;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        handler.postDelayed(readyShow, 2000);
    }

    Runnable readyShow = new Runnable() {
        @Override
        public void run() {
            GetReadyLoginActivity.start(activity);
        }
    };
}
