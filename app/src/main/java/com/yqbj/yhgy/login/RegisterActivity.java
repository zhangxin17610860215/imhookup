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

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.me.AgreementActivity;
import com.yqbj.yhgy.me.GenderSelectionAct;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.StringUtil;

import java.util.Map;

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

    private void countDown() {
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
                        getVfCode();
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
                Constants.USER_ATTRIBUTE.PHONE = phone;
                Constants.USER_ATTRIBUTE.VFCODE = vfCode;
                Constants.USER_ATTRIBUTE.PSW = psw;
                Constants.USER_ATTRIBUTE.SIGNUPTYPE = "1";
                GenderSelectionAct.start(activity);
                break;
            case R.id.tv_Agreement:
                //用户协议
                AgreementActivity.start(activity,"2");
                break;
            case R.id.img_weixin:
                //微信
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.img_qq:
                //QQ
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

    /**
     * 授权
     * */
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) { }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                toast("授权成功");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                Constants.USER_ATTRIBUTE.WXUUID = map.get("uid");
                Constants.USER_ATTRIBUTE.OPENID = map.get("openid");
                Constants.USER_ATTRIBUTE.WXTOKEN = map.get("access_token");
                Constants.USER_ATTRIBUTE.WXNAME = map.get("name");
                Constants.USER_ATTRIBUTE.WXHEADIMG = map.get("iconurl");
                Constants.USER_ATTRIBUTE.SIGNUPTYPE = "2";
                //拿到信息去绑定手机号
                BindPhoneActivity.start(activity);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                toast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                toast("取消授权");
            }
        });
    }

}
