package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.yqbj.yhgy.R;

/**
 * 解锁私聊警示弹窗
 * */
public class ChatCautionDialog extends CenterPopupView implements View.OnClickListener {

    private Context context;
    private TextView tv_paySee;
    private TextView tv_OpeningVip;
    private ImageView imgClose;

    public ChatCautionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_chat_caution_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tv_paySee = findViewById(R.id.tv_paySee);
        tv_OpeningVip = findViewById(R.id.tv_OpeningVip);
        imgClose = findViewById(R.id.img_close);
        tv_paySee.setOnClickListener(this);
        tv_OpeningVip.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_paySee:
                //付费查看
                dismiss();
                break;
            case R.id.tv_OpeningVip:
                //成为会员,免费查看
                dismiss();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}
