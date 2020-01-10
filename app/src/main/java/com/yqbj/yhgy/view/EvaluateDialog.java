package com.yqbj.yhgy.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.bean.EvaluateDataBean;
import com.yqbj.yhgy.utils.DemoCache;
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
    private List<EvaluateDataBean> dataList;
    private EvaluateListener listener;
    private String[] ids = new String[6];
    private String accid = "";
    private String gender = "";

    public EvaluateDialog(String accid, String gender, @NonNull Activity activity, List<EvaluateDataBean> dataList, EvaluateListener listener) {
        super(activity);
        this.mActivity = activity;
        this.dataList = dataList;
        this.listener = listener;
        this.accid = accid;
        this.gender = gender;
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

        tv_Close.setText(accid.equals(DemoCache.getAccount())?"你的真实评价":gender.equals("1") ? "他的真实评价" : "她的真实评价");
        tv_niming.setVisibility(accid.equals(DemoCache.getAccount())?GONE:VISIBLE);

        tv_Close.setOnClickListener(this);
        tv_niming.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
    }

    private void initData() {

        mAdapter = new EasyRVAdapter(mActivity,dataList,R.layout.item_evaluate_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                EvaluateDataBean dataBean = dataList.get(position);
                final TextView tv_num = viewHolder.getView(R.id.tv_num);
                TextView tv_text = viewHolder.getView(R.id.tv_text);
                tv_text.setText(dataBean.getLabel());
                tv_num.setText(dataBean.getTotalValue() + "");
            }
        };
        mAdapter.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                if (accid.equals(DemoCache.getAccount())){
                    ToastHelper.showToast(mActivity,"不能评价自己");
                    return;
                }
                EvaluateDataBean dataBean = (EvaluateDataBean) item;
                TextView tv_num = view.findViewById(R.id.tv_num);
                if (dataBean.isOnClick()){
                    dataBean.setOnClick(false);
                    dataBean.setTotalValue(dataBean.getTotalValue()-1);
                    ids[position] = "";
                }else {
                    dataBean.setOnClick(true);
                    dataBean.setTotalValue(dataBean.getTotalValue()+1);
                    ids[position] = dataBean.getId();
                }
                tv_num.setText(dataBean.getTotalValue()+"");
            }
        });
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
                listener.evaluateOnClick(ids);
                dismiss();
                break;
        }
    }

    public interface EvaluateListener{
        void evaluateOnClick(String[] ids);
    }
}
