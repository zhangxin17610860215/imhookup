package com.yqbj.yhgy.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePicker;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.ui.ImageGridActivity;
import com.netease.nim.uikit.common.ui.widget.BlurTransformation;
import com.netease.nim.uikit.common.util.CityBean;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseFragment;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.bean.UserInfoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.login.VipCoreActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.ApiUrl;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.EventBusUtils;
import com.yqbj.yhgy.utils.ImageFilter;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.TimeUtils;
import com.yqbj.yhgy.utils.UMShareUtil;
import com.yqbj.yhgy.utils.ZodiacUtil;
import com.yqbj.yhgy.utils.pay.MyALipayUtils;
import com.yqbj.yhgy.wxapi.WXUtil;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yqbj.yhgy.MyApplication.ALIPAY_APPID;
import static com.yqbj.yhgy.config.Constants.OCCUPATIONBEANLIST;

/**
 * 我
 */
public class MeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.img_header)
    RoundedImageView imgHeader;
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
    @BindView(R.id.tv_ZhenRen)
    TextView tvZhenRen;
    @BindView(R.id.tv_NvShen)
    TextView tvNvShen;
    @BindView(R.id.tv_BurnDownNum)
    TextView tvBurnDownNum;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.ll_vipBg)
    LinearLayout llVipBg;
    @BindView(R.id.ll_noData)
    LinearLayout llNoData;

    private UserInfoBean.UserDetailsBean userDetailsBean;
    private UserInfoBean.ConfigBean configBean;
    private UserInfoBean.ContactInfoBean contactInfoBean;
    private List<UserInfoBean.PhotoAlbumBean> photoAlbumBean;
    private UserInfoBean.WalletBean walletBean;
    private List<PhotoBean> burnAfterReadingList = new ArrayList<>();
    private List<PhotoBean> list = new ArrayList<>();
    private List<PhotoBean> photoList = new ArrayList<>();
    private EasyRVAdapter mAdapter;
    private int sendImageNum = 0;

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
        EventBusUtils.register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.REFRESH){
            onVisible();
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        showProgress(false);
        UserApi.getUserDetails(mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    Constants.REFRESH = false;
                    UserInfoBean userInfoBean = (UserInfoBean) object;
                    userDetailsBean = userInfoBean.getUserDetails();
                    configBean = userInfoBean.getConfig();
                    contactInfoBean = userInfoBean.getContactInfo();
                    photoAlbumBean = userInfoBean.getPhotoAlbum();
                    walletBean = userInfoBean.getWallet();
                    Glide.with(mActivity).load(userDetailsBean.getHeadUrl()).placeholder(R.mipmap.default_head_logo).error(R.mipmap.default_head_logo).into(imgHeader);
                    tvPlace.setText(Preferences.getCity());
                    tvAge.setText(ZodiacUtil.date2Constellation(userDetailsBean.getBirthday()) + "-" + TimeUtils.getAgeFromBirthTime(userDetailsBean.getBirthday()) + "岁");
                    String job = "";
                    CityBean occupationBean;
                    for (int i = 0; i < OCCUPATIONBEANLIST.size(); i++) {
                        occupationBean = OCCUPATIONBEANLIST.get(i);
                        for (int j = 0; j < occupationBean.getChild().size(); j++) {
                            List<CityBean.ChildBeanX> occupation = OCCUPATIONBEANLIST.get(i).getChild();
                            if (userDetailsBean.getJob().equals(occupation.get(j).getValue())) {
                                job = occupation.get(j).getText();
                            }
                        }
                    }
                    tvOccupation.setText(job);
                    tvZhenRen.setVisibility(userDetailsBean.getCertification() == 1 ? View.VISIBLE : View.GONE);
                    tvNvShen.setVisibility(userDetailsBean.getGender() == 2 ? userDetailsBean.getLabeltype() == 1 ? View.VISIBLE : View.GONE : View.GONE);
                    llVipBg.setVisibility(userDetailsBean.getGender() == 1 ? userDetailsBean.getVipMember() == 0 ? View.VISIBLE : View.GONE : View.GONE);
                    tvWallet.setText(walletBean.getMoney()+"元," + walletBean.getCurrency() + "约会币");

                    if (null != photoAlbumBean && photoAlbumBean.size() > 0){
                        photoAlbumBean.clear();
                    }
                    for (int i = 0; i < 10; i++){
                        UserInfoBean.PhotoAlbumBean albumBean = new UserInfoBean.PhotoAlbumBean();
                        albumBean.setType(1);
                        if (i == 2 || i == 6){
                            albumBean.setStatusFlag(1);
                        }else {
                            albumBean.setSelfFlag(1);
                        }
                        if (i == 3 || i == 5 || i == 6){
                            albumBean.setPayFlag(1);
                            albumBean.setFee(3);
                        }else {
                            albumBean.setSelfFlag(1);
                        }
                        albumBean.setUrl("https://nim-nosdn.netease.im/MTY3Njc1MDE=/bmltYV8xNTg4OTM1MDc0Ml8xNTc4Mjk5MDgwOTE3XzNiZDI5MGJmLTE0OGItNDNkNy1hMjNhLWYxNzQxMGQ1MGM0Zg==");
                        photoAlbumBean.add(albumBean);
                    }
                    list.clear();
                    for (UserInfoBean.PhotoAlbumBean albumBean : photoAlbumBean){
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.setBurnAfterReading(albumBean.getStatusFlag()==1 ? true : false);
                        photoBean.setRedEnvelopePhotos(albumBean.getPayFlag()==1? true : false);
                        photoBean.setPhotoUrl(albumBean.getUrl());
                        photoBean.setFee(albumBean.getFee()+"");
                        photoBean.setOneself(albumBean.getSelfFlag()==1? true : false);
                        list.add(photoBean);
                    }
                    initData();
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

    private void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
    }

    private void initData() {
        if (list.size() <= 0){
            llNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            llNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            photoList.clear();
            for (int i = 0; list.size() > 8 ? i < 8 : i < list.size(); i ++){
                photoList.add(list.get(i));
            }
        }
        mAdapter = new EasyRVAdapter(mActivity,photoList,R.layout.item_mephoto_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                PhotoBean photoBean = photoList.get(position);
                RoundedImageView imgHead = viewHolder.getView(R.id.img_head);
                RelativeLayout rlBurnAfterReading = viewHolder.getView(R.id.rl_BurnAfterReading);
                TextView tvBurnedDown = viewHolder.getView(R.id.tv_BurnedDown);
                TextView tvIsBenRen = viewHolder.getView(R.id.tv_isBenRen);
                RelativeLayout rlRedEnvelopePhotos = viewHolder.getView(R.id.rl_RedEnvelopePhotos);
                TextView tvRedEnvelopePhotos = viewHolder.getView(R.id.tv_RedEnvelopePhotos);
                TextView tvMengceng = viewHolder.getView(R.id.tv_mengceng);

                if (photoBean.isBurnAfterReading()){
                    if (photoBean.isBurnedDown()){
                        rlBurnAfterReading.setBackgroundResource(R.mipmap.burneddown_bg_logo);
                        tvBurnedDown.setText("已焚毁");
                        tvBurnedDown.setBackgroundResource(R.drawable.burneddown_bg_shape);
                    }else {
                        rlBurnAfterReading.setBackgroundResource(R.mipmap.burnafterreading_bg_logo);
                        tvBurnedDown.setText("阅后即焚");
                        tvBurnedDown.setBackgroundResource(R.mipmap.burnafterreading_logo);
                    }
                }else {
                    Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                }

                if (photoBean.isRedEnvelopePhotos() && photoBean.isBurnAfterReading()){
                    //阅后即焚的红包照片
                    if (photoBean.isBurnedDown()){
                        rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redburneddown_bg_logo);
                        tvRedEnvelopePhotos.setText("已焚毁");
                        tvRedEnvelopePhotos.setBackgroundResource(R.drawable.burneddown_bg_shape);
                    }else {
                        rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redenvelopephotos_bg_logo);
                        tvRedEnvelopePhotos.setText(photoBean.isRedEnvelopePhotosPaid() ? "已付费" : "阅后即焚的红包照片");
                        tvRedEnvelopePhotos.setBackgroundResource(R.mipmap.burnafterreading_logo);
                    }
                }else if (photoBean.isRedEnvelopePhotos()){
                    //只是红包照片
                    rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redenvelopephotos_bg_logo);
                    tvRedEnvelopePhotos.setText(photoBean.isRedEnvelopePhotosPaid() ? "已付费" : "红包照片");
                    tvRedEnvelopePhotos.setBackgroundResource(R.mipmap.burnafterreading_logo);
                }

                tvMengceng.setVisibility((list.size() - 8) > 0 && position == 7 ? View.VISIBLE : View.GONE);
                tvMengceng.setText("+" + (list.size() - 8));
                rlBurnAfterReading.setVisibility(photoBean.isBurnAfterReading() ? View.VISIBLE : View.GONE);
                rlRedEnvelopePhotos.setVisibility(photoBean.isRedEnvelopePhotos() ? View.VISIBLE : View.GONE);
                tvIsBenRen.setVisibility(photoBean.isOneself() ? View.VISIBLE : View.GONE);
                tvMengceng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入我的相册
                        Intent intent = new Intent();
                        intent.setClass(mActivity, MyPhotoActivity.class);
                        intent.putExtra("photoList", (Serializable) list);
                        intent.putExtra("isShowButton",true);
                        intent.putExtra("Gender",userDetailsBean.getGender()+"");
                        startActivityForResult(intent, 20);
                    }
                });
                imgHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看删除该照片
                        Intent intent = new Intent();
                        intent.setClass(mActivity, LookPhotoActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("photoList", (Serializable) list);
                        intent.putExtra("accId",NimUIKit.getAccount());
                        intent.putExtra("type","2");
                        intent.putExtra("isShowButton",true);
                        startActivityForResult(intent, 10);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_editingmaterials, R.id.tv_wallet, R.id.img_header, R.id.tv_addVIP, R.id.tv_Authentication, R.id.tv_PrivacySetting, R.id.tv_dynamic, R.id.tv_myAlbum, R.id.tv_evaluate, R.id.tv_myLike, R.id.tv_Blacklist, R.id.tv_recovery, R.id.tv_Setting, R.id.tv_Share, R.id.tv_Help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_editingmaterials:
                //编辑资料
                PerfectDataActivity.start(mActivity,"1");
                break;
            case R.id.img_header:
                //头像
                WXUtil.weiChatPay(mActivity);
                break;
            case R.id.tv_addVIP:
                //立即加入
                VipCoreActivity.start(mActivity);
                break;
            case R.id.tv_Authentication:
                //真人认证
                AuthenticationActivity.start(mActivity);
                break;
            case R.id.tv_wallet:
                //钱包
                WalletActivity.start(mActivity);
                break;
            case R.id.tv_PrivacySetting:
                //隐私与连麦设置
                PrivacySettingActivity.start(mActivity);
                break;
            case R.id.tv_dynamic:
                //我的动态
                MyDynamicsActivity.start(mActivity);
                break;
            case R.id.tv_myAlbum:
                //我的相册
                showSelector(R.string.input_panel_photo, 100, true, 9);
                break;
            case R.id.tv_evaluate:
                //我的评价
                break;
            case R.id.tv_myLike:
                //我喜欢的
                MyLikeActivity.start(mActivity);
                break;
            case R.id.tv_Blacklist:
                //黑名单
                BlacklistActivity.start(mActivity);
                break;
            case R.id.tv_recovery:
                //一键恢复
                final MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                MyALipayUtils myALipayUtils = builder.setAppid(ALIPAY_APPID)
                        .setMoney("12")       //设置金额
                        .setTitle("杭州吾乐玩网络科技有限公司")     //设置商品信息
                        .setBody("杭州吾乐玩网络科技有限公司")       //设置商品信息描述
                        .setOrderTradeId("123123132")   //设置订单ID
                        .setNotifyUrl(ApiUrl.BASE_URL_HEAD + ApiUrl.BASE_URL + "/notify/alipay") //服务器异步通知页面路径
                        .build();
                myALipayUtils.goAliPay("112",mActivity);
                break;
            case R.id.tv_Setting:
                //设置
                SettingsActivity.start(mActivity);
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

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, int requestCode, boolean multiSelect, int number) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(number);
        option.setSaveRectangle(true);
