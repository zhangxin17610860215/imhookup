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
import com.yqbj.yhgy.me.AgreementActivity;
import com.yqbj.yhgy.me.GenderSelectionAct;
import com.yqbj.yhgy.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_send_vfCode)
    TextView tvSendVfCode;
    @BindView(R.id.et_vfCode)
    EditText etVfCode;
    @BindView(R.id.et_setPsw)
    EditText etSetPsw;

    private Activity activity;
    private Handler mHandler = new Handler();

    private Runnable mRunnable;
    private int mSeconds = 30;
    private String phone = "";
    private String vfCode = "";
    private String psw = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);
        ButterKnife.bind(this);

        activity = this;

        initView();
    }

    private void initView() {
        setToolbar(activity, 0, "");
    }

    private void initData() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeconds--;
                if (mSeconds <= 0) {
                    tvSendVfCode.setText("重新获取验证码");
                    mRunnable = null;
                    mSeconds = 30;
                } else {
                    tvSendVfCode.setText(mSeconds + "s后重新获取");

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

    @OnClick({R.id.tv_send_vfCode, R.id.tv_Next, R.id.tv_Agreement, R.id.img_weixin, R.id.img_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_vfCode:
                //发送验证码
                if (null == mRunnable){
                    phone = etPhone.getText().toString().trim();
                    if (StringUtil.isEmpty(phone)){
                        toast("请先输入手机号");
                    }else {
                        initData();
                    }
                }
                break;
            case R.id.tv_Next:
                //下一步
                phone = etPhone.getText().toString().trim();
                vfCode = etVfCode.getText().toString().trim();
                psw = etSetPsw.getText().toString().trim();
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
                GenderSelectionAct.start(activity);
                break;
            case R.id.tv_Agreement:
                //用户协议
                AgreementActivity.start(activity,"2");
                break;
            case R.id.img_weixin:
                //微信
                break;
            case R.id.img_qq:
                //QQ
                break;
        }
    }
}
