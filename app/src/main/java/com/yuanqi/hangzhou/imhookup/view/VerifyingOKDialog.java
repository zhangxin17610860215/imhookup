package com.yuanqi.hangzhou.imhookup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.user.GenderSelectionAct;

/**
 * 邀请码验证通过
 */
public class VerifyingOKDialog extends CenterPopupView implements View.OnClickListener {

    private Context context;
    private TextView tvDetermine;
    private ImageView imgClose;

    public VerifyingOKDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_verifyingisok_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tvDetermine = findViewById(R.id.tv_Determine);
        imgClose = findViewById(R.id.img_close);
        tvDetermine.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_Determine:
                GenderSelectionAct.start(context);
                dismiss();
                break;
            case R.id.img_close:
                GenderSelectionAct.start(context);
                dismiss();
                break;
        }
    }
}
