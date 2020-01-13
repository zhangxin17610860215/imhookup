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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.MyLikeBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.home.DetailsActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;
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
    @BindView(R.id.tv_noData_content)
    TextView tvNoDataContent;
    @BindView(R.id.ll_nodata)
    LinearLayout llNodata;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<MyLikeBean.RecordsBean> list = new ArrayList<>();
    private int count;              //总数量
    private int pageNum = 1;        //页码
    private int pageSize = 20;      //每页需要展示的数量

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
                pageNum = 1;
                initData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多
                if (count / pageNum > pageSize && count / pageNum > 0) {
                    pageNum++;
                    initData();
                } else {
                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }
            }
        });
    }

    private void initData() {
        showProgress(false);
        UserApi.getBlackList(pageNum, pageSize, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
                if (code == Constants.SUCCESS_CODE) {
                    MyLikeBean bean = (MyLikeBean) object;
                    List<MyLikeBean.RecordsBean> records = bean.getRecords();
                    count = bean.getTotal();
                    list.clear();
                    list.addAll(records);
                    loadData();
                } else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }

    private void loadData() {
        if (null == list || list.size() <= 0){
            llNodata.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            tvNoDataContent.setText("没有数据");
        }else {
            llNodata.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter = new EasyRVAdapter(mActivity, list, R.layout.blacklist_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                MyLikeBean.RecordsBean recordsBean = list.get(position);
                if (null == list || list.size() == 0) {
                    return;
                }
                RoundedImageView imgHeader = viewHolder.getView(R.id.img_head);
                TextView tvName = viewHolder.getView(R.id.tv_name);
                TextView tvBlacklist = viewHolder.getView(R.id.tv_Blacklist);
                Glide.with(mActivity).load(recordsBean.getHeadUrl()).placeholder(R.mipmap.default_home_head).error(R.mipmap.default_home_head).into(imgHeader);
                tvName.setText(recordsBean.getName());
                recordsBean.setBlacklistFlag(1);

                tvBlacklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int addBlacklist = recordsBean.getBlacklistFlag() == 1 ? 2 : 1;
                        operatorBlackList(recordsBean, addBlacklist, tvBlacklist);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                MyLikeBean.RecordsBean recordsBean = list.get(position);
                String gender = StringUtil.isEmpty(Preferences.getGender()) ? "1" : Preferences.getGender();
                if (gender.equals(recordsBean.getGender() + "")) {
                    if (gender.equals("1")) {
                        toast("男士无法查看其他男士详情");
                    } else {
                        toast("女士无法查看其他女士详情");
                    }
                    return;
                }
                DetailsActivity.start(mActivity, recordsBean.getAccid());
            }
        });
    }

    /**
     * 操作黑名单
     *
     * @param recordsBean
     * @param addBlacklist
     * @param tvBlacklist
     */
    private void operatorBlackList(MyLikeBean.RecordsBean recordsBean, int addBlacklist, TextView tvBlacklist) {
        showProgress(false);
        UserApi.operatorBlackList(recordsBean.getAccid(), addBlacklist, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE) {
                    tvBlacklist.setText(addBlacklist == 1 ? "取消拉黑" : "拉黑");
                    recordsBean.setBlacklistFlag(addBlacklist);
                } else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

}
