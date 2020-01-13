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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.common.util.CityBean;
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
import com.yqbj.yhgy.utils.TimeUtils;
import com.yqbj.yhgy.utils.ZodiacUtil;
import com.yqbj.yhgy.view.MorePopupView;
import com.yqbj.yhgy.view.MyRefreshLayout;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yqbj.yhgy.config.Constants.OCCUPATIONBEANLIST;

/**
 * 我喜欢的
 * */
public class MyLikeActivity extends BaseActivity {

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
        Intent intent = new Intent(context, MyLikeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylike_activity_layout);
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
                if (count / pageNum > pageSize && count / pageNum > 0){
                    pageNum++;
                    initData();
                }else {
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
        UserApi.getEnjoyList(pageNum, pageSize, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
                }
                if (code == Constants.SUCCESS_CODE){
                    MyLikeBean bean = (MyLikeBean) object;
                    List<MyLikeBean.RecordsBean> records = bean.getRecords();
                    count = bean.getTotal();
                    list.clear();
                    list.addAll(records);
                    loadData();
                }else {
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
        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.item_home_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                MyLikeBean.RecordsBean recordsBean = list.get(position);
                RoundedImageView imgHeader = viewHolder.getView(R.id.img_home_header);//头像
                TextView tvPhoto = viewHolder.getView(R.id.tv_home_photo);//照片数量
                TextView tvName = viewHolder.getView(R.id.tv_home_name);//姓名
                ImageView imgNvShen = viewHolder.getView(R.id.img_home_nvshen);//女神
                ImageView imgZhenRen = viewHolder.getView(R.id.img_home_zhenren);//真人
                ImageView imgVip = viewHolder.getView(R.id.img_home_vip);//VIP
                ImageView imgIsLike = viewHolder.getView(R.id.img_isLike);//是否是喜欢
                TextView tvDistance = viewHolder.getView(R.id.tv_home_distance);//距离
                TextView tvPaidAlbum = viewHolder.getView(R.id.tv_home_PaidAlbum);//付费相册
                TextView tvOLine = viewHolder.getView(R.id.tv_home_online);//在线时长
                TextView tvPlace = viewHolder.getView(R.id.tv_home_place);//地点
                TextView tvAge = viewHolder.getView(R.id.tv_home_age);//星座-年龄
                TextView tvOccupation = viewHolder.getView(R.id.tv_home_Occupation);//职业

                tvPhoto.setText(recordsBean.getMultimediaSize() + "");
                if (recordsBean.getGender() == 1) {
                    //男士
                    imgVip.setVisibility(recordsBean.getVipMember() == 1 ? View.VISIBLE : View.GONE);
                    imgNvShen.setVisibility(View.GONE);
                    imgZhenRen.setVisibility(View.GONE);
                } else {
                    //女士
                    imgVip.setVisibility(View.GONE);
                    imgNvShen.setVisibility(recordsBean.getLabeltype() == 1 ? View.VISIBLE : View.GONE);
                    imgZhenRen.setVisibility(recordsBean.getLabeltype() == 0 ? recordsBean.getCertification() == 1 ? View.VISIBLE : View.GONE : View.GONE);
                }
                tvPlace.setText(recordsBean.getRegion());
                tvPaidAlbum.setVisibility(recordsBean.getConfig().getPrivacystate() == 2 ? View.VISIBLE : View.GONE);
                tvOLine.setVisibility(recordsBean.getConfig().getHideonline() == 1 ? View.GONE : View.VISIBLE);
                Glide.with(mActivity).load(recordsBean.getHeadUrl()).placeholder(R.mipmap.default_home_head).error(R.mipmap.default_home_head).into(imgHeader);
                tvName.setText(recordsBean.getName());

                tvDistance.setText(recordsBean.getDistance() + "m");
                tvDistance.setVisibility(recordsBean.getConfig().getHidelocation() == 1 ? View.GONE : View.VISIBLE);
                tvAge.setText(ZodiacUtil.date2Constellation(recordsBean.getBirthday()) + "-" + TimeUtils.getAgeFromBirthTime(recordsBean.getBirthday()) + "岁");
                CityBean occupationBean;
                String job = "";
                for (int i = 0; i < OCCUPATIONBEANLIST.size(); i++) {
                    occupationBean = OCCUPATIONBEANLIST.get(i);
                    for (int j = 0; j < occupationBean.getChild().size(); j++) {
                        List<CityBean.ChildBeanX> occupation = OCCUPATIONBEANLIST.get(i).getChild();
                        if (recordsBean.getJob().equals(occupation.get(j).getValue())) {
                            job = occupation.get(j).getText();
                        }
                    }
                }

                tvOccupation.setText(job);
                recordsBean.setEnjoyFlag(1);
                imgIsLike.setImageResource(R.mipmap.yes_like_logo);
                imgIsLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //是否加入喜欢
                        String gender = StringUtil.isEmpty(Preferences.getGender()) ? "1" : Preferences.getGender();
                        if (gender.equals(recordsBean.getGender() + "")){
                            if (gender.equals("1")){
                                toast("男士无法收藏其他男士");
                            }else {
                                toast("女士无法收藏其他女士");
                            }
                            return;
                        }
                        int addLike = recordsBean.getEnjoyFlag() == 1? 2:1;
                        operatorEnjoy(recordsBean, recordsBean.getAccid(), addLike,imgIsLike);
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
                if (gender.equals(recordsBean.getGender() + "")){
                    if (gender.equals("1")){
                        toast("男士无法查看其他男士详情");
                    }else {
                        toast("女士无法查看其他女士详情");
                    }
                    return;
                }
                DetailsActivity.start(mActivity,recordsBean.getAccid());
            }
        });
    }

    /**
     * 添加移除收藏
     * */
    private void operatorEnjoy(MyLikeBean.RecordsBean recordsBean, String accid, int addLike, ImageView imgIsLike) {
        showProgress(false);
        UserApi.operatorEnjoy(accid, addLike, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    imgIsLike.setImageResource(addLike==1?R.mipmap.yes_like_logo:R.mipmap.no_like_logo);
                    recordsBean.setEnjoyFlag(addLike);
                }else {
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
