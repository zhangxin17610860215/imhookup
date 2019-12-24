package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
 * 黑名单
 */
public class BlacklistActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    MyRefreshLayout refreshLayout;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, BlacklistActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity_layout);
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

        loadData();
    }

    private void loadData() {
        mAdapter = new EasyRVAdapter(mActivity, list, R.layout.blacklist_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                ImageView imgHeader = viewHolder.getView(R.id.img_header);
                ImageView imgMore = viewHolder.getView(R.id.img_More);
                TextView tvName = viewHolder.getView(R.id.tv_name);

                imgMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new XPopup.Builder(mActivity)
                                .atView(v)
                                .asCustom(new MorePopupView(mActivity,3))
                                .show();
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

}
