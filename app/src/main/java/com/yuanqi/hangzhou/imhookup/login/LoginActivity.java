package com.yuanqi.hangzhou.imhookup.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_Account)
    EditText etAccount;
    @BindView(R.id.tv_forgetPsw)
    TextView tvForgetPsw;
    @BindView(R.id.et_psw)
    EditText etPsw;
    private Activity activity;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity,0,"");
    }

    @OnClick({R.id.tv_login, R.id.tv_weixin, R.id.tv_QQ, R.id.tv_weibo, R.id.tv_forgetPsw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPsw:
                //忘记密码
                break;
            case R.id.tv_login:
                //登录
                BindPhoneActivity.start(activity);
                break;
            case R.id.tv_weixin:
                //微信
                break;
            case R.id.tv_QQ:
                //QQ
                break;
            case R.id.tv_weibo:
                //微博
                break;
        }
    }
}
