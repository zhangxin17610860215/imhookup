package com.yqbj.yhgy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.view.ChatCautionDialog;
import com.yqbj.yhgy.view.EvaluateDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    ImageView imgHeader;                        //头像
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

    private Activity mActivity;
    private int addLike = 0;                    //是否加入喜欢     0=不加入     1=加入

    public static void start(Context context) {
        Intent intent = new Intent(context, DetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        setRightImg(mActivity, R.mipmap.more_icon, new onToolBarRightImgListener() {
            @Override
            public void onRight(View view) {
                //更多
                new XPopup.Builder(mActivity).asBottomList(null,new String[]{"备注", "匿名举报"},
                        // null, /** 图标Id数组，可无 **/
                        // 1,    /** 选中的position，默认没有选中效果 **/
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click "+text);
                                if (position == 1){
                                    //匿名举报
                                    AnonymousReportActivity.start(mActivity);
                                }else {
                                    //备注
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void initData() {

    }

    List<String> data = new ArrayList<>();

    @OnClick({R.id.ll_evaluate, R.id.ll_chat, R.id.ll_SocialContact, R.id.tv_addLike, R.id.img_header, R.id.tv_ApplySee, R.id.rl_dynamic, R.id.tv_wechat, R.id.tv_QQ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_evaluate:
                //评价
                data.clear();
                data.add("友好");
                data.add("有趣");
                data.add("爽快");
                data.add("耐心");
                data.add("高冷");
                data.add("暴脾气");
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new EvaluateDialog(mActivity,data))
                        .show();
                break;
            case R.id.ll_chat:
                //私聊
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity))
                        .show();
                break;
            case R.id.ll_SocialContact:
                //社交
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity))
                        .show();
                break;
            case R.id.tv_addLike:
                //添加喜欢
                if (addLike == 0){
                    addLike = 1;
                    tvAddLike.setText("取消喜欢");
                    tvAddLike.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.yes_like_logo), null);
                }else {
                    addLike = 0;
                    tvAddLike.setText("加入喜欢");
                    tvAddLike.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.no_like_logo), null);
                }
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
                        .asCustom(new ChatCautionDialog(mActivity))
                        .show();
                break;
            case R.id.tv_QQ:
                //QQ
                new XPopup.Builder(mActivity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new ChatCautionDialog(mActivity))
                        .show();
                break;
        }
    }
}
