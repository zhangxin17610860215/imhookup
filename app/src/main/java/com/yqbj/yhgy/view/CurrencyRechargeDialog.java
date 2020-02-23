package com.yqbj.yhgy.view;

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
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.bean.CurrencyPriceBean;
import com.yqbj.yhgy.utils.StringUtil;
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
    private List<CurrencyPriceBean> list;
    private String currencyNum = "";
    private GoPayListener listener;

    public CurrencyRechargeDialog(@NonNull Activity activity, List<CurrencyPriceBean> data, String currencyNum, GoPayListener listener) {
        super(activity);
        this.mActivity = activity;
        this.list = data;
        this.currencyNum = currencyNum;
        this.listener = listener;
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
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                CurrencyPriceBean currencyPriceBean = list.get(position);
                TextView tv_number = viewHolder.getView(R.id.tv_number);
                TextView tv_money = viewHolder.getView(R.id.tv_money);

                String value = currencyPriceBean.getValue();
                if (StringUtil.isNotEmpty(value) && value.contains("/")){
                    String[] split = value.split("/");
                    tv_number.setText(split[0] + "个");
                    tv_money.setText("¥  " + split[1]);
                    tv_money.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.goPay(split[1],currencyPriceBean.getId());
                            dismiss();
                        }
                    });
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    public interface GoPayListener{
        void goPay(String money,String id);
    }

}
