package com.yuanqi.hangzhou.imhookup.view;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * 底部弹窗(虚拟币支付)
 * */
public class CurrencyPayDialog extends BottomPopupView implements View.OnClickListener {

    private Activity mActivity;
    private TextView tv_currencyNum;        //可用余额
    private ImageView img_Refresh;          //刷新余额
    private TextView tv_title;              //支付项目
    private TextView tv_payNum;             //支付总额
    private TextView tv_pay;                //支付
    private TextView tv_Recharge;           //充值

    /**
     * type==1    约会币红包
     * */
    private int type;

    public CurrencyPayDialog(@NonNull Activity activity, int type) {
        super(activity);
        this.mActivity = activity;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_currencypay_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tv_currencyNum = findViewById(R.id.tv_currencyNum);
        img_Refresh = findViewById(R.id.img_Refresh);
        tv_title = findViewById(R.id.tv_title);
        tv_payNum = findViewById(R.id.tv_payNum);
        tv_pay = findViewById(R.id.tv_pay);
        tv_Recharge = findViewById(R.id.tv_Recharge);

        if (type == 1){
            tv_title.setText("约会币红包");
        }

        img_Refresh.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        tv_Recharge.setOnClickListener(this);

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_Refresh:
                //刷新
                break;
            case R.id.tv_pay:
                //支付
                Intent intent = new Intent();
                intent.putExtra("redId", "redId");
                intent.putExtra("redTitle", "redTitle");
                intent.putExtra("redContent", "小小意思,拿去浪吧");
                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.finish();
                break;
            case R.id.tv_Recharge:
                //充值
                break;
        }
    }
}
