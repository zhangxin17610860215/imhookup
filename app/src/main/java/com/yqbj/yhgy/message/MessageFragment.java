package com.yqbj.yhgy.message;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseFragment;
import com.yqbj.yhgy.main.SessionHelper;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 消息
 */
public class MessageFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.tv_Message)
    TextView tvMessage;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChat;
    @BindView(R.id.img_system_header)
    ImageView imgSystemHeader;
    @BindView(R.id.tv_system_time)
    TextView tvSystemTime;
    @BindView(R.id.tv_system_name)
    TextView tvSystemName;
    @BindView(R.id.tv_system_context)
    TextView tvSystemContext;
    @BindView(R.id.img_Fabulous_header)
    ImageView imgFabulousHeader;
    @BindView(R.id.tv_Fabulous_time)
    TextView tvFabulousTime;
    @BindView(R.id.tv_Fabulous_name)
    TextView tvFabulousName;
    @BindView(R.id.tv_Fabulous_context)
    TextView tvFabulousContext;
    @BindView(R.id.img_comment_header)
    ImageView imgCommentHeader;
    @BindView(R.id.tv_comment_time)
    TextView tvCommentTime;
    @BindView(R.id.tv_comment_name)
    TextView tvCommentName;
    @BindView(R.id.tv_comment_context)
    TextView tvCommentContext;
    @BindView(R.id.img_seeApply_header)
    ImageView imgSeeApplyHeader;
    @BindView(R.id.tv_seeApply_time)
    TextView tvSeeApplyTime;
    @BindView(R.id.tv_seeApply_name)
    TextView tvSeeApplyName;
    @BindView(R.id.tv_seeApply_context)
    TextView tvSeeApplyContext;
    @BindView(R.id.ll_systemMessage)
    LinearLayout llSystemMessage;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private int type = 1;//类型    1=聊天   2=系统消息

    public MessageFragment() {

    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
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
        toggleSearchType();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setRefreshHeader(new ClassicsHeader(mActivity));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新
            }
        });

        refreshLayout.setRefreshFooter(new ClassicsFooter(mActivity));
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
        mAdapter = new EasyRVAdapter(mActivity, list, R.layout.message_chat_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, int position, Object item) {
                ImageView imgHeader = viewHolder.getView(R.id.img_header);
                TextView tvTime = viewHolder.getView(R.id.tv_time);
                TextView tvName = viewHolder.getView(R.id.tv_name);
                TextView tvContext = viewHolder.getView(R.id.tv_context);

            }
        };
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EasyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object item) {
//                NimUIKitImpl.getContactEventListener().onItemClick(getActivity(), (((ContactItem) item).getContact()).getContactId());
                SessionHelper.startP2PSession(mActivity, "123123");
            }
        });
    }

    private void toggleSearchType() {
        tvChat.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvMessage.setTextColor(mActivity.getResources().getColor(R.color.black));
        tvChat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.line_not_selected));
        tvMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.line_not_selected));
        switch (type) {
            case 1:
                llSystemMessage.setVisibility(View.GONE);
                rlChat.setVisibility(View.VISIBLE);
                tvChat.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvChat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                break;
            case 2:
                llSystemMessage.setVisibility(View.VISIBLE);
                rlChat.setVisibility(View.GONE);
                tvMessage.setTextColor(mActivity.getResources().getColor(R.color.text_theme_color));
                tvMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.mipmap.xiabiao_logo));
                break;
        }
        refreshList();
    }

    /**
     * 根据type  刷新列表
     */
    private void refreshList() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_system, R.id.rl_Fabulous, R.id.rl_comment, R.id.rl_seeApply, R.id.img_setting, R.id.tv_chat, R.id.tv_Message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_system:
                //系统
                break;
            case R.id.rl_Fabulous:
                //点赞
                break;
            case R.id.rl_comment:
                //评论
                break;
            case R.id.rl_seeApply:
                //查看权限
                break;
            case R.id.img_setting:
                //设置
                MessageSettingActivity.start(mActivity);
                break;
            case R.id.tv_chat:
                //聊天
                type = 1;
                toggleSearchType();
                break;
            case R.id.tv_Message:
                //系统消息
                type = 2;
                toggleSearchType();
                break;
        }
    }
}
