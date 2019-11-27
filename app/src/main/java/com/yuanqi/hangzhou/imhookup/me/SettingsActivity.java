package com.yuanqi.hangzhou.imhookup.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

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

    }

    @OnClick({R.id.tv_MessageNotification, R.id.rl_Phone, R.id.tv_SetPassword, R.id.tv_Agreement, R.id.rl_ClearCache, R.id.tv_Logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_MessageNotification:
                //新消息通知
                break;
            case R.id.rl_Phone:
                //手机号码
                break;
            case R.id.tv_SetPassword:
                //修改密码
                break;
            case R.id.tv_Agreement:
                //用户使用协议
                break;
            case R.id.rl_ClearCache:
                //清理缓存
                break;
            case R.id.tv_Logout:
                //退出登录
                break;
        }
    }
}
