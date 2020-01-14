package com.yqbj.yhgy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.widget.BlurTransformation;
import com.netease.nim.uikit.common.util.CityBean;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.EvaluateDataBean;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.bean.UserInfoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.me.LookPhotoActivity;
import com.yqbj.yhgy.me.MyPhotoActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.NumberUtil;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.utils.TimeUtils;
import com.yqbj.yhgy.utils.ZodiacUtil;
import com.yqbj.yhgy.view.ChatCautionDialog;
import com.yqbj.yhgy.view.EvaluateDialog;
import com.yqbj.yhgy.view.PaySelect;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yqbj.yhgy.config.Constants.CITYBEANLIST;
import static com.yqbj.yhgy.config.Constants.OCCUPATIONBEANLIST;

/**
 * 查看对方详情页面
 */
public class DetailsActivity extends BaseActivity {

    @BindView(R.id.img_evaluate)
    ImageView imgEvaluate;                      //评论img
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;                        //评论text
    @BindView(R.id.img_chat)
    ImageView imgChat;                          //私聊img
    @BindView(R.id.tv_chat)
    TextView tvChat;                            //私聊text
    @BindView(R.id.img_SocialContact)
    ImageView imgSocialContact;                 //社交img
    @BindView(R.id.tv_SocialContact)
    TextView tvSocialContact;                   //社交text
    @BindView(R.id.tv_addLike)
    TextView tvAddLike;                         //加入喜欢
    @BindView(R.id.tv_name)
    TextView tvName;                            //网名
    @BindView(R.id.tv_place)
    TextView tvPlace;                           //地点
    @BindView(R.id.tv_age)
    TextView tvAge;                             //星座年龄
    @BindView(R.id.tv_Occupation)
    TextView tvOccupation;                      //职业
    @BindView(R.id.tv_adoptAdd)
    TextView tvAdoptAdd;                        //她通过系统发放的验证码进入公园
    @BindView(R.id.tv_adoptNvShen)
    TextView tvAdoptNvShen;                     //通过系统女神认证
    @BindView(R.id.tv_adoptZhenRen)
    TextView tvAdoptZhenRen;                    //通过系统真人认证
    @BindView(R.id.img_header)
    RoundedImageView imgHeader;                 //头像
    @BindView(R.id.tv_distance)
    TextView tvDistance;                        //距离
    @BindView(R.id.tv_home_online)
    TextView tvHomeOnline;                      //在线
    @BindView(R.id.tv_home_PaidAlbum)
    TextView tvHomePaidAlbum;                   //付费相册
    @BindView(R.id.tv_ApplySee)
    TextView tvApplySee;                        //立即查看
    @BindView(R.id.ll_NoJurisdiction)
    LinearLayout llNoJurisdiction;              //没有查看权限
    @BindView(R.id.tv_Album)
    TextView tvAlbum;                           //他的相册
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_height)
    TextView tvHeight;                          //身高
    @BindView(R.id.tv_weight)
    TextView tvWeight;                          //体重
    @BindView(R.id.tv_onCity)
    TextView tvOnCity;                          //常驻城市
    @BindView(R.id.tv_program)
    TextView tvProgram;                         //交友节目
    @BindView(R.id.tv_Expect)
    TextView tvExpect;                          //期望对象
    @BindView(R.id.tv_wechat)
    TextView tvWechat;                          //微信
    @BindView(R.id.tv_QQ)
    TextView tvQQ;                              //QQ
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;                       //个人介绍
    @BindView(R.id.ll_YseJurisdiction)
    LinearLayout llYseJurisdiction;             //有查看权限
    @BindView(R.id.rl_QQ)
    RelativeLayout rlQQ;
    @BindView(R.id.rl_weChat)
    RelativeLayout rlWeChat;
    @BindView(R.id.rl_height)
    RelativeLayout rlHeight;
    @BindView(R.id.view_height)
    View viewHeight;
    @BindView(R.id.rl_weight)
    RelativeLayout rlWeight;
    @BindView(R.id.view_weight)
    View viewWeight;
    @BindView(R.id.rl_program)
    RelativeLayout rlProgram;
    @BindView(R.id.view_program)
    View viewProgram;
    @BindView(R.id.rl_Expect)
    RelativeLayout rlExpect;
    @BindView(R.id.view_Expect)
    View viewExpect;
    @BindView(R.id.view_weChat)
    View viewWeChat;
    @BindView(R.id.view_qq)
    View viewQq;
    @BindView(R.id.rl_introduce)
    RelativeLayout rlIntroduce;
    @BindView(R.id.ll_albumlock)
    LinearLayout llAlbumlock;
    @BindView(R.id.tv_albumlock)
    TextView tvAlbumlock;
    @BindView(R.id.tv_Unlock)
    TextView tvUnlock;
    @BindView(R.id.tv_setalbumlock)
    TextView tvSetalbumlock;
    @BindView(R.id.tv_NoPhoto)
    TextView tvNoPhoto;

    private Activity mActivity;
    private int addLike = 2;                    //是否加入喜欢     1=移除     2=加入
    private int addBlacklist = 2;               //是否加入黑名单     1=移除     2=加入
    private String accid = "";
    private String albumMoney = "";
    private UserInfoBean.UserDetailsBean userDetailsBean;
    private UserInfoBean.ConfigBean configBean;
    private UserInfoBean.ContactInfoBean contactInfoBean;
    private List<UserInfoBean.PhotoAlbumBean> photoAlbumBean;
    private List<PhotoBean> list = new ArrayList<>();
    private List<PhotoBean> photoList = new ArrayList<>();
    private EasyRVAdapter mAdapter;

    public static void start(Context context, String accid) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("accid", accid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        accid = getIntent().getStringExtra("accid");
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        setRightImg(mActivity, R.mipmap.more_icon, new onToolBarRightImgListener() {
            @Override
            public void onRight(View view) {
                //更多
                addBlacklist = addBlacklist == 1? 2:1;
                new XPopup.Builder(mActivity).asBottomList(null, new String[]{addBlacklist==1?"加入黑名单":"移除黑名单", "匿名举报"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 1) {
                                    //匿名举报
                                    AnonymousReportActivity.start(mActivity);
                                } else {
                                    //黑名单
                                    operatorBlackList();
                                }
                            }
                        })
                        .show();
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
    }

    /**
     * 操作黑名单
     * */
    private void operatorBlackList() {
        showProgress(false);
        UserApi.operatorBlackList(accid, addBlacklist, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){

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

    private void initData() {
        showProgress(false);
        UserApi.getTargetDetails(accid, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE) {
                    UserInfoBean userInfoBean = (UserInfoBean) object;
                    userDetailsBean = userInfoBean.getUserDetails();
                    configBean = userInfoBean.getConfig();
                    contactInfoBean = userInfoBean.getContactInfo();
                    photoAlbumBean = userInfoBean.getPhotoAlbum();
                    loadDetails();
                    initPhotoAlbum();
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

    /**
     * 加载相册信息
     * */
    private void initPhotoAlbum() {
        list.clear();
        for (UserInfoBean.PhotoAlbumBean albumBean : photoAlbumBean){
            PhotoBean photoBean = new PhotoBean();
            photoBean.setBurnAfterReading(albumBean.getStatusFlag()==1);
            photoBean.setRedEnvelopePhotos(albumBean.getPayFlag()==1);
            photoBean.setPhotoUrl(albumBean.getUrl());
            photoBean.setFee(albumBean.getFee()+"");
            photoBean.setOneself(albumBean.getLabelFlag()>0);
            list.add(photoBean);
        }
        photoList.clear();
        for (int i = 0; list.size() > 8 ? i < 8 : i < list.size(); i ++){
            photoList.add(list.get(i));
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
                    Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
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
                    Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
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
                    if (photoBean.isRedEnvelopePhotosPaid()){
                        Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                    }else {
                        Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                    }

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
                        intent.putExtra("isShowButton",false);
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
                        intent.putExtra("isShowButton",false);
                        startActivityForResult(intent, 10);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 加载页面详情信息
     * */
    private void loadDetails() {
        addLike = userDetailsBean.getEnjoyFlag();
        addBlacklist = userDetailsBean.getBlacklistFlag();
        tvAddLike.setText(addLike==1?"取消喜欢":"加入喜欢");
        tvAddLike.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(addLike==1?R.mipmap.yes_like_logo:R.mipmap.no_like_logo), null);

        Glide.with(mActivity).load(userDetailsBean.getHeadUrl()).placeholder(R.mipmap.default_head_logo).error(R.mipmap.default_head_logo).into(imgHeader);
        tvName.setText(userDetailsBean.getName());
        tvPlace.setText(userDetailsBean.getRegion());
        tvAge.setText(ZodiacUtil.date2Constellation(userDetailsBean.getBirthday()) + "-" + TimeUtils.getAgeFromBirthTime(userDetailsBean.getBirthday()) + "岁");
        CityBean occupationBean;
        String job = "";
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
        tvOccupation.setVisibility(StringUtil.isEmpty(job) ? View.GONE : View.VISIBLE);
        String onCity = "";
        CityBean cityBean;
        for (int i = 0; i < CITYBEANLIST.size(); i++) {
            cityBean = CITYBEANLIST.get(i);
            for (int j = 0; j < cityBean.getChild().size(); j++) {
                List<CityBean.ChildBeanX> city = CITYBEANLIST.get(i).getChild();
                if (userDetailsBean.getCities().contains(city.get(j).getValue())) {
                    onCity = onCity + city.get(j).getText() + "/";
                }
            }
        }
        if (onCity.contains("/")) {
            onCity=onCity.substring(0, onCity.length() - 1);
        }
        tvOnCity.setText(onCity);
        tvOnCity.setVisibility(StringUtil.isEmpty(onCity) ? View.GONE : View.VISIBLE);
        tvAdoptZhenRen.setVisibility(userDetailsBean.getCertification() == 1 ? View.VISIBLE : View.GONE);
        tvAdoptNvShen.setVisibility(userDetailsBean.getGender() == 2 ? userDetailsBean.getLabeltype() == 1 ? View.VISIBLE : View.GONE : View.GONE);
        tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, userDetailsBean.getGender() == 1 ? userDetailsBean.getVipMember() == 0 ? getResources().getDrawable(R.mipmap.vip_huangguan_logo) : null : null, null);
        tvDistance.setText(userDetailsBean.getDistance() + "m");
        tvHomeOnline.setVisibility(userDetailsBean.getOnline()==1 ? View.VISIBLE : View.GONE);
        tvHomePaidAlbum.setVisibility(configBean.getPrivacystate() == 2 ? View.VISIBLE : View.GONE);
        tvHeight.setText(userDetailsBean.getHeight());
        tvWeight.setText(userDetailsBean.getWeight());
        tvProgram.setText(configBean.getDatingPrograms());
        tvExpect.setText(configBean.getDesiredGoals());
        tvIntroduce.setText(userDetailsBean.getDescription());
        viewHeight.setVisibility(StringUtil.isEmpty(userDetailsBean.getHeight()) ? View.GONE : View.VISIBLE);
        rlHeight.setVisibility(StringUtil.isEmpty(userDetailsBean.getHeight()) ? View.GONE : View.VISIBLE);
        viewWeight.setVisibility(StringUtil.isEmpty(userDetailsBean.getWeight()) ? View.GONE : View.VISIBLE);
        rlWeight.setVisibility(StringUtil.isEmpty(userDetailsBean.getWeight()) ? View.GONE : View.VISIBLE);
        viewProgram.setVisibility(StringUtil.isEmpty(configBean.getDatingPrograms()) ? View.GONE : View.VISIBLE);
        rlProgram.setVisibility(StringUtil.isEmpty(configBean.getDatingPrograms()) ? View.GONE : View.VISIBLE);
        viewExpect.setVisibility(StringUtil.isEmpty(configBean.getDesiredGoals()) ? View.GONE : View.VISIBLE);
        rlExpect.setVisibility(StringUtil.isEmpty(configBean.getDesiredGoals()) ? View.GONE : View.VISIBLE);
        rlIntroduce.setVisibility(StringUtil.isEmpty(userDetailsBean.getDescription()) ? View.GONE : View.VISIBLE);
        rlQQ.setVisibility(StringUtil.isEmpty(contactInfoBean.getQq()) ? View.GONE : View.VISIBLE);
        rlWeChat.setVisibility(StringUtil.isEmpty(contactInfoBean.getWeChat()) ? View.GONE : View.VISIBLE);
        viewQq.setVisibility(StringUtil.isEmpty(contactInfoBean.getQq()) ? View.GONE : View.VISIBLE);
        viewWeChat.setVisibility(StringUtil.isEmpty(contactInfoBean.getWeChat()) ? View.GONE : View.VISIBLE);
        String socialContact = "";
        if (contactInfoBean.getHidecontactinfo() == 1){
            //隐藏社交账号
            socialContact = userDetailsBean.getGender() == 1 ? "已隐藏(直接私聊他)":"已隐藏(直接私聊她)";
        }else {
            //显示社交账号
            socialContact = "已填写，点击查看";
        }
        tvQQ.setText(socialContact);
        tvWechat.setText(socialContact);
        if (null == photoAlbumBean || photoAlbumBean.size() <= 0){
            tvNoPhoto.setVisibility(View.VISIBLE);
            llAlbumlock.setVisibility(View.GONE);
        }else {
            tvNoPhoto.setVisibility(View.GONE);
            llAlbumlock.setVisibility(configBean.getPrivacystate()==2 ? View.VISIBLE : View.GONE);
            int payNum = 0;
            for (UserInfoBean.PhotoAlbumBean albumBean : photoAlbumBean){
                if (albumBean.getPayFlag() == 1){
                    payNum++;
                }
            }
            tvAlbumlock.setText("有"+photoAlbumBean.size()+"张照片，其中"+payNum+"张为红包照片");
            if (configBean.getCurrencyType() == 2){
                //现金
                albumMoney = configBean.getViewphotofee()+"";
            }else {
                //虚拟币
                try {
                    albumMoney = NumberUtil.div_Intercept("20",configBean.getViewphotofee()+"",1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (userDetailsBean.getGender() == 1){
                tvUnlock.setText("解锁他的相册(" + albumMoney + "元),会员免费");
                tvSetalbumlock.setText("他设置了相册锁");
            }else {
                tvUnlock.setText("解锁她的相册(" + albumMoney + "元),会员免费");
                tvSetalbumlock.setText("她设置了相册锁");
            }
        }
    }

    @OnClick({R.id.tv_Unlock, R.id.ll_evaluate, R.id.ll_chat, R.id.ll_SocialContact, R.id.tv_addLike, R.id.img_header, R.id.tv_ApplySee, R.id.rl_dynamic, R.id.tv_wechat, R.id.tv_QQ})
    public void onViewClicked(View view) {
        String title = userDetailsBean.getGender() == 1 ? "是否与他私聊，同时会解锁他的全部资料哦":"是否与她私聊，同时会解锁她的全部资料哦";
        switch (view.getId()) {
            case R.id.ll_evaluate:
                //评价
                showEvaluateDialog();
                break;
            case R.id.ll_chat:
                //私聊
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity, title, "10", new ChatCautionDialog.PaySeeOnClickListener() {
                            @Override
                            public void onClick(String money) {
                                //付费查看
                                showPayMode(view,money);
                            }
                        }))
                        .show();
                break;
            case R.id.ll_SocialContact:
                //社交
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity,title,"10", new ChatCautionDialog.PaySeeOnClickListener() {
                            @Override
                            public void onClick(String money) {
                                //付费查看
                                showPayMode(view,money);
                            }
                        }))
                        .show();
                break;
            case R.id.tv_addLike:
                //添加喜欢
                addLike = addLike == 1? 2:1;
                operatorEnjoy();
                break;
            case R.id.img_header:
                //头像
                break;
            case R.id.tv_ApplySee:
                //立即查看
                break;
            case R.id.rl_dynamic:
                //动态
                DynamicActivity.start(mActivity);
                break;
            case R.id.tv_wechat:
                //微信
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity,title,"10", new ChatCautionDialog.PaySeeOnClickListener() {
                            @Override
                            public void onClick(String money) {
                                //付费查看
                                showPayMode(view,money);
                            }
                        }))
                        .show();
                break;
            case R.id.tv_QQ:
                //QQ
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity,title,"10", new ChatCautionDialog.PaySeeOnClickListener() {
                            @Override
                            public void onClick(String money) {
                                //付费查看
                                showPayMode(view,money);
                            }
                        }))
                        .show();
                break;
            case R.id.tv_Unlock:
                //相册解锁
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity,title,albumMoney, new ChatCautionDialog.PaySeeOnClickListener() {
                            @Override
                            public void onClick(String money) {
                                //付费查看
                                showPayMode(view,money);
                            }
                        }))
                        .show();
                break;
        }
    }

    /**
     * 添加移除收藏
     * */
    private void operatorEnjoy() {
        showProgress(false);
        UserApi.operatorEnjoy(accid, addLike, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    tvAddLike.setText(addLike==1?"取消喜欢":"加入喜欢");
                    tvAddLike.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(addLike==1?R.mipmap.yes_like_logo:R.mipmap.no_like_logo), null);
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
                                    String estimateData = "";
                                    StringBuilder builder = new StringBuilder();
                                    builder.append("[");
                                    for (String id : ids){
                                        if (StringUtil.isNotEmpty(id)){
                                            builder.append(id);
                                            builder.append(",");
                                        }
                                    }
                                    if (builder.toString().contains(",")){
                                        estimateData = builder.toString().substring(0,builder.toString().length()-1) + "]";
                                    }
                                    evalualeUser(estimateData);
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

    /**
     * 评价用户
     * */
    private void evalualeUser(String estimateData) {
        showProgress(false);
        UserApi.evalualeUser(accid, estimateData, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                toast((String) object);
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    /**
     * 显示支付方式弹窗
     * */
    private void showPayMode(View v,String amount) {
        final PaySelect paySelect = new PaySelect(mActivity,amount,"会员套餐",amount,2);
        new XPopup.Builder(mActivity)
                .atView(v)
                .asCustom(paySelect)
                .show();
        paySelect.setOnClickListenerOnSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即支付
                PaySelect.SelectPayType type = paySelect.getCurrSeletPayType();
                int payType = 1;
                switch (type) {
                    case ALI:
                        //支付宝支付
                        payType = 3;
                        break;
                    case WCHAT:
                        //微信支付
                        payType = 2;
                        break;
                    case WALLET:
                        //钱包支付
                        payType = 1;
                        break;
                }
                if (!NoDoubleClickUtils.isDoubleClick(2000)){
//                    getRedPageId(amount,payType);
                    toast(payType == 3 ? "支付宝支付" : "微信支付");
                    paySelect.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) return;
        switch (requestCode) {
            case 10:
            case 20:
                list = (List<PhotoBean>) data.getSerializableExtra("photoList");
                photoList.clear();
                for (int i = 0; list.size() > 8 ? i < 8 : i < list.size(); i ++){
                    photoList.add(list.get(i));
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
