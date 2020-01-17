package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.me.GenderSelectionAct;
import com.yqbj.yhgy.utils.StringUtil;

/**
 * 两个按钮中间弹窗
 */
public class MiddleTwoBtDialog extends CenterPopupView implements View.OnClickListener {

    private Context context;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvDetermine;
    private TextView tvCancel;
    private ImageView imgClose;
    private String title;
    private String content;
    private String determine;
    private String cancel;
    private ClickListener listener;

    public MiddleTwoBtDialog(@NonNull Context context, String title, String content, String determine, String cancel, ClickListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.determine = determine;
        this.cancel = cancel;
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_middletwobt_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvDetermine = findViewById(R.id.tv_Determine);
        tvCancel = findViewById(R.id.tv_cancel);
        imgClose = findViewById(R.id.img_close);
        if (StringUtil.isNotEmpty(title)){
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
        if (StringUtil.isNotEmpty(content)){
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(content);
        }
        if (StringUtil.isNotEmpty(determine)){
            tvDetermine.setVisibility(VISIBLE);
            tvDetermine.setText(determine);
        }
        if (StringUtil.isNotEmpty(cancel)){
            tvCancel.setVisibility(VISIBLE);
            tvCancel.setText(cancel);
        }

        tvDetermine.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_Determine:
                listener.determine();
                dismiss();
                break;
            case R.id.tv_cancel:
                listener.cancel();
                dismiss();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    public interface ClickListener {
        void determine();
        void cancel();
    }

}
