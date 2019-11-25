package com.yuanqi.hangzhou.imhookup.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.core.AttachPopupView;
import com.yuanqi.hangzhou.imhookup.R;

/**
 * 自定义会话页面加好Popuwindow背景
 */
public class MorePopupView extends AttachPopupView implements View.OnClickListener {

    private Context context;
    private int type;               //type == 1  显示  收藏和拉黑两个按钮    type == 2  显示匿名举报一个按钮

    public MorePopupView(@NonNull Context context,int type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.more_popup;
    }

    @Override
    public void init() {
        super.init();
        initView();
    }

    private void initView() {
        TextView tvCollection = findViewById(R.id.tv_more_popup_Collection);
        TextView tvPullBlack = findViewById(R.id.tv_more_popup_PullBlack);
        TextView tvReport = findViewById(R.id.tv_more_popup_Report);
        LinearLayout llMoreHome = findViewById(R.id.ll_more_home);

        if (type == 1){
            llMoreHome.setVisibility(VISIBLE);
            tvReport.setVisibility(GONE);
        }else if (type == 2){
            llMoreHome.setVisibility(GONE);
            tvReport.setVisibility(VISIBLE);
        }

        tvCollection.setOnClickListener(this);
        tvPullBlack.setOnClickListener(this);
        tvReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more_popup_Collection:
                //收藏
                Toast.makeText(context,"收藏",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_more_popup_PullBlack:
                //拉黑
                Toast.makeText(context,"拉黑",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_more_popup_Report:
                //举报
                Toast.makeText(context,"举报",Toast.LENGTH_LONG).show();
                break;
        }
        dismiss();
    }
}
