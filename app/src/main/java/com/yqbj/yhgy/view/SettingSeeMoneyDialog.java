package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.StringUtil;

/**
 * Author: 张鑫
 * Date: 2019/12/17 11:35
 * Description: 设置查看金额
 */
public class SettingSeeMoneyDialog extends CenterPopupView implements View.OnClickListener {

    public interface OnSuccessClickListener{
        void onClick(String str);
    }
    private Context context;
    private TextView tvConfirm;
    private ImageView imgClose;
    private EditText etMoney;
    private OnSuccessClickListener listener;

    public SettingSeeMoneyDialog(@NonNull Context context, OnSuccessClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_settingseemoney_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        etMoney = findViewById(R.id.et_money);
        tvConfirm = findViewById(R.id.tv_confirm);
        imgClose = findViewById(R.id.img_close);
        tvConfirm.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                String money = etMoney.getText().toString().trim();
                if (StringUtil.isEmpty(money)){
                    ToastHelper.showToast(context,"请输入金额");
                    return;
                }
                if (!NumberUtil.compareGreater(money,"0")){
                    ToastHelper.showToast(context,"金额必须大于0");
                    return;
                }
                listener.onClick(money);
                dismiss();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}