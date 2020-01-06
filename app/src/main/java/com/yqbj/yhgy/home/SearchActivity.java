package com.yqbj.yhgy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.view.MorePopupView;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    private void initData() {
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
                RoundedImageView imgHeader = viewHolder.getView(R.id.img_home_header);//头像
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

    @OnClick(R.id.img_delete)
    public void onViewClicked() {
        etSearch.setText("");
    }
}
