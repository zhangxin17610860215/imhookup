package com.yuanqi.hangzhou.imhookup.me;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseFragment;
import com.yuanqi.hangzhou.imhookup.utils.UMShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yuanqi.hangzhou.imhookup.main.MainActivity.REQUEST_PERMISSION;

/**
 * 我
 */
public class MeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_Occupation)
    TextView tvOccupation;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_seeNum)
    TextView tvSeeNum;
    @BindView(R.id.tv_BurnDownNum)
    TextView tvBurnDownNum;

    private Activity mActivity;

    public MeFragment() {

    }

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
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

    }

    private void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_editingmaterials, R.id.img_header, R.id.tv_addVIP, R.id.tv_Authentication, R.id.tv_PrivacySetting, R.id.tv_dynamic, R.id.tv_myAlbum, R.id.tv_evaluate, R.id.tv_myLike, R.id.tv_Blacklist, R.id.tv_recovery, R.id.tv_Setting, R.id.tv_Share, R.id.tv_Help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_editingmaterials:
                //编辑资料
                break;
            case R.id.img_header:
                //头像
                break;
            case R.id.tv_addVIP:
                //立即加入
                break;
            case R.id.tv_Authentication:
                //真人认证
                break;
            case R.id.tv_PrivacySetting:
                //隐私与连麦设置
                break;
            case R.id.tv_dynamic:
                //我的动态
                break;
            case R.id.tv_myAlbum:
                //我的相册
                break;
            case R.id.tv_evaluate:
                //我的评价
                break;
            case R.id.tv_myLike:
                //我喜欢的
                break;
            case R.id.tv_Blacklist:
                //黑名单
                break;
            case R.id.tv_recovery:
                //一键恢复
                break;
            case R.id.tv_Setting:
                //设置
                break;
            case R.id.tv_Share:
                //分享
                UMShareUtil.shareText(mActivity, "http://baidu.com", "这是标题", R.mipmap.icon, "这是内容", new UMShareUtil.ShareListener() {
                    @Override
                    public void onStart() {
                        Log.e("TAG","开始分享");
                    }

                    @Override
                    public void onResult() {
                        Log.e("TAG","分享成功");
                    }

                    @Override
                    public void onError() {
                        Log.e("TAG","分享失败");
                    }

                    @Override
                    public void onCancel() {
                        Log.e("TAG","取消分享");
                    }
                });
                break;
            case R.id.tv_Help:
                //客服帮助
                break;
        }
    }

}
