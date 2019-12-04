package com.yuanqi.hangzhou.imhookup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.utils.StringUtil;

/**
 * 绑定支付宝弹窗
 */
public class BindAliPayDialog extends CenterPopupView implements View.OnClickListener {

    private EditText etAccount;
    private EditText etName;
    private ImageView imgClose;
    private TextView tvConfirm;

    private Context context;
    private ConfirmOnClickListener confirmListener;
    private String alpayAccount = "";
    private String alpayName = "";

    public BindAliPayDialog(@NonNull Context context, String alpayAccount, String alpayName, ConfirmOnClickListener confirmListener) {
        super(context);
        this.context = context;
        this.alpayAccount = alpayAccount;
        this.alpayName = alpayName;
        this.confirmListener = confirmListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bindalipay_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_Account);
        etName = findViewById(R.id.et_name);
        imgClose = findViewById(R.id.img_close);
        tvConfirm = findViewById(R.id.tv_confirm);

        if (StringUtil.isNotEmpty(alpayAccount)){
            etAccount.setText(alpayAccount);
        }
        if (StringUtil.isNotEmpty(alpayName)){
            etName.setText(alpayName);
        }

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
                if (StringUtil.isEmpty(etAccount.getText().toString()) || StringUtil.isEmpty(etName.getText().toString())){
                    ToastHelper.showToast(context,"请输入支付宝信息");
                    return;
                }
                confirmListener.onClick(view, etAccount.getText().toString(), etName.getText().toString());
                dismiss();
                break;
            case R.id.img_close:
                //关闭
                dismiss();
                break;
        }
    }

    public interface ConfirmOnClickListener{
        void onClick(View v, String account, String Name);
    }
}
