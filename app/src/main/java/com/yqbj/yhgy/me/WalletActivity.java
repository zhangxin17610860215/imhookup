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
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.CurrencyPriceBean;
import com.yqbj.yhgy.bean.PayInfoBean;
import com.yqbj.yhgy.bean.WalletBalanceBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.home.DetailsActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.utils.pay.MyALipayUtils;
import com.yqbj.yhgy.view.BindAliPayDialog;
import com.yqbj.yhgy.view.CurrencyRechargeDialog;
import com.yqbj.yhgy.view.CurrencyWithdrawalDialog;
import com.yqbj.yhgy.view.MyRefreshLayout;
import com.yqbj.yhgy.view.PaySelect;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yqbj.yhgy.MyApplication.ALIPAY_APPID;

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
    private String currencyWithdrawal = "";
    private String currencyTotal = "";
    private String cashTotal = "";
    private String cashWithdrawal = "";
    private int type = 1;//类型    1=现金   2=约会币
    private int pageNum = 1;
    private int pageSize = 20;
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
        initData();
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
        showProgress(false);
        UserApi.getBalance(mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                if (code == Constants.SUCCESS_CODE){
                    WalletBalanceBean balanceBean = (WalletBalanceBean) object;
                    currencyTotal = balanceBean.getUnassignableTotalCurrency()+"";
                    currencyWithdrawal = balanceBean.getCurrency()+"";
                    cashWithdrawal  = balanceBean.getMoney();
                    cashTotal = balanceBean.getUnassignableTotalMoney();

                    tvCashTotal.setText(String.valueOf(NumberUtil.add(cashWithdrawal,cashTotal)));
                    tvWithdrawalMoney.setText(cashWithdrawal);
                    tvCurrencyTotal.setText(String.valueOf(NumberUtil.add(currencyWithdrawal,currencyTotal)));
                    tvWithdrawalCurrency.setText(currencyWithdrawal);

                    if (StringUtil.isNotEmpty(balanceBean.getAliAccount()) && StringUtil.isNotEmpty(balanceBean.getAliRealName())){
                        alpayAccount = balanceBean.getAliAccount();
                        alpayName = balanceBean.getAliRealName();
                        tvBindAliPayCurrency.setText(alpayAccount + "(" + alpayName + ")");
                        tvBindAliPayCash.setText(alpayAccount + "(" + alpayName + ")");
                    }
                }else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                toast(errMessage);
            }
        });

        UserApi.getOrderList(type, pageNum, pageSize, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    lodeData();
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    private void lodeData() {
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
                        DetailsActivity.start(mActivity,"");
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
                if (!NumberUtil.compareGreater(cashWithdrawal,"0")){
                    toast("可提现的余额不足");
                    return;
                }
                withdrawDeposit();
                break;
            case R.id.tv_CurrencyRecharge:
                //约会币充值
                showRechargeDialog(view);
                break;
            case R.id.tv_CurrencyWithdrawal:
                //约会币提现
                try {
                    if (!NumberUtil.compareGreater(currencyWithdrawal,"20")){
                        toast("可提现的约会币数量不足");
                        return;
                    }
                    String str = NumberUtil.div_Intercept(currencyWithdrawal, "20", 0);
                    int surplus = NumberUtil.toInt(currencyWithdrawal) % 20;
                    new XPopup.Builder(mActivity)
                            .dismissOnTouchOutside(false)
                            .asCustom(new CurrencyWithdrawalDialog(mActivity, currencyWithdrawal, str, surplus + "", "1", new CurrencyWithdrawalDialog.WithdrawDepositCallback() {
                                @Override
                                public void onCallback() {
                                    pageNum = 1;
                                    initData();
                                }
                            }))
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                            public void onClick(View v, String account, String name) {
                                bindALiAccount(account,name);
                            }
                        }))
                        .show();
                break;
        }
    }

    private void withdrawDeposit() {
        showProgress(false);
        UserApi.withdrawDeposit("9", tvWithdrawalMoney.getText().toString(), "2", "2", mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    /**
     * 绑定支付宝账户
     * */
    private void bindALiAccount(String account, String name) {
        showProgress(false);
        UserApi.bindALiAccount(account, name, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    alpayAccount = account;
                    alpayName = name;
                    tvBindAliPayCurrency.setText(alpayAccount + "(" + alpayName + ")");
                    tvBindAliPayCash.setText(alpayAccount + "(" + alpayName + ")");
                }else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    /**
     * 显示虚拟币充值弹窗
     * */
    private void showRechargeDialog(View view) {
        showProgress(false);
        UserApi.getCurrencyPriceList(mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    List<CurrencyPriceBean> priceList = (List<CurrencyPriceBean>) object;
                    new XPopup.Builder(mActivity)
                            .dismissOnTouchOutside(false)
                            .asCustom(new CurrencyRechargeDialog(mActivity, priceList, "99", new CurrencyRechargeDialog.GoPayListener() {
                                @Override
                                public void goPay(String money,String id) {
                                    showPayMode(money,id,view);
                                }
                            }))
                            .show();
                }else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    /**
     * 显示支付方式弹窗
     * */
    private void showPayMode(String money, String id, View v) {
        final PaySelect paySelect = new PaySelect(mActivity,money,"红包",money,2);
        new XPopup.Builder(mActivity)
                .atView(v)
                .asCustom(paySelect)
                .show();
        paySelect.setOnClickListenerOnSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtils.isDoubleClick(2000)){
                    //立即支付
                    PaySelect.SelectPayType type = paySelect.getCurrSeletPayType();
                    switch (type) {
                        case ALI:
                            //支付宝支付
                            recharge("2",money,id);
                            break;
                        case WCHAT:
                            //微信支付
                            recharge("1",money,id);
                            break;
                        case WALLET:
                            //钱包支付
                            recharge("4",money,id);
                            break;
                    }
                }
                paySelect.dismiss();
            }
        });
    }

    private void recharge(String payType, String money, String id) {
        showProgress(false);
        UserApi.recharge("8", id, "2", payType, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    PayInfoBean payInfoBean = (PayInfoBean) object;
                    aliPay(payInfoBean.getPayInfo());
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    private void aliPay(String payInfo) {
        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
        MyALipayUtils myALipayUtils = builder.setAppid(ALIPAY_APPID).build();
        myALipayUtils.goAliPay(payInfo, mActivity, new MyALipayUtils.AlipayListener() {
            @Override
            public void onPaySuccess() {
                initData();
            }

            @Override
            public void onPayFailed() {

            }
        });
    }
}
