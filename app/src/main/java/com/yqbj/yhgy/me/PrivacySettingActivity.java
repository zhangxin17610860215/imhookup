package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 隐私设置
 */
public class PrivacySettingActivity extends BaseActivity {

    @BindView(R.id.tv_public)
    TextView tvPublic;
    @BindView(R.id.tv_Album)
    TextView tvAlbum;
    @BindView(R.id.tv_Verification)
    TextView tvVerification;
    @BindView(R.id.tv_ListHide)
    TextView tvListHide;
    @BindView(R.id.tv_HideDistance)
    TextView tvHideDistance;
    @BindView(R.id.tv_HideTime)
    TextView tvHideTime;
    @BindView(R.id.tv_HideAccount)
    TextView tvHideAccount;

    private Activity mActivity;
    private boolean publicSwitch;               //公开
    private boolean albumSwitch;                //相册付费
    private boolean verificationSwitch;         //查看前需通过我的验证
    private boolean listHideSwitch;             //在公园列表隐藏我
    private boolean hideDistanceSwitch;         //对他人隐藏我的距离
    private boolean hideTimeSwitch;             //对他人隐藏我的在线时间
    private boolean hideAccountSwitch;          //对他人隐藏我的社交账号

    public static void start(Context context) {
        Intent intent = new Intent(context, PrivacySettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacysetting_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
    }

    private void initData() {
        publicSwitch = true;
    }

    @OnClick({R.id.tv_public, R.id.tv_Album, R.id.tv_Verification, R.id.tv_ListHide, R.id.tv_HideDistance, R.id.tv_HideTime, R.id.tv_HideAccount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_public:
                //公开
                publicSwitch = !publicSwitch;
                switchSetting(tvPublic,publicSwitch,1);
                break;
            case R.id.tv_Album:
                //相册付费
                albumSwitch = !albumSwitch;
                switchSetting(tvAlbum,albumSwitch,1);
                break;
            case R.id.tv_Verification:
                //查看前需通过我的验证
                verificationSwitch = !verificationSwitch;
                switchSetting(tvVerification,verificationSwitch,1);
                break;
            case R.id.tv_ListHide:
                //在公园列表隐藏我
                listHideSwitch = !listHideSwitch;
                switchSetting(tvListHide,listHideSwitch,2);
                break;
            case R.id.tv_HideDistance:
                //对他人隐藏我的距离
                hideDistanceSwitch = !hideDistanceSwitch;
                switchSetting(tvHideDistance,hideDistanceSwitch,2);
                break;
            case R.id.tv_HideTime:
                //对他人隐藏我的在线时间
                hideTimeSwitch = !hideTimeSwitch;
                switchSetting(tvHideTime,hideTimeSwitch,2);
                break;
            case R.id.tv_HideAccount:
                //对他人隐藏我的社交账号
                hideAccountSwitch = !hideAccountSwitch;
                switchSetting(tvHideAccount,hideAccountSwitch,2);
                break;
        }
    }

    /**
     * 开关设置
     * */
    private void switchSetting(TextView textView, boolean isSwitchType, int switchType) {
        textView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(switchType == 1 ? isSwitchType ? R.mipmap.selected_logo : R.mipmap.unselected_logo : isSwitchType ? R.mipmap.isshow_open : R.mipmap.isshow_close), null);
    }


}
