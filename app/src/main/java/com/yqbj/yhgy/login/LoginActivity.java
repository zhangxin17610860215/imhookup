package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.main.MainActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.utils.UserPreferences;

import java.util.Map;

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
    private String phone,psw,loginType,wxToken,wxOpenId,wxUUID;
    private AbortableFuture<LoginInfo> loginRequest;

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
                loginType = "1";
                login();
                break;
            case R.id.tv_weixin:
                //微信
                loginType = "2";
                authorization(SHARE_MEDIA.WEIXIN);
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
     * 授权
     * */
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(activity, share_media, new UMAuthListener() {
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
                wxOpenId = map.get("openid");
                wxToken = map.get("access_token");
                wxUUID = map.get("uid");
                //拿到信息去登录
                login();
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

    /**
     * 登录
     * */
    private void login() {
        showProgress(false);
        UserApi.login(loginType, phone,psw, wxOpenId, wxToken, wxUUID, activity, new RequestCallback() {
                    @Override
                    public void onSuccess(int code, Object object) {
                        dismissProgress();
                        if (code == Constants.SUCCESS_CODE){
                            yunXinLogin();
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

    private void yunXinLogin() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        showProgress(false);
        final String account = Preferences.getUserAccId();
        final String token = Preferences.getYunxinToken();
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new com.netease.nimlib.sdk.RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                dismissProgress();
                onLoginDone();
                DemoCache.setAccount(account);
                // 初始化消息提醒配置
                initNotificationConfig();
                // 进入主界面
                MainActivity.start(activity);
            }

            @Override
            public void onFailed(int code) {
                dismissProgress();
                onLoginDone();
            }

            @Override
            public void onException(Throwable exception) {
                dismissProgress();
                onLoginDone();
            }
        });
    }

    private void onLoginDone() {
        loginRequest = null;
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

}
