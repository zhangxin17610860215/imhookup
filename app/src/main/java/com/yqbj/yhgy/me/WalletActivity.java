package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.home.DetailsActivity;
import com.yqbj.yhgy.view.BindAliPayDialog;
import com.yqbj.yhgy.view.CurrencyRechargeDialog;
import com.yqbj.yhgy.view.CurrencyWithdrawalDialog;
import com.yqbj.yhgy.view.MyRefreshLayout;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.tv_CashWithdrawal)
    TextView tvCashWithdrawal;
    @BindView(R.id.tv_CurrencyRecharge)
    TextView tvCurrencyRecharge;
    @BindView(R.id.tv_CurrencyWithdrawal)
    TextView tvCurrencyWithdrawal;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.tv_cashTotal)
    TextView tvCashTotal;
    @BindView(R.id.tv_withdrawalMoney)
    TextView tvWithdrawalMoney;
    @BindView(R.id.tv_bindAliPayCash)
    TextView tvBindAliPayCash;
    @BindView(R.id.rv_Cash)
    RecyclerView rvCash;
    @BindView(R.id.ll_cash)
    LinearLayout llCash;
    @BindView(R.id.tv_currencyTotal)
    TextView tvCurrencyTotal;
    @BindView(R.id.tv_withdrawalCurrency)
    TextView tvWithdrawalCurrency;
    @BindView(R.id.tv_bindAliPayCurrency)
    TextView tvBindAliPayCurrency;
    @BindView(R.id.rv_currency)
    RecyclerView rvCurrency;
    @BindView(R.id.ll_currency)
    LinearLayout llCurrency;
    @BindView(R.id.refresh_layout)
    MyRefreshLayout refreshLayout;

    private Activity mActivity;
    private String alpayAccount = "";
    private String alpayName = "";
    private int type = 1;//类型    1=现金   2=约会币
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        toggleSearchType();
        rvCash.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvCurrency.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多
            }
        });
    }

    private void initData() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        mAdapter = new EasyRVAdapter(mActivity, list, R.layout.wallet_list_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                RoundedImageView imgHeader = viewHolder.getView(R.id.img_header);
                TextView tvNumber = viewHolder.getView(R.id.tv_number);
                TextView tvTitle = viewHolder.getView(R.id.tv_title);
                TextView tvTime = viewHolder.getView(R.id.tv_time);
                Glide.with(mActivity).load(list.get(position)).error(R.mipmap.default_home_head).into(imgHeader);
                if (type == 1){
                    tvNumber.setText("-" + list.get(position) + "现金");
                    tvNumber.setTextColor(getResources().getColor(R.color.redpackageColor));
                }else {
                    tvNumber.setText("+" + list.get(position) + "约会币");
                    tvNumber.setTextColor(getResources().getColor(R.color.text_theme_color));
                }
                imgHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailsActivity.start(mActivity,"","","","");
                    }
                });
            }
        };
        RecyclerView mRecyclerView = type == 1 ? rvCash : rvCurrency;
        mRecyclerView.setAdapter(mAdapter);
    }

    private void toggleSearchType() {
        mAdapter = null;
        tvCash.setTextColor(getResources().getColor(R.color.black));
        tvCurrency.setTextColor(getResources().getColor(R.color.black));
        tvCashWithdrawal.setVisibility(View.GONE);
        tvCurrencyRecharge.setVisibility(View.GONE);
        tvCurrencyWithdrawal.setVisibility(View.GONE);
        switch (type){
            case 1:
                tvCash.setTextColor(getResources().getColor(R.color.text_theme_color));
                llCash.setVisibility(View.VISIBLE);
                llCurrency.setVisibility(View.GONE);
                tvCashWithdrawal.setVisibility(View.VISIBLE);
                tvCurrencyRecharge.setVisibility(View.GONE);
                tvCurrencyWithdrawal.setVisibility(View.GONE);
                break;
            case 2:
                tvCurrency.setTextColor(getResources().getColor(R.color.text_theme_color));
                llCurrency.setVisibility(View.VISIBLE);
                llCash.setVisibility(View.GONE);
                tvCashWithdrawal.setVisibility(View.GONE);
                tvCurrencyRecharge.setVisibility(View.VISIBLE);
                tvCurrencyWithdrawal.setVisibility(View.VISIBLE);
                break;
        }
        list.clear();
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        initData();
    }

    @OnClick({R.id.tv_cash, R.id.tv_CashWithdrawal, R.id.tv_CurrencyRecharge, R.id.tv_CurrencyWithdrawal, R.id.tv_currency, R.id.tv_bindAliPayCash, R.id.tv_bindAliPayCurrency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_CashWithdrawal:
                //现金提现
                break;
            case R.id.tv_CurrencyRecharge:
                //约会币充值
                List<String> list = new ArrayList<>();
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
                list.add("6");
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new CurrencyRechargeDialog(mActivity,list,"99"))
                        .show();
                break;
            case R.id.tv_CurrencyWithdrawal:
                //约会币提现
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new CurrencyWithdrawalDialog(mActivity,"40","2","19","1"))
                        .show();
                break;
            case R.id.tv_cash:
                //现金
                if (type != 1){
                    type = 1;
                    toggleSearchType();
                }
                break;
            case R.id.tv_currency:
                //约会币
                if (type != 2){
                    type = 2;
                    toggleSearchType();
                }
                break;
            case R.id.tv_bindAliPayCash:
                //现金绑定支付宝
            case R.id.tv_bindAliPayCurrency:
                //约会币绑定支付宝
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new BindAliPayDialog(mActivity, alpayAccount, alpayName, new BindAliPayDialog.ConfirmOnClickListener() {
                            @Override
                            public void onClick(View v, String account, String Name) {
                                alpayAccount = account;
                                alpayName = Name;
                                tvBindAliPayCurrency.setText(alpayAccount + "(" + alpayName + ")");
                                tvBindAliPayCash.setText(alpayAccount + "(" + alpayName + ")");
                            }
                        }))
                        .show();
                break;
        }
    }
}
