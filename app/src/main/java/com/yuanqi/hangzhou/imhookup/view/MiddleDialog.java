package com.yuanqi.hangzhou.imhookup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yuanqi.hangzhou.imhookup.R;

/**
 * 中间弹窗
 */
public class MiddleDialog extends CenterPopupView implements View.OnClickListener {

    private ImageView imgClose;
    private TextView tvConfirm;
    private TextView tvContent;
    private TextView tvTitle;

    private Context context;
    private String content = "";
    private String title = "";
    private OnClickListener onClickListener;

    public MiddleDialog(@NonNull Context context, String title, String content, OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.onClickListener = onClickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_middle_layout;
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
        tvTitle.setText(title);
        tvContent.setText(content);

        imgClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                //确定
                onClickListener.onClick(view);
                dismiss();
                break;
            case R.id.img_close:
                //关闭
                dismiss();
                break;
        }
    }

}
