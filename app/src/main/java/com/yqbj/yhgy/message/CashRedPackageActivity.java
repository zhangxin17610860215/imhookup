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
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.PayInfoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.RedPacketTextWatcher;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.utils.pay.MyALipayUtils;
import com.yqbj.yhgy.view.PaySelect;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yqbj.yhgy.MyApplication.ALIPAY_APPID;

/**
 * 现金红包
 */
public class CashRedPackageActivity extends BaseActivity {

    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.tv_money)
    TextView tvMoney;

    private Activity mActivity;
    private String money = "";
    private String targetId = "";
    private String redName = "";

    public static void start(Activity context, int requestCode, String targetId) {
        Intent intent = new Intent(context, CashRedPackageActivity.class);
        intent.putExtra("targetId",targetId);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashredpackage_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        targetId = getIntent().getStringExtra("targetId");
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
                if (StringUtil.isNotEmpty(money) && NumberUtil.compareLess("200",money)){
                    toast("单次红包金额不得大于200");
                    s.delete(editStart-1, editEnd);
                    int tempSelection = editStart;
                    etMoney.setText(s);
                    etMoney.setSelection(tempSelection);
                }
                if (StringUtil.isEmpty(money)){
                    money = "0.00";
                }
                tvMoney.setText(money);
            }
        });
    }

    private void initData() {

    }

    @OnClick({R.id.tv_back, R.id.tv_sendRedPackage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                //取消
                finish();
                break;
            case R.id.tv_sendRedPackage:
                //赛钱进红包
                if (StringUtil.isEmpty(money)){
                    toast("请输入红包金额");
                    return;
                }
                if (NumberUtil.compareLess(money,"0.01")){
                    toast("单个红包金额不得少于0.01");
                    return;
                }
                if (NumberUtil.compareLess("200",money)){
                    toast("单次红包金额不得大于200");
                    return;
                }
                showPayMode(money,view);
                break;
        }
    }

    /**
     * 显示支付方式Dialog
     * amount  当前余额
     * */
    private void showPayMode(final String amount, View v) {
        final PaySelect paySelect = new PaySelect(mActivity,money,"红包",amount,2);
        new XPopup.Builder(mActivity)
                .atView(v)
                .asCustom(paySelect)
                .show();
        paySelect.setOnClickListenerOnSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即支付
                if (!NoDoubleClickUtils.isDoubleClick(2000)){
                    PaySelect.SelectPayType type = paySelect.getCurrSeletPayType();
                    switch (type) {
                        case ALI:
                            //支付宝支付
                            sendPacket("2",amount);
                            break;
                        case WCHAT:
                            //微信支付
                            sendPacket("1",amount);
                            break;
                        case WALLET:
                            //钱包支付
                            sendPacket("4",amount);
                            break;
                    }
                    paySelect.dismiss();
                }
            }
        });

    }

    private void sendPacket(String payType, String amount) {
        if (StringUtil.isEmpty(etDescribe.getText().toString())){
            redName = "小小意思,拿去浪吧";
        }else {
            redName = etDescribe.getText().toString();
        }
        showProgress(false);
        UserApi.sendPacket(redName, amount, "2", payType, targetId, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    if (payType.equals("2")){
                        PayInfoBean payInfoBean = (PayInfoBean) object;
                        MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                        MyALipayUtils myALipayUtils = builder.setAppid(ALIPAY_APPID).build();
                        myALipayUtils.goAliPay(payInfoBean.getPayInfo(), mActivity, new MyALipayUtils.AlipayListener() {
                            @Override
                            public void onPaySuccess() {
//                                sendPacketFinish();
                                finish();
                            }

                            @Override
                            public void onPayFailed() {

                            }
                        });
                    }

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

    private void sendPacketFinish() {
        Intent intent = new Intent();
        intent.putExtra("redId", "redId");
        intent.putExtra("redTitle", "普通红包");
        intent.putExtra("redContent", redName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
