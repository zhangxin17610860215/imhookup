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
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.RedPacketTextWatcher;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.view.PaySelect;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public static void start(Activity context, int requestCode) {
        Intent intent = new Intent(context, CashRedPackageActivity.class);
//        context.startActivity(intent);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashredpackage_activity_layout);
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
        if (!NoDoubleClickUtils.isDoubleClick(500)){
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
                    showPayMode("99.99",view);
                    break;
            }
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
                    Intent intent = new Intent();
                    intent.putExtra("redId", "redId");
                    intent.putExtra("redTitle", "redTitle");
                    intent.putExtra("redContent", "小小意思,拿去浪吧");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

    }
}
