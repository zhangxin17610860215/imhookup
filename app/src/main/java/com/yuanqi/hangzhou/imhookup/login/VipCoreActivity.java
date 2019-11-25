package com.yuanqi.hangzhou.imhookup.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会员中心页面
 * TODO   暂不确定是不是固定布局    会员套餐是否是列表
 */
public class VipCoreActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private Activity activity;

    public static void start(Context context) {
        Intent intent = new Intent(context, VipCoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipcore_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");
    }

    @OnClick(R.id.tv_Opening)
    public void onViewClicked() {
        //立即开通

    }
}
