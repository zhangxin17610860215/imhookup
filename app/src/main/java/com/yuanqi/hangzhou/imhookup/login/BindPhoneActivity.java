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
 * 绑定手机号页面
 * */
public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_sendVerCode)
    TextView tvSendVerCode;
    @BindView(R.id.et_verCode)
    EditText etVerCode;
    @BindView(R.id.et_psw)
    EditText etPsw;
    private Activity activity;

    public static void start(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindphone_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");
    }

    @OnClick({R.id.tv_sendVerCode, R.id.tv_bindPhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendVerCode:
                break;
            case R.id.tv_bindPhone:
                WelcomActivity.start(activity);
                break;
        }
    }
}
