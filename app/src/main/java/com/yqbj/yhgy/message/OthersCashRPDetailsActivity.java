package com.yqbj.yhgy.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看别人发出去的虚拟币红包
 */
public class OthersCashRPDetailsActivity extends BaseActivity {

    @BindView(R.id.img_header)
    HeadImageView imgHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private Activity mActivity;

    public static void start(Context context) {
        Intent intent = new Intent(context, OthersCashRPDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherscashredpackagedetails_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
