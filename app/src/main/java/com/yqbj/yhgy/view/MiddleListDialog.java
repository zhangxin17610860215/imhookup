package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxj.xpopup.core.CenterPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.utils.StringUtil;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 中间弹出   列表Dialog
 * */
public class MiddleListDialog extends CenterPopupView implements View.OnClickListener {

    private Context context;
    private TextView tvTitle;
    private TextView tvDetermine;
    private TextView tvCancel;
    private RecyclerView mRecyclerView;
    private RelativeLayout rlTag;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private OnClickListener listener;
    private List<String> stringList = new ArrayList<>();

    /**
     * type == 1 期望对象
     * type == 2 身高
     * type == 3 体重
     * */
    private int type;

    public MiddleListDialog(@NonNull Context context, int type, OnClickListener listener) {
        super(context);
        this.context = context;
        this.type = type;
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_middlelist_layout;
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
        mRecyclerView = findViewById(R.id.mRecyclerView);
        rlTag = findViewById(R.id.rl_tag);
        tvDetermine = findViewById(R.id.tv_Determine);
        tvCancel = findViewById(R.id.tv_cancel);
        tvDetermine.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initData() {
        list.clear();
        switch (type){
            case 1:
                tvTitle.setText("期望对象");
                rlTag.setVisibility(VISIBLE);
                for (String expect : Constants.EXPECTLIST){
                    list.add(expect);
                }
                break;
            case 2:
                tvTitle.setText("身高");
                rlTag.setVisibility(GONE);
                for (int i = 139; i <= 240; i ++){
                    list.add(i == 139 ? "不显示" : String.format("%dCM", i));
                }
                break;
            case 3:
                tvTitle.setText("体重");
                rlTag.setVisibility(GONE);
                for (int i = 29; i <= 120; i ++){
                    list.add(i == 29 ? "不显示" : String.format("%dKG", i));
                }
                break;
        }
        loadData();
    }

    private void loadData() {
        mAdapter = new EasyRVAdapter(context,list,R.layout.item_middlelist_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                TextView textView = viewHolder.getView(R.id.tv_text);
                textView.setText(list.get(position));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (type){
                            case 1:
                                String tag = (String) textView.getTag(R.string.app_text_settag);
                                if (StringUtil.isEmpty(tag) || tag.equals("black")){
                                    textView.setTextColor(getResources().getColor(R.color.text_theme_color));
                                    textView.setTag(R.string.app_text_settag,"theme");
                                    stringList.add(list.get(position));
                                }else {
                                    textView.setTextColor(getResources().getColor(R.color.black));
                                    textView.setTag(R.string.app_text_settag,"black");
                                    stringList.remove(list.get(position));
                                }
                                break;
                            case 2:
                            case 3:
                                listener.onClick(list.get(position));
                                dismiss();
                                break;
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_Determine:
                //确定
                if (stringList.size() > 4){
                    ToastHelper.showToast(context,"最多只能选择4个期望对象");
                    return;
                }
                if (stringList.size() <= 0){
                    ToastHelper.showToast(context,"请选择期望对象");
                    return;
                }
                String content = "";
                for (int i = 0; i < stringList.size(); i++){
                    content = content + stringList.get(i) + "/";
                }
                if (content.contains("/")){
                    content = content.substring(0,content.length()-1);
                }
                listener.onClick(content);
                dismiss();
                break;
            case R.id.tv_cancel:
                //取消
                dismiss();
                break;
        }
    }

    public interface OnClickListener{
        void onClick(String text);
    }
}
