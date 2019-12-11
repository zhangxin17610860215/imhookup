package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.view.PaySelect;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

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
    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private String amount = "";         //选择套餐的金额
    private int pos;                    //选择套餐的索引

    public static void start(Context context) {
        Intent intent = new Intent(context, VipCoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vipcore_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();

    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        mAdapter = new EasyRVAdapter(mActivity, list, R.layout.vipsetmeal_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                ImageView imgVipSetmealBg = viewHolder.getView(R.id.img_vipsetmeal_bg);
                TextView tvTime = viewHolder.getView(R.id.tv_Time);
                TextView tvAmount = viewHolder.getView(R.id.tv_Amount);
                TextView tvCompany = viewHolder.getView(R.id.tv_Company);
                if (StringUtil.isEmpty(amount)){
                    //第一次进入该页面   默认选择第三个套餐
                    if (list.get(position).equals("3")){
                        imgVipSetmealBg.setImageResource(R.mipmap.vipselection_logo);
                        tvAmount.setTextColor(getResources().getColor(R.color.white));
                        tvCompany.setTextColor(getResources().getColor(R.color.white));
                        amount = list.get(position);
                        pos = position;
                    }else {
                        imgVipSetmealBg.setImageResource(R.mipmap.vipselection_no_logo);
                        tvAmount.setTextColor(getResources().getColor(R.color.black));
                        tvCompany.setTextColor(getResources().getColor(R.color.black));
                    }
                }else{
                    if (position == pos){
                        imgVipSetmealBg.setImageResource(R.mipmap.vipselection_logo);
                        tvAmount.setTextColor(getResources().getColor(R.color.white));
                        tvCompany.setTextColor(getResources().getColor(R.color.white));
                        amount = list.get(position);
                        pos = position;
                    }else {
                        imgVipSetmealBg.setImageResource(R.mipmap.vipselection_no_logo);
                        tvAmount.setTextColor(getResources().getColor(R.color.black));
                        tvCompany.setTextColor(getResources().getColor(R.color.black));
                    }
                }
                tvTime.setText(list.get(position) + "个月");
                tvAmount.setText(list.get(position));
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                mAdapter.notifyDataSetChanged();
                ImageView imgVipSetmealBg = view.findViewById(R.id.img_vipsetmeal_bg);
                TextView tvAmount = view.findViewById(R.id.tv_Amount);
                TextView tvCompany = view.findViewById(R.id.tv_Company);
                imgVipSetmealBg.setImageResource(R.mipmap.vipselection_logo);
                tvAmount.setTextColor(getResources().getColor(R.color.white));
                tvCompany.setTextColor(getResources().getColor(R.color.white));
                amount = list.get(position);
                pos = position;
            }
        });
    }

    @OnClick(R.id.tv_Opening)
    public void onViewClicked(View view) {
        //立即开通
        if (StringUtil.isEmpty(amount)){
            toast("请选择会员套餐");
            return;
        }
        showPayMode(view);
    }

    /**
     * 显示支付方式弹窗
     * */
    private void showPayMode(View v) {
        final PaySelect paySelect = new PaySelect(mActivity,amount,"会员套餐",amount,2);
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
                    toast(payType == 3 ? "支付宝支付" : "微信支付");
                    paySelect.dismiss();
                }
            }
        });
    }
}
