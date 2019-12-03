package com.yuanqi.hangzhou.imhookup.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * 底部弹窗(虚拟币充值)
 * */
public class CurrencyRechargeDialog extends BottomPopupView {

    private Activity mActivity;
    private TextView tv_currencyNum;        //可用余额
    private RecyclerView mRecyclerView;

    private EasyRVAdapter mAdapter;
    private List<String> list;
    private String currencyNum = "";

    public CurrencyRechargeDialog(@NonNull Activity activity, List<String> data, String currencyNum) {
        super(activity);
        this.mActivity = activity;
        this.list = data;
        this.currencyNum = currencyNum;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_currencyrecharge_layout;
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
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    private void initData() {
        tv_currencyNum.setText(currencyNum);

        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.item_currencyrecharge_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                TextView tv_number = viewHolder.getView(R.id.tv_number);
                TextView tv_money = viewHolder.getView(R.id.tv_money);
                tv_money.setText("¥  " + list.get(position));
                tv_money.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPayMode(list.get(position),v);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 显示支付方式弹窗
     * */
    private void showPayMode(String money, View v) {
        final PaySelect paySelect = new PaySelect(mActivity,money,"红包",money,2);
        new XPopup.Builder(mActivity)
                .atView(v)
                .asCustom(paySelect)
                .show();
        paySelect.setOnClickListenerOnSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即支付
                PaySelect.SelectPayType type = paySelect.getCurrSeletPayType();
                int payType = 1;
                switch (type) {
                    case ALI:
                        //支付宝支付
                        payType = 3;
                        break;
                    case WCHAT:
                        //微信支付
                        payType = 2;
                        break;
                    case WALLET:
                        //钱包支付
                        payType = 1;
                        break;
                }
                if (!NoDoubleClickUtils.isDoubleClick(2000)){
//                    getRedPageId(amount,payType);
                    ToastHelper.showToast(mActivity,payType == 3 ? "支付宝支付" : "微信支付");
                    paySelect.dismiss();
                }
            }
        });
    }

}
