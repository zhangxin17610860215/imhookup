package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.view.MiddleDialog;
import com.yqbj.yhgy.view.SettingSeeMoneyDialog;

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
                if (!publicSwitch){
                    personalDetailsSwitchSetting(1);
                }
                break;
            case R.id.tv_Album:
                //相册付费
                if (!albumSwitch){
                    personalDetailsSwitchSetting(2);
                }
                break;
            case R.id.tv_Verification:
                //查看前需通过我的验证
                if (!verificationSwitch){
                    personalDetailsSwitchSetting(3);
                }
                break;
            case R.id.tv_ListHide:
                //在公园列表隐藏我
                listHideSwitch = !listHideSwitch;
                switchSetting(tvListHide,listHideSwitch,4);
                break;
            case R.id.tv_HideDistance:
                //对他人隐藏我的距离
                hideDistanceSwitch = !hideDistanceSwitch;
                switchSetting(tvHideDistance,hideDistanceSwitch,5);
                break;
            case R.id.tv_HideTime:
                //对他人隐藏我的在线时间
                hideTimeSwitch = !hideTimeSwitch;
                switchSetting(tvHideTime,hideTimeSwitch,6);
                break;
            case R.id.tv_HideAccount:
                //对他人隐藏我的社交账号
                hideAccountSwitch = !hideAccountSwitch;
                switchSetting(tvHideAccount,hideAccountSwitch,7);
                break;
        }
    }

    /**
     * 个人详情开关设置
     * */
    private void personalDetailsSwitchSetting(int switchType) {
        switch (switchType){
            case 1:
                publicSwitch = true;
                albumSwitch = false;
                verificationSwitch = false;
                tvPublic.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.selected_logo), null);
                tvAlbum.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                tvVerification.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                break;
            case 2:
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new MiddleDialog(mActivity, "", getString(R.string.album_pay_dialog_content), new MiddleDialog.Listener() {
                            @Override
                            public void onConfirmClickListener() {
                                new XPopup.Builder(mActivity)
                                        .dismissOnTouchOutside(false)
                                        .asCustom(new SettingSeeMoneyDialog(mActivity, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                publicSwitch = false;
                                                albumSwitch = true;
                                                verificationSwitch = false;
                                                tvPublic.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                                                tvAlbum.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.selected_logo), null);
                                                tvVerification.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                                            }
                                        })).show();
                            }

                            @Override
                            public void onCloseClickListener() {

                            }
                        }))
                        .show();
                break;
            case 3:
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new MiddleDialog(mActivity, "", getString(R.string.verification_dialog_content), new MiddleDialog.Listener() {
                            @Override
                            public void onConfirmClickListener() {
                                publicSwitch = false;
                                albumSwitch = false;
                                verificationSwitch = true;
                                tvPublic.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                                tvAlbum.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.unselected_logo), null);
                                tvVerification.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.selected_logo), null);
                            }

                            @Override
                            public void onCloseClickListener() {

                            }
                        }))
                        .show();
                break;
        }
    }

    /**
     * 其他开关设置
     * */
    private void switchSetting(TextView textView, boolean isSwitchType, int switchType) {
        switch (switchType){
            case 4:
                //在公园列表隐藏我
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(isSwitchType ? R.mipmap.isshow_open : R.mipmap.isshow_close), null);
                break;
            case 5:
                //对他人隐藏我的距离
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(isSwitchType ? R.mipmap.isshow_open : R.mipmap.isshow_close), null);
                break;
            case 6:
                //对他人隐藏我的在线时间
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(isSwitchType ? R.mipmap.isshow_open : R.mipmap.isshow_close), null);
                break;
            case 7:
                //对他人隐藏我的社交账号
                textView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(isSwitchType ? R.mipmap.isshow_open : R.mipmap.isshow_close), null);
                break;
            default:
                break;
        }
    }


}
