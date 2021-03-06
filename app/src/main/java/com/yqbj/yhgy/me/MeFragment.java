package com.yqbj.yhgy.me;

import android.content.Intent;
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

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePicker;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.ui.ImageGridActivity;
import com.netease.nim.uikit.common.util.CityBean;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseFragment;
import com.yqbj.yhgy.bean.EvaluateDataBean;
import com.yqbj.yhgy.bean.MyAlbumBean;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.bean.UpLoadPhotoBean;
import com.yqbj.yhgy.bean.UserInfoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.login.VipCoreActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.EventBusUtils;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.utils.TimeUtils;
import com.yqbj.yhgy.utils.UMShareUtil;
import com.yqbj.yhgy.utils.ZodiacUtil;
import com.yqbj.yhgy.view.EvaluateDialog;
import com.yqbj.yhgy.view.MiddleDialog;
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

    private UserInfoBean userInfoBean = new UserInfoBean();
    private UserInfoBean.UserDetailsBean userDetailsBean = new UserInfoBean.UserDetailsBean();
    private UserInfoBean.ConfigBean configBean = new UserInfoBean.ConfigBean();
    private UserInfoBean.ContactInfoBean contactInfoBean = new UserInfoBean.ContactInfoBean();
    private List<UserInfoBean.PhotoAlbumBean> photoAlbumBean = new ArrayList<>();
    private UserInfoBean.WalletBean walletBean = new UserInfoBean.WalletBean();
    private List<PhotoBean> list = new ArrayList<>();
    private List<PhotoBean> choiceList = new ArrayList<>();
    private List<PhotoBean> photoList = new ArrayList<>();
    private EasyRVAdapter mAdapter;
    private int sendImageNum = 0;
    private String accid = "";

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
        accid = DemoCache.getAccount();
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
                    userInfoBean = (UserInfoBean) object;
                    userDetailsBean = userInfoBean.getUserDetails();
                    configBean = userInfoBean.getConfig();
                    contactInfoBean = userInfoBean.getContactInfo();
                    photoAlbumBean = userInfoBean.getPhotoAlbum();
                    walletBean = userInfoBean.getWallet();
                    Preferences.saveVipMember(userDetailsBean.getVipMember());
                    Preferences.saveCertification(userDetailsBean.getCertification()+"");
                    Preferences.saveLabeltype(userDetailsBean.getLabeltype()+"");
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
                    list.clear();
                    for (UserInfoBean.PhotoAlbumBean albumBean : photoAlbumBean){
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.setId(albumBean.getId());
                        photoBean.setYellowish(albumBean.getCheckFlag() == 0);
                        photoBean.setBurnAfterReading(albumBean.getStatusFlag()==1);
                        photoBean.setRedEnvelopePhotos(albumBean.getPayFlag()==1);
                        photoBean.setPhotoUrl(albumBean.getUrl());
                        photoBean.setFee(albumBean.getFee()+"");
                        photoBean.setOneself(albumBean.getLabelFlag()>0);
                        list.add(photoBean);
                    }
                    initData();
                    if (StringUtil.isEmpty(userDetailsBean.getDescription()) && Constants.ISSHOWINTRODUCEDIALOG){
                        new XPopup.Builder(mActivity)
                                .dismissOnTouchOutside(false)
                                .asCustom(new MiddleDialog(mActivity, "温馨提示", "你还没填写个人介绍，吸引人的个人介绍对交友成功率影响很大哦，你要补充一下吗?", new MiddleDialog.Listener() {
                                    @Override
                                    public void onConfirmClickListener() {
                                        PerfectDataActivity.start(mActivity,"1",userInfoBean);
                                        Constants.ISSHOWINTRODUCEDIALOG = false;
                                    }

                                    @Override
                                    public void onCloseClickListener() {

                                    }
                                }))
                                .show();
                        return;
                    }
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
        if (null != mAdapter){
            mAdapter.notifyDataSetChanged();
            return;
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

                tvMengceng.setVisibility((list.size() - 8) > 0 && position == 7 ? View.VISIBLE : View.GONE);
                tvMengceng.setText("+" + (list.size() - 8));

                if (photoBean.isYellowish()){
                    rlBurnAfterReading.setVisibility(View.GONE);
                    rlRedEnvelopePhotos.setVisibility(View.GONE);
                    Glide.with(mActivity).load(R.mipmap.shehuang_bg).into(imgHead);
                }else {
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
                    }

                    Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);

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

                    rlBurnAfterReading.setVisibility(photoBean.isBurnAfterReading() ? View.VISIBLE : View.GONE);
                    rlRedEnvelopePhotos.setVisibility(photoBean.isRedEnvelopePhotos() ? View.VISIBLE : View.GONE);
                    tvIsBenRen.setVisibility(photoBean.isOneself() ? View.VISIBLE : View.GONE);
                }

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
                        intent.putExtra("isRequest",true);
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
        if (!NoDoubleClickUtils.isDoubleClick(500)) {
            switch (view.getId()) {
                case R.id.img_editingmaterials:
                    //编辑资料
                    PerfectDataActivity.start(mActivity, "1", userInfoBean);
                    break;
                case R.id.img_header:
                    //头像
                    break;
                case R.id.tv_addVIP:
                    //立即加入
                    VipCoreActivity.start(mActivity);
                    break;
                case R.id.tv_Authentication:
                    //真人认证
                    if (userDetailsBean.getGender() == 1) {
                        if (Preferences.getCertification().equals("1")) {
                            //男号已通过真人认证
                            RealPersonCertificationActivity.start(mActivity);
                        } else {
                            //男号未通过真人认证
                            AuthenticationActivity.start(mActivity);
                        }
                    } else {
                        AuthenticationActivity.start(mActivity);
                    }
                    break;
                case R.id.tv_wallet:
                    //钱包
                    WalletActivity.start(mActivity);
                    break;
                case R.id.tv_PrivacySetting:
                    //隐私设置
                    PrivacySettingActivity.start(mActivity, configBean, contactInfoBean.getHidecontactinfo() + "");
                    break;
                case R.id.tv_dynamic:
                    //我的动态
                    MyDynamicsActivity.start(mActivity);
                    break;
                case R.id.tv_myAlbum:
                    //我的相册
                    choiceList.clear();
                    showSelector(R.string.input_panel_photo, 100, true, 9);
                    break;
                case R.id.tv_evaluate:
                    //我的评价
                    showEvaluateDialog();
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
                            Log.e("TAG", "开始分享");
                        }

                        @Override
                        public void onResult() {
                            Log.e("TAG", "分享成功");
                        }

                        @Override
                        public void onError() {
                            Log.e("TAG", "分享失败");
                        }

                        @Override
                        public void onCancel() {
                            Log.e("TAG", "取消分享");
                        }
                    });
                    break;
                case R.id.tv_Help:
                    //客服帮助
                    break;
            }
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
                //查看图片
                choiceList = (List<PhotoBean>) data.getSerializableExtra("photoList");
                String type = data.getStringExtra("type");
                if (StringUtil.isEmpty(type)){
                    return;
                }
                if (type.equals("1")){
                    if (choiceList.size() < 1){
                        return;
                    }
                    for (PhotoBean photoBean : list){
                        if (photoBean.getPhotoUrl().equals(choiceList.get(0).getPhotoUrl())){
                            return;
                        }
                    }
                    upLoadPhoto();
                }else if (type.equals("2")){
                    getMyAlbum();
                }
                break;
            case 20:
                //查看相册
                list = (List<PhotoBean>) data.getSerializableExtra("photoList");
                initData();
                break;
        }
    }

    /**
     * 上传照片
     * */
    private void upLoadPhoto() {
        showProgress(false);
        List<UpLoadPhotoBean> upLoadPhotoBeans = new ArrayList<>();
        for (PhotoBean photoBean : choiceList){
            UpLoadPhotoBean bean = new UpLoadPhotoBean();
            bean.setType(1);
            bean.setRedPacketFee(photoBean.isRedEnvelopePhotos()?1:0);
            bean.setStatusFlag(photoBean.isBurnAfterReading()?1:0);
            bean.setUrl(photoBean.getPhotoUrl());
            upLoadPhotoBeans.add(bean);
        }
        String multimediaeInfo = JSON.toJSONString(upLoadPhotoBeans);
        UserApi.upLoadPhoto(multimediaeInfo, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    list.addAll(choiceList);
                    initData();
                    getMyAlbum();
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

    /**
     * 获取我的相册
     * */
    private void getMyAlbum() {
        showProgress(false);
        UserApi.getMyAlbum(mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    List<MyAlbumBean> albumBeanList = (List<MyAlbumBean>) object;
                    list.clear();
                    for (MyAlbumBean albumBean : albumBeanList){
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.setId(albumBean.getId());
                        photoBean.setYellowish(albumBean.getCheckFlag() == 0);
                        photoBean.setBurnAfterReading(albumBean.getStatusFlag()==1);
                        photoBean.setRedEnvelopePhotos(albumBean.getPayFlag()==1);
                        photoBean.setPhotoUrl(albumBean.getUrl());
                        photoBean.setFee(albumBean.getFee()+"");
                        photoBean.setOneself(albumBean.getLabelFlag()>0);
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
                fileToUrl(file,imgListSize);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 将file转换成URL
     * */
    private void fileToUrl(File file,int imgListSize) {
        if (file == null) {
            return;
        }
        NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG).setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int i, final String url, Throwable throwable) {
                if (i != ResponseCode.RES_SUCCESS){
                    toast("图片上传失败，请稍后重试");
                    return;
                }
                sendImageNum++;
                PhotoBean photoBean = new PhotoBean();
                photoBean.setBurnAfterReading(false);
                photoBean.setPhotoUrl(url);
                choiceList.add(photoBean);
                if (sendImageNum == imgListSize){
//                    upLoadPhoto();
                    Intent intent = new Intent();
                    intent.setClass(mActivity, LookPhotoActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("photoList", (Serializable) choiceList);
                    intent.putExtra("accId",NimUIKit.getAccount());
                    intent.putExtra("type","1");
                    intent.putExtra("isShowButton",true);
                    intent.putExtra("isRequest",false);
                    startActivityForResult(intent, 10);
                }
            }
        });
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

    /**
     * 显示评价弹窗
     * */
    private void showEvaluateDialog() {
        showProgress(false);
        UserApi.getEvalualeData(accid, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    List<EvaluateDataBean> dataList = (List<EvaluateDataBean>) object;
                    new XPopup.Builder(mActivity)
                            .dismissOnTouchOutside(false)
                            .asCustom(new EvaluateDialog(accid, userDetailsBean.getGender()+"", mActivity, dataList, new EvaluateDialog.EvaluateListener() {
                                @Override
                                public void evaluateOnClick(String[] ids) {

                                }
                            }))
                            .show();
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
