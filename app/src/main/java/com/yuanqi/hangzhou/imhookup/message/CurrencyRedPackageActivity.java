package com.yuanqi.hangzhou.imhookup.message;

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
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;
import com.yuanqi.hangzhou.imhookup.utils.NumberUtil;
import com.yuanqi.hangzhou.imhookup.utils.RedPacketTextWatcher;
import com.yuanqi.hangzhou.imhookup.utils.StringUtil;
import com.yuanqi.hangzhou.imhookup.view.CurrencyPayDialog;
import com.yuanqi.hangzhou.imhookup.view.PaySelect;

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
                    showPayDialog();
                    break;
            }
        }

    }

    /**
     * 显示支付弹窗
     * */
    private void showPayDialog() {
        new XPopup.Builder(mActivity)
                .dismissOnTouchOutside(false)
                .asCustom(new CurrencyPayDialog(mActivity,1))
                .show();
    }

}
