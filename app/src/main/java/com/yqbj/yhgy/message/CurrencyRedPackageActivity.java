package com.yqbj.yhgy.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.CurrencyPriceBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.RedPacketTextWatcher;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.view.CurrencyPayDialog;
import com.yqbj.yhgy.view.CurrencyRechargeDialog;
import com.yqbj.yhgy.view.PaySelect;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 虚拟币红包
 */
public class CurrencyRedPackageActivity extends BaseActivity {

    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.tv_money)
    TextView tvMoney;

    private Activity mActivity;
    private String money = "";

    public static void start(Activity context, int requestCode) {
        Intent intent = new Intent(context, CurrencyRedPackageActivity.class);
//        context.startActivity(intent);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyredpackage_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        etMoney.addTextChangedListener(new RedPacketTextWatcher(etMoney));
        etMoney.addTextChangedListener(new TextWatcher() {
            private int editStart ;
            private int editEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                money = etMoney.getText().toString();
                editStart = etMoney.getSelectionStart();
                editEnd = etMoney.getSelectionEnd();
                if (StringUtil.isNotEmpty(money) && NumberUtil.compareLess("4000",money)){
                    toast("单次红包约会币不得大于4000");
                    s.delete(editStart-1, editEnd);
                    int tempSelection = editStart;
                    etMoney.setText(s);
                    etMoney.setSelection(tempSelection);
                }
                if (StringUtil.isEmpty(money)){
                    money = "0";
                }
                tvMoney.setText(money);
            }
        });
    }

    private void initData() {

    }

    @OnClick({R.id.tv_back, R.id.tv_sendRedPackage})
    public void onViewClicked(View view) {
        if (!NoDoubleClickUtils.isDoubleClick(500)){
            switch (view.getId()) {
                case R.id.tv_back:
                    //取消
                    finish();
                    break;
                case R.id.tv_sendRedPackage:
                    //赛钱进红包
                    if (StringUtil.isEmpty(money)){
                        toast("请输入约会币红包数量");
                        return;
                    }
                    if (NumberUtil.compareLess(money,"1")){
                        toast("单个红包约会币不得少于1");
                        return;
                    }
                    if (NumberUtil.compareLess("4000",money)){
                        toast("单次红包约会币不得大于4000");
                        return;
                    }
                    showPayDialog(view);
                    break;
            }
        }

    }

    /**
     * 显示支付弹窗
     * */
    private void showPayDialog(View view) {
        new XPopup.Builder(mActivity)
                .dismissOnTouchOutside(false)
                .asCustom(new CurrencyPayDialog(mActivity, 1, new CurrencyPayDialog.CurrencyPayListener() {
                    @Override
                    public void recharge(String currencyNum) {
                        //充值
                        showRechargeDialog(view);
                    }

                    @Override
                    public void pay() {
                        //支付
                        Intent intent = new Intent();
                        intent.putExtra("redId", "redId");
                        intent.putExtra("redTitle", "redTitle");
                        intent.putExtra("redContent", "小小意思,拿去浪吧");
                        mActivity.setResult(Activity.RESULT_OK, intent);
                        mActivity.finish();
                    }
                }))
                .show();
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
                                public void goPay(String money) {
                                    showPayMode(money,view);
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
