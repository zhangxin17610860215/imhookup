package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.login.VipCoreActivity;

/**
 * 解锁私聊警示弹窗
 * */
public class ChatCautionDialog extends CenterPopupView implements View.OnClickListener {

    private Context context;
    private TextView tv_title;
    private TextView tv_paySee;
    private TextView tv_OpeningVip;
    private ImageView imgClose;
    private String title,money;
    private PaySeeOnClickListener listener;

    public ChatCautionDialog(@NonNull Context context, String title, String money, PaySeeOnClickListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.money = money;
        this.listener = listener;
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
        tv_title = findViewById(R.id.tv_title);
        tv_paySee = findViewById(R.id.tv_paySee);
        tv_OpeningVip = findViewById(R.id.tv_OpeningVip);
        imgClose = findViewById(R.id.img_close);
        tv_paySee.setOnClickListener(this);
        tv_OpeningVip.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    private void initData() {
        tv_paySee.setText("付费查看("+money+"元)");
        tv_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_paySee:
                //付费查看
                listener.onClick(money);
                dismiss();
                break;
            case R.id.tv_OpeningVip:
                //成为会员,免费查看
                VipCoreActivity.start(context);
                dismiss();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    public interface PaySeeOnClickListener{
        void onClick(String money);
    }

}
