package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yqbj.yhgy.R;

/**
 * 解锁私聊警示弹窗
 */
public class CurrencyWithdrawalDialog extends CenterPopupView implements View.OnClickListener {

    private ImageView imgClose;
    private TextView tvConfirm;
    private TextView tvContent;
    private TextView tvTitle;

    private Context context;
    private String content = "你有%1$s枚约会币可兑换成人民币%2$s元,兑换后剩余%3$s枚约会币。确定申请兑换吗?";
    private String buttonText = "确认";

    /**
     * @param convertible       有convertible枚约会币可兑换
     * @param rMB               convertible枚约会币可兑换成rMB元
     * @param surplus           兑换完后剩余surplus枚约会币
     * @param type
     * */
    public CurrencyWithdrawalDialog(@NonNull Context context, String convertible, String rMB, String surplus, String type) {
        super(context);
        this.context = context;
        if (type.equals("1")){
            this.content = String.format(content, convertible, rMB, surplus);
            this.buttonText = "确认";
        }else {
            this.content = "我们将在2个工作日内向你的支付宝账号转账。";
            this.buttonText = "知道了";
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_currencywithdrawal_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        imgClose = findViewById(R.id.img_close);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvContent = findViewById(R.id.tv_content);
        tvTitle = findViewById(R.id.tv_title);
        tvContent.setText(content);
        tvConfirm.setText(buttonText);

        imgClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                //确认
                if (buttonText.equals("确认")){
                    //确认提现
                    ToastHelper.showToast(context,"确认提现");
                    dismiss();
                    new XPopup.Builder(context)
                            .dismissOnTouchOutside(false)
                            .asCustom(new CurrencyWithdrawalDialog(context,"40","2","19","2"))
                            .show();
                }else {
                    dismiss();
                }
                break;
            case R.id.img_close:
                //关闭
                dismiss();
                break;
        }
    }
}