//        ImagePickerLauncher.selectImage(mActivity, requestCode, option, titleId);
        ImagePicker.getInstance().setOption(option);
        Intent intent = new Intent(mActivity, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) return;
        switch (requestCode) {
            case 100:
                onPickImageActivityResult(requestCode, data);
                break;
            case 10:
            case 20:
                list = (List<PhotoBean>) data.getSerializableExtra("photoList");
                if (list.size() <= 0){
                    llNoData.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }else {
                    llNoData.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    photoList.clear();
                    for (int i = 0; list.size() > 8 ? i < 8 : i < list.size(); i ++){
                        photoList.add(list.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 图片选取回调
     */
    private void onPickImageActivityResult(int requestCode, Intent data) {
        if (data == null) {
            ToastHelper.showToastLong(mActivity, R.string.picker_image_error);
            return;
        }
        sendImageAfterSelfImagePicker(data);
    }

    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        sendImageNum = 0;
        SendImageHelper.sendImageAfterSelfImagePicker(mActivity, data, new SendImageHelper.Callback() {
            @Override
            public void sendImage(File file, boolean isOrig, int imgListSize) {
                sendImageNum++;
                PhotoBean photoBean = new PhotoBean();
                photoBean.setBurnAfterReading(false);
                photoBean.setPhotoUrl(file.getPath());
                list.add(photoBean);
                if (sendImageNum == imgListSize){
                    Intent intent = new Intent();
                    intent.setClass(mActivity, LookPhotoActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("photoList", (Serializable) list);
                    intent.putExtra("accId",NimUIKit.getAccount());
                    intent.putExtra("type","1");
                    intent.putExtra("isShowButton",true);
                    startActivityForResult(intent, 10);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 微信支付结果回调
     * */
    @Subscribe
    public void getWXPayData(EventBusUtils.CommonEvent commonEvent){
        if (null == commonEvent) {
            return;
        }
        if (null == commonEvent.data){
            dismissProgress();
            return;
        }
        Bundle bundle = commonEvent.data;
        String result = (String) bundle.get("payResult");
        Log.e("TAG","微信支付结果回调>>>>>>>>" + result);
    }

}
