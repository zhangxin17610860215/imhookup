package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.StringUtil;

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
    private String phone,psw;

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
                ForgetPasswordActivity.start(activity);
                break;
            case R.id.tv_login:
                //登录
                phone = etAccount.getText().toString().trim();
                psw = etPsw.getText().toString().trim();
                if (StringUtil.isEmpty(phone)){
                    toast("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(psw)){
                    toast("请输入密码");
                    return;
                }

                login();
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

    /**
     * 登录
     * */
    private void login() {
        showProgress(false);
        UserApi.login("1", phone, psw, "", "", "", activity, new RequestCallback() {
            @Override
            public void onSuccess(Object object) {
                dismissProgress();
                BindPhoneActivity.start(activity);
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
            }
        });
    }
}
