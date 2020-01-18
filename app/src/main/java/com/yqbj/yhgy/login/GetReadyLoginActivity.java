package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.security.rp.RPSDK;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.main.MainActivity;
import com.yqbj.yhgy.utils.DemoCache;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 准备登录页面
 * */
public class GetReadyLoginActivity extends BaseActivity {

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private Activity activity;

    public static void start(Context context) {
        Intent intent = new Intent(context, GetReadyLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getreadylogin_layout);
        ButterKnife.bind(this);

        activity = this;
    }

    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_weixin, R.id.tv_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                //登录
                LoginActivity.start(activity);
//                authentication();
                break;
            case R.id.tv_register:
                //注册
                RegisterActivity.start(activity);
                break;
            case R.id.tv_weixin:
                //微信登录
//                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_qq:
                //QQ登录
//                MainActivity.start(activity);
                break;
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
            toast("再按一次退出约会公园");
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
