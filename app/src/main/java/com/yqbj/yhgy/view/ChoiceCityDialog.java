package com.yqbj.yhgy.view;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lxj.xpopup.core.BottomPopupView;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.util.CityBean;
import com.yqbj.yhgy.R;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹窗(选择城市/职业)
 * */
public class ChoiceCityDialog extends BottomPopupView implements View.OnClickListener {

    private Activity mActivity;
    private ImageView img_back;
    private TextView tv_title;
    private TextView tv_Determine;
    private DetermineOnClickListener listener;

    /**
     * type==1    选择城市
     * type==2    选择职业
     * */
    private int type;
    /**
     * 可以选几个
     * */
    private int optionNumber;

    private RecyclerView rv_ClassA;
    private RecyclerView rv_ClassB;
    private EasyRVAdapter mAdapterA;
    private EasyRVAdapter mAdapterB;
    private ArrayList<CityBean> cityBeanList;
    private List<CityBean.ChildBeanX> childList = new ArrayList<>();
    private int posA = -1;

    /**
     * 存放选择后的选项文字
     * */
    private List<String> selectedTextList;

    /**
     * 存放选择后的选项编号
     * */
    private List<String> selectedValueList;

    public ChoiceCityDialog(@NonNull Activity activity, ArrayList<CityBean> cityBeanList, List<String> selectedValueList, List<String> selectedTextList, int type, int optionNumber, DetermineOnClickListener listener) {
        super(activity);
        this.mActivity = activity;
        this.type = type;
        this.optionNumber = optionNumber;
        this.listener = listener;
        this.cityBeanList = cityBeanList;
        this.selectedValueList = null == selectedValueList ? new ArrayList<>() : selectedValueList;
        this.selectedTextList = null == selectedTextList ? new ArrayList<>() : selectedTextList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choicecity_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        tv_Determine = findViewById(R.id.tv_Determine);
        rv_ClassA = findViewById(R.id.rv_ClassA);
        rv_ClassB = findViewById(R.id.rv_ClassB);
        rv_ClassA.setLayoutManager(new LinearLayoutManager(mActivity));
        rv_ClassB.setLayoutManager(new LinearLayoutManager(mActivity));
        img_back.setOnClickListener(this);
        tv_Determine.setOnClickListener(this);

        if (type == 1){
            tv_title.setText("常驻城市");
        }else if (type == 2){
            tv_title.setText("选择职业");
        }
    }

    private void initData() {
        //一级Adapter
        mAdapterA = new EasyRVAdapter(mActivity,cityBeanList,R.layout.choicecity_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                TextView tv_text = viewHolder.getView(R.id.tv_text);
                tv_text.setText(cityBeanList.get(position).getText());
                if (posA == position){
                    tv_text.setBackgroundResource(R.color.text_theme_color);
                    tv_text.setTextColor(Color.WHITE);
                }else {
                    tv_text.setBackgroundResource(R.color.white);
                    CityBean cityBean = cityBeanList.get(position);
                    for (int j = 0; j < cityBean.getChild().size(); j ++){
                        CityBean.ChildBeanX childBeanX = cityBean.getChild().get(j);
                        if (selectedValueList.contains(childBeanX.getValue())){
                            tv_text.setTextColor(getResources().getColor(R.color.text_theme_color));
                            break;
                        }else {
                            tv_text.setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                }
            }
        };
        rv_ClassA.setAdapter(mAdapterA);
        mAdapterA.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                mAdapterA.notifyDataSetChanged();
                if (null != childList || childList.size() > 0){
                    childList.clear();
                }
                childList.addAll(cityBeanList.get(position).getChild());
                loadClassBAdapter();
                posA = position;
            }
        });
    }

    private void loadClassBAdapter() {
        //二级Adapter
        if (null != mAdapterB){
            mAdapterB.notifyDataSetChanged();
            return;
        }
        mAdapterB = new EasyRVAdapter(mActivity,childList,R.layout.choicecity_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                TextView tv_text = viewHolder.getView(R.id.tv_text);
                tv_text.setText(childList.get(position).getText());
                tv_text.setTextColor(getResources().getColor(R.color.black));
                if (selectedValueList.size() > 0){
                    for (String child : selectedValueList){
                        if (childList.get(position).getValue().equals(child)){
                            tv_text.setTextColor(getResources().getColor(R.color.text_theme_color));
                        }
                    }
                }else {
                    tv_text.setTextColor(Color.BLACK);
                }
                tv_text.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否已经选中
                        if (!selectedValueList.contains(childList.get(position).getValue())){
                            //判断是否超过选择个数
                            if (selectedTextList.size() >= optionNumber && selectedValueList.size() >= optionNumber){
                                ToastHelper.showToast(mActivity,"最多可以选择"+optionNumber+"个");
                                return;
                            }
                            tv_text.setTextColor(getResources().getColor(R.color.text_theme_color));
                            selectedTextList.add(childList.get(position).getText());
                            selectedValueList.add(childList.get(position).getValue());
                        }else {
                            tv_text.setTextColor(getResources().getColor(R.color.black));
                            selectedTextList.remove(childList.get(position).getText());
                            selectedValueList.remove(childList.get(position).getValue());
                        }
                    }
                });
            }
        };
        rv_ClassB.setAdapter(mAdapterB);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                dismiss();
                break;
            case R.id.tv_Determine:
                //确定
                listener.determineOnClickListener(selectedTextList,selectedValueList);
                dismiss();
                break;
        }
    }

    public interface DetermineOnClickListener{
        void determineOnClickListener(List<String> selectedTextList, List<String> selectedValueList);
    }
}
