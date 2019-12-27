package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.SPUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.misc.DirCacheFileType;
import com.netease.nimlib.sdk.misc.MiscService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.SplashActivity;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.login.BindPhoneActivity;
import com.yqbj.yhgy.login.ModifyPasswordActivity;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.view.EasyAlertDialogHelper;
import com.yqbj.yhgy.view.MiddleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.tv_Phone)
    TextView tvPhone;
    @BindView(R.id.tv_CacheSize)
    TextView tvCacheSize;

    private Activity mActivity;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
    }

    private void initData() {
        getSDKDirCacheSize();
    }

    @OnClick({R.id.tv_MessageNotification, R.id.rl_Phone, R.id.tv_SetPassword, R.id.tv_Standard, R.id.tv_Privacy, R.id.tv_Agreement, R.id.rl_ClearCache, R.id.tv_Logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_MessageNotification:
                //新消息通知
                break;
            case R.id.rl_Phone:
                //手机号码
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new MiddleDialog(mActivity, "", "你想修改绑定的手机号吗?", new MiddleDialog.Listener() {
                            @Override
                            public void onConfirmClickListener() {
                                BindPhoneActivity.start(mActivity);
                            }

                            @Override
                            public void onCloseClickListener() {

                            }
                        }))
                        .show();
                break;
            case R.id.tv_SetPassword:
                //修改密码
                ModifyPasswordActivity.start(mActivity);
                break;
            case R.id.tv_Standard:
                //平台使用规范
                AgreementActivity.start(mActivity,"1");
                break;
            case R.id.tv_Agreement:
                //用户使用协议
                AgreementActivity.start(mActivity,"2");
                break;
            case R.id.tv_Privacy:
                //用户隐私政策
                AgreementActivity.start(mActivity,"3");
                break;
            case R.id.rl_ClearCache:
                //清理缓存
                clearSDKDirCache();
                break;
            case R.id.tv_Logout:
                //退出登录
                EasyAlertDialogHelper.showCommonDialog(SettingsActivity.this, null, "确定要退出吗？", "确定", "取消", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        logout();
                    }
                }).show();
                break;
        }
    }

    private void getSDKDirCacheSize() {
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);
        NIMClient.getService(MiscService.class).getSizeOfDirCache(types, 0, 0).setCallback(new RequestCallbackWrapper<Long>() {
            @Override
            public void onResult(int code, Long result, Throwable exception) {
                tvCacheSize.setText(String.format("%.2f M", result / (1024.0f * 1024.0f)));
            }
        });
    }

    private void clearSDKDirCache() {
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);

        NIMClient.getService(MiscService.class).clearDirCache(types, 0, 0).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
//                clearSDKDirCacheItem.setDetail("0.00 M");
//                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 注销
     */
    private void logout() {
        SplashActivity.start(mActivity,null);
//        SPUtils.getInstance(Constants.ALIPAY_USERINFO.FILENAME).clear();
        NimUIKit.logout();
        NIMClient.getService(AuthService.class).logout();
        finish();
    }

}
