package com.yqbj.yhgy.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseFragment;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.view.MorePopupView;
import com.yqbj.yhgy.view.MyRefreshLayout;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页   公园
 */
public class HomeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_home_city)
    TextView tvHomeCity;                    //城市
    @BindView(R.id.img_selecte_Gender)
    ImageView imgSelecteGender;             //选择性别
    @BindView(R.id.tv_nearby)
    TextView tvNearby;                      //附近
    @BindView(R.id.tv_newRegister)
    TextView tvNewRegister;                 //新注册
    @BindView(R.id.tv_goddess)
    TextView tvGoddess;                     //女神
    @BindView(R.id.tv_onLine)
    TextView tvOnLine;                      //在线
    @BindView(R.id.home_mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    MyRefreshLayout refreshLayout;

    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    private Activity mActivity;

    private int gender = 1;//性别    1=男   2=女
    private int type = 1;//类型    1=附近   2=新注册    3=女神
    private boolean isOnLine = true;        //是否在线
    private int pageNum = 1;
    private String region = "510100";//区域（0：附近；区域id：对应具体id值）     默认成都510100

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: get params
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
                pageNum = 1;
                initData();
                refreshLayout.finishRefresh(3000); // 模拟请求数据, 3秒后结束
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //上拉加载更多
                pageNum ++;
                initData();
                refreshLayout.finishLoadMore(3000);
            }
        });
    }

    private void initData() {
        showProgress(false);
        UserApi.index(gender+"", region, isOnLine?"1":"0",
                type+"", pageNum+"", "20", mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.item_home_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                ImageView imgHeader = viewHolder.getView(R.id.img_home_header);//头像
                TextView tvPhoto = viewHolder.getView(R.id.tv_home_photo);//照片数量
                TextView tvName = viewHolder.getView(R.id.tv_home_name);//姓名
                ImageView imgNvShen = viewHolder.getView(R.id.img_home_nvshen);//女神
                ImageView imgZhenRen = viewHolder.getView(R.id.img_home_zhenren);//真人
                ImageView imgVip = viewHolder.getView(R.id.img_home_vip);//VIP
                ImageView imgMore = viewHolder.getView(R.id.img_home_more);//更多
                TextView tvDistance = viewHolder.getView(R.id.tv_home_distance);//距离
                TextView tvPaidAlbum = viewHolder.getView(R.id.tv_home_PaidAlbum);//付费相册
                TextView tvOLine = viewHolder.getView(R.id.tv_home_online);//在线时长
                TextView tvPlace = viewHolder.getView(R.id.tv_home_place);//地点
                TextView tvAge = viewHolder.getView(R.id.tv_home_age);//星座-年龄
                TextView tvOccupation = viewHolder.getView(R.id.tv_home_Occupation);//职业

                imgMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //更多
                        new XPopup.Builder(mActivity)
                                .atView(v)
                                .asCustom(new MorePopupView(mActivity,1))
                                .show();
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
                DetailsActivity.start(mActivity);
            }
        });
    }

    @OnClick({R.id.tv_home_city, R.id.img_selecte_Gender, R.id.el_Search, R.id.tv_nearby, R.id.tv_newRegister, R.id.tv_goddess, R.id.tv_onLine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_home_city:
                //选择城市
                break;
            case R.id.img_selecte_Gender:
                //切换性别
                if (gender == 1){
                    gender = 2;
                    imgSelecteGender.setImageResource(R.mipmap.gender_logo_nan);
                }else if (gender == 2){
                    gender = 1;
                    imgSelecteGender.setImageResource(R.mipmap.gender_logo_nv);
                }
                pageNum = 1;
                initData();
                break;
            case R.id.el_Search:
                //搜索
                SearchActivity.start(getContext());
                break;
            case R.id.tv_nearby:
                //附近
                type = 1;
                pageNum = 1;
                initData();
                break;
            case R.id.tv_newRegister:
                //新注册
                type = 2;
                pageNum = 1;
                initData();
                break;
            case R.id.tv_goddess:
                //女神
                type = 3;
                pageNum = 1;
                initData();
                break;
            case R.id.tv_onLine:
                //在线
                if (isOnLine){
                    isOnLine = false;
                    tvOnLine.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.online_no_logo),null, null, null);
                }else {
                    isOnLine = true;
                    tvOnLine.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.online_yes_logo),null, null, null);
                }
                pageNum = 1;
                initData();
                break;
        }
    }

    /**
     * 切换搜索类型
     * */
    private void toggleSearchType() {
        tvNearby.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvNewRegister.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvGoddess.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvNearby.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.drawable.line_not_selected));
        tvNewRegister.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.drawable.line_not_selected));
        tvGoddess.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.drawable.line_not_selected));
        switch (type){
            case 1:
                tvNearby.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvNearby.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                break;
            case 2:
                tvNewRegister.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvNewRegister.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                break;
            case 3:
                tvGoddess.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvGoddess.setCompoundDrawablesWithIntrinsicBounds(null,null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                break;
        }
        refreshList();
    }

    /**
     * 根据type  和  isOnLine刷新列表
     * */
    private void refreshList() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
