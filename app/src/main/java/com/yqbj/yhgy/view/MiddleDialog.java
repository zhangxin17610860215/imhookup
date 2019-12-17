package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.utils.StringUtil;

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
    public interface Listener{
        void onConfirmClickListener();
        void onCloseClickListener();
    }
    private Listener listener;

    public MiddleDialog(@NonNull Context context, String title, String content, Listener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
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
        tvContent.setVisibility(StringUtil.isEmpty(content) ? GONE : VISIBLE);
        tvTitle.setVisibility(StringUtil.isEmpty(title) ? GONE : VISIBLE);
        tvTitle.setText(StringUtil.isEmpty(title) ? "" : title);
        tvContent.setText(StringUtil.isEmpty(content) ? "" : content);

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
                listener.onConfirmClickListener();
                dismiss();
                break;
            case R.id.img_close:
                //关闭
                listener.onCloseClickListener();
                dismiss();
                break;
        }
    }

}
