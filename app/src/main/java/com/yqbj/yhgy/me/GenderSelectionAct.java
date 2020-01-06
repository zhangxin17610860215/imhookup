package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.main.MainActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.UserPreferences;
import com.yqbj.yhgy.view.CautionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择的性别
 */
public class GenderSelectionAct extends BaseActivity {

    @BindView(R.id.img_male)
    ImageView imgMale;
    @BindView(R.id.tv_male)
    TextView tvMale;
    @BindView(R.id.img_female)
    ImageView imgFemale;
    @BindView(R.id.tv_female)
    TextView tvFemale;
    private Activity activity;
    /**
     * gender == 1 (男)
     * gender == 2 (女)
     * */
    private int gender = 1;
    private AbortableFuture<LoginInfo> loginRequest;

    public static void start(Context context) {
        Intent intent = new Intent(context, GenderSelectionAct.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genderselection_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");
        changeOptions();
    }

    @OnClick({R.id.img_male, R.id.img_female, R.id.tv_Determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_male:
                //选择男
                gender = 1;
                changeOptions();
                break;
            case R.id.img_female:
                //选择女
                gender = 2;
                changeOptions();
                break;
            case R.id.tv_Determine:
                //确定
                Constants.USER_ATTRIBUTE.GENDER = gender == 1 ? "1" : "2";
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new CautionDialog(activity, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //注册
                                signup(Constants.USER_ATTRIBUTE.SIGNUPTYPE);
                            }
                        }))
                        .show();
                break;
        }
    }

    /**
     * 注册
     * */
    private void signup(String signupType) {
        showProgress(false);
        UserApi.signup(signupType, Constants.USER_ATTRIBUTE.PHONE, Constants.USER_ATTRIBUTE.VFCODE,
                Constants.USER_ATTRIBUTE.PSW, Constants.USER_ATTRIBUTE.GENDER,
                Constants.USER_ATTRIBUTE.WXTOKEN, Constants.USER_ATTRIBUTE.OPENID, Constants.USER_ATTRIBUTE.WXUUID,
                activity, new RequestCallback() {
                    @Override
                    public void onSuccess(int code, Object object) {
                        dismissProgress();
                        if (code == Constants.SUCCESS_CODE){
                            //云信登录
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

    private void changeOptions() {
        if (gender == 1){
            //男
            tvMale.setTextColor(getResources().getColor(R.color.color_7265F6));
            imgMale.setImageResource(R.mipmap.male_yes);
            tvFemale.setTextColor(getResources().getColor(R.color.black));
            imgFemale.setImageResource(R.mipmap.female_no);
        }else {
            //女
            tvMale.setTextColor(getResources().getColor(R.color.black));
            imgMale.setImageResource(R.mipmap.male_no);
            tvFemale.setTextColor(getResources().getColor(R.color.color_7265F6));
            imgFemale.setImageResource(R.mipmap.female_yes);
        }
    }

    private void yunXinLogin() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = Preferences.getUserAccId();
        final String token = Preferences.getYunxinToken();
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new com.netease.nimlib.sdk.RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                onLoginDone();
                DemoCache.setAccount(account);
                // 初始化消息提醒配置
                initNotificationConfig();
                //跳转完善资料
                PerfectDataActivity.start(activity,"0");
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
            }

            @Override
            public void onException(Throwable exception) {
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
