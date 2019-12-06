package com.yuanqi.hangzhou.imhookup.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * 底部弹窗(评价)
 * */
public class EvaluateDialog extends BottomPopupView implements View.OnClickListener {

    private Activity mActivity;
    private TextView tv_Close;
    private TextView tv_niming;
    private RecyclerView mRecyclerView;

    private EasyRVAdapter mAdapter;
    private List<String> list;

    public EvaluateDialog(@NonNull Activity activity, List<String> data) {
        super(activity);
        this.mActivity = activity;
        this.list = data;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_evaluate_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tv_Close = findViewById(R.id.tv_Close);
        tv_niming = findViewById(R.id.tv_niming);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        tv_Close.setOnClickListener(this);
        tv_niming.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
    }

    private void initData() {

        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.item_evaluate_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                final TextView tv_num = viewHolder.getView(R.id.tv_num);
                LinearLayout ll_onClike = viewHolder.getView(R.id.ll_onClike);
                TextView tv_text = viewHolder.getView(R.id.tv_text);
                tv_text.setText(list.get(position));

                ll_onClike.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_num.setText("1");
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_Close:
                dismiss();
                break;
            case R.id.tv_niming:
                //匿名评价
                break;
        }
    }
}
