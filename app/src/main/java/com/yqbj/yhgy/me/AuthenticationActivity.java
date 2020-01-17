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
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.view.MiddleTwoBtDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 认证方式
 */
public class AuthenticationActivity extends BaseActivity {

    @BindView(R.id.tv_RealPerson)
    TextView tvRealPerson;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_goddess)
    TextView tvGoddess;
    @BindView(R.id.img_authentication)
    ImageView imgAuthentication;

    private Activity mActivity;
    private int type = 1;       //类型    1=真人认证   2=女神认证
    private String gender = ""; //性别

    public static void start(Context context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        gender = Preferences.getGender();
        toggleSearchType();
    }

    private void initData() {

    }
    
    @OnClick({R.id.tv_RealPerson, R.id.tv_goddess, R.id.tv_Authentication})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_RealPerson:
                //真人认证
                type = 1;
                toggleSearchType();
                break;
            case R.id.tv_goddess:
                //女神认证
                type = 2;
                toggleSearchType();
                break;
            case R.id.tv_Authentication:
                //立即认证
                if (type == 2){
                    if (gender.equals("1")){
                        toast("该功能仅对女性开放");
                        return;
                    }
                    if (Preferences.getCertification().equals("0")){
                        //未真人认证
                        new XPopup.Builder(mActivity)
                                .dismissOnTouchOutside(false)
                                .asCustom(new MiddleTwoBtDialog(mActivity, "", "只有真人认证之后才可以进行女神认证，是否要前往真人认证？", "真人认证", "取消认证",
                                        new MiddleTwoBtDialog.ClickListener() {
                                            @Override
                                            public void determine() {
                                                type = 1;
                                                toggleSearchType();
                                            }

                                            @Override
                                            public void cancel() {
                                                finish();
                                            }
                                        }))
                                .show();
                        return;
                    }
                    GoddessCertificationActivity.start(mActivity);
                    finish();
                }else if (type == 1){
                    RealPersonCertificationActivity.start(mActivity);
                    finish();
                }
                break;
        }
    }

    private void toggleSearchType() {
        if (gender.equals("1")){
            tvTitle.setText("真人认证");
            tvRealPerson.setVisibility(View.GONE);
            tvGoddess.setVisibility(View.GONE);
            imgAuthentication.setImageResource(R.mipmap.realperson_img_logo);
            return;
        }
        tvRealPerson.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvGoddess.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvRealPerson.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.line_not_selected));
        tvGoddess.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.line_not_selected));
        switch (type) {
            case 1:
                tvRealPerson.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvRealPerson.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                imgAuthentication.setImageResource(R.mipmap.realperson_img_logo);
                break;
            case 2:
                tvGoddess.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvGoddess.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                imgAuthentication.setImageResource(R.mipmap.goddess_img_logo);
                break;
        }
    }
}
