package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.view.VerifyingOKDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 欢迎页面
 */
public class WelcomActivity extends BaseActivity {

    @BindView(R.id.et_InvitationCode)
    EditText etInvitationCode;
    @BindView(R.id.tv_Determine)
    TextView tvDetermine;
    @BindView(R.id.tv_getInvitationCode)
    TextView tvGetInvitationCode;
    private Activity activity;

    public static void start(Context context) {
        Intent intent = new Intent(context, WelcomActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");
    }

    @OnClick({R.id.tv_Determine, R.id.tv_Apply, R.id.tv_getInvitationCode, R.id.tv_Opening, R.id.tv_Paid})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_Determine:
                //确定
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new VerifyingOKDialog(activity))
                        .show();
                break;
            case R.id.tv_Apply:
                //马上申请
                break;
            case R.id.tv_getInvitationCode:
                //查收邀请码
                break;
            case R.id.tv_Opening:
                //马上开通
                VipCoreActivity.start(activity);
                break;
            case R.id.tv_Paid:
                //已付费开通
                break;
        }
    }
}
