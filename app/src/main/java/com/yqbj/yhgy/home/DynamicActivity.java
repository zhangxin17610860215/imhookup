package com.yqbj.yhgy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.view.MorePopupView;
import com.yqbj.yhgy.view.MyRefreshLayout;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人动态
 */
public class DynamicActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    MyRefreshLayout refreshLayout;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private EasyRVAdapter mGridAdapter;
    private List<String> gridlist = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, DynamicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多
            }
        });
    }

    private void initData() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");

        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.dynamic_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                ImageView imgHeader = viewHolder.getView(R.id.img_header);
                ImageView imgGenderType = viewHolder.getView(R.id.img_GenderType);
                ImageView imgMore = viewHolder.getView(R.id.img_more);
                TextView tvName = viewHolder.getView(R.id.tv_name);
                TextView tvContext = viewHolder.getView(R.id.tv_context);
                TextView tvFabulous = viewHolder.getView(R.id.tv_Fabulous);
                TextView tvComment = viewHolder.getView(R.id.tv_comment);
                TextView tvTime = viewHolder.getView(R.id.tv_time);
                RecyclerView mGridRecyclerView = viewHolder.getView(R.id.mRecyclerView);
                mGridRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
                gridlist.clear();
                for (int i = 0; i < position; i++){
                    gridlist.add("");
                }
                mGridAdapter = new EasyRVAdapter(mActivity,gridlist,R.layout.dynamic_grid_item_layout) {
                    @Override
                    protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                        ImageView imageView = viewHolder.getView(R.id.img_dynamic);
                    }
                };
                mGridRecyclerView.setAdapter(mGridAdapter);
                if (position == 2){
                    imgGenderType.setImageResource(R.mipmap.gender_nan_logo);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(null,null, null, null);
                }else {
                    imgGenderType.setImageResource(R.mipmap.gender_nv_logo);
                    tvName.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.mipmap.nvshen_logo), null);
                }

                imgMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //更多
                        new XPopup.Builder(mActivity)
                                .atView(v)
                                .asCustom(new MorePopupView(mActivity,2))
                                .show();
                    }
                });
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }
}
