package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.SPUtils;
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
    private String gender = "";
    private String wxtoken = "";
    private String openid = "";
    private String uuid = "";

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
                gender = "1";
                signup("1");
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
            public void onSuccess(Object object) {
                dismissProgress();
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
            }
        });
    }

    /**
     * 注册
     * */
    private void signup(String signupType) {
        showProgress(false);
        UserApi.signup(signupType, phone, vfCode, psw, gender, wxtoken, openid, uuid,
                activity, new RequestCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        dismissProgress();
                        GenderSelectionAct.start(activity);
                    }

                    @Override
                    public void onFailed(String errMessage) {
                        dismissProgress();
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

                //拿到信息去绑定手机号
                BindPhoneActivity.start(activity);
//                signup("2");
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
