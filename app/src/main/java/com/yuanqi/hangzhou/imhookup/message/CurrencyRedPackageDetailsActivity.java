package com.yuanqi.hangzhou.imhookup.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看自己发出去的虚拟币红包详情页面
 */
public class CurrencyRedPackageDetailsActivity extends BaseActivity {

    @BindView(R.id.img_header)
    HeadImageView imgHeader;
    @BindView(R.id.tv_sendName)
    TextView tvSendName;
    @BindView(R.id.tv_redPackaheDetails)
    TextView tvRedPackaheDetails;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_Progress)
    TextView tvProgress;

    private Activity mActivity;

    public static void start(Context context) {
        Intent intent = new Intent(context, CurrencyRedPackageDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyredpackagedetails_activity_layout);
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
