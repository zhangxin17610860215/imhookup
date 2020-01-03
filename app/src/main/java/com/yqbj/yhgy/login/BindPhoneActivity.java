package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.me.GenderSelectionAct;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.StringUtil;

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
    private Handler mHandler = new Handler();
    private Activity activity;
    private Runnable mRunnable;
    private int mSeconds = 30;
    private String phone = "";
    private String vfCode = "";
    private String psw = "";

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
        initView();
    }

    private void initView() {
        setToolbar(activity, 0, "");
    }

    private void countDown() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeconds--;
                if (mSeconds <= 0) {
                    tvSendVerCode.setText("重新获取验证码");
                    mRunnable = null;
                    mSeconds = 30;
                } else {
                    tvSendVerCode.setText(mSeconds + "s后重新获取");
                    mHandler.postDelayed(mRunnable, 1000);
                }
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @OnClick({R.id.tv_sendVerCode, R.id.tv_bindPhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendVerCode:
                //发送验证码
                if (null == mRunnable){
                    phone = etPhone.getText().toString().trim();
                    if (StringUtil.isEmpty(phone)){
                        toast("请先输入手机号");
                    }else {
                        getVfCode();
                    }
                }
                break;
            case R.id.tv_bindPhone:
                phone = etPhone.getText().toString().trim();
                vfCode = etVerCode.getText().toString().trim();
                psw = etPsw.getText().toString().trim();
                if (StringUtil.isEmpty(phone)){
                    toast("请输入手机号");
                    return;
                }
                if (StringUtil.isEmpty(vfCode)){
                    toast("请输入验证码");
                    return;
                }
                if (StringUtil.isEmpty(psw)){
                    toast("请输入密码");
                    return;
                }
                Constants.USER_ATTRIBUTE.PHONE = phone;
                Constants.USER_ATTRIBUTE.VFCODE = vfCode;
                Constants.USER_ATTRIBUTE.PSW = psw;
                GenderSelectionAct.start(activity);
                break;
        }
    }

    /**
     * 获取验证码
     * */
    private void getVfCode() {
        showProgress(false);
        UserApi.getVfCode("2", phone, activity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    countDown();
                }else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }


}
