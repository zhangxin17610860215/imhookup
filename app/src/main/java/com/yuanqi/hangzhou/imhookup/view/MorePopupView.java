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
 * 自定义Popuwindow背景
 */
public class MorePopupView extends AttachPopupView implements View.OnClickListener {

    private Context context;
    /**
     * type == 1  显示收藏和拉黑两个按钮
     * type == 2  显示匿名举报一个按钮
     * type == 3  显示取消拉黑和举报两个按钮
     * type == 4  显示取消收藏和拉黑两个按钮
     * */
    private int type;

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
        TextView tvAnonymousReport = findViewById(R.id.tv_more_popup_AnonymousReport);
        TextView tvCancelBlackout = findViewById(R.id.tv_cancelBlackout);
        TextView tvReport = findViewById(R.id.tv_Report);
        TextView tvCancelCollection = findViewById(R.id.tv_cancelCollection);
        TextView tvBlackout = findViewById(R.id.tv_Blackout);
        LinearLayout llMoreHomeOne = findViewById(R.id.ll_more_home_one);
        LinearLayout llMoreHomeThree = findViewById(R.id.ll_more_home_three);
        LinearLayout llMoreHomeFore = findViewById(R.id.ll_more_home_fore);

        if (type == 1){
            llMoreHomeOne.setVisibility(VISIBLE);
            tvAnonymousReport.setVisibility(GONE);
            llMoreHomeThree.setVisibility(GONE);
            llMoreHomeFore.setVisibility(GONE);
        }else if (type == 2){
            llMoreHomeOne.setVisibility(GONE);
            tvAnonymousReport.setVisibility(VISIBLE);
            llMoreHomeThree.setVisibility(GONE);
            llMoreHomeFore.setVisibility(GONE);
        }else if (type == 3){
            llMoreHomeOne.setVisibility(GONE);
            tvAnonymousReport.setVisibility(GONE);
            llMoreHomeThree.setVisibility(VISIBLE);
            llMoreHomeFore.setVisibility(GONE);
        }else if (type == 4){
            llMoreHomeOne.setVisibility(GONE);
            tvAnonymousReport.setVisibility(GONE);
            llMoreHomeThree.setVisibility(GONE);
            llMoreHomeFore.setVisibility(VISIBLE);
        }

        tvCollection.setOnClickListener(this);
        tvPullBlack.setOnClickListener(this);
        tvAnonymousReport.setOnClickListener(this);
        tvReport.setOnClickListener(this);
        tvCancelBlackout.setOnClickListener(this);
        tvCancelCollection.setOnClickListener(this);
        tvBlackout.setOnClickListener(this);
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
            case R.id.tv_more_popup_AnonymousReport:
                //匿名举报
                Toast.makeText(context,"匿名举报",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_Report:
                //举报
                Toast.makeText(context,"举报",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_cancelBlackout:
                //取消拉黑
                Toast.makeText(context,"取消拉黑",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_cancelCollection:
                //取消收藏
                Toast.makeText(context,"取消收藏",Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_Blackout:
                //拉黑
                Toast.makeText(context,"拉黑",Toast.LENGTH_LONG).show();
                break;
        }
        dismiss();
    }
}
