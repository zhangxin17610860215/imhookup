package com.yqbj.yhgy.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息设置
 */
public class MessageSettingActivity extends BaseActivity {

    @BindView(R.id.img_chatMessageNotice)
    ImageView imgChatMessageNotice;
    @BindView(R.id.img_BroadcastNotice)
    ImageView imgBroadcastNotice;
    @BindView(R.id.img_FabulousNotice)
    ImageView imgFabulousNotice;
    @BindView(R.id.img_commentNotice)
    ImageView imgCommentNotice;
    @BindView(R.id.img_ByRequestNotice)
    ImageView imgByRequestNotice;
    @BindView(R.id.img_ApplySuccessNotice)
    ImageView imgApplySuccessNotice;
    @BindView(R.id.img_voiceNotice)
    ImageView imgVoiceNotice;
    @BindView(R.id.img_shockNotice)
    ImageView imgShockNotice;

    private Activity mActivity;

    private boolean chatMessageNotice = false;      //私聊消息通知
    private boolean broadcastNotice = false;        //广播报名通知
    private boolean fabulousNotice = false;         //新点赞提醒
    private boolean commentNotice = false;          //新评论提醒
    private boolean byRequestNotice = false;        //用户通过了我的查看请求
    private boolean applySuccessNotice = false;     //邀请码申请成功
    private boolean voiceNotice = false;            //声音
    private boolean shockNotice = false;            //震动

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagesetting_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
    }

    private void initData() {

    }

    @OnClick({R.id.img_chatMessageNotice, R.id.img_BroadcastNotice, R.id.img_FabulousNotice, R.id.img_commentNotice, R.id.img_ByRequestNotice, R.id.img_ApplySuccessNotice, R.id.img_voiceNotice, R.id.img_shockNotice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_chatMessageNotice:
                //私聊消息通知
                toggleSwitch(1,chatMessageNotice,imgChatMessageNotice);
                break;
            case R.id.img_BroadcastNotice:
                //广播报名通知
                toggleSwitch(2,broadcastNotice,imgBroadcastNotice);
                break;
            case R.id.img_FabulousNotice:
                //新点赞提醒
                toggleSwitch(3,fabulousNotice,imgFabulousNotice);
                break;
            case R.id.img_commentNotice:
                //新评论提醒
                toggleSwitch(4,commentNotice,imgCommentNotice);
                break;
            case R.id.img_ByRequestNotice:
                //用户通过了我的查看请求
                toggleSwitch(5,byRequestNotice,imgByRequestNotice);
                break;
            case R.id.img_ApplySuccessNotice:
                //邀请码申请成功
                toggleSwitch(6,applySuccessNotice,imgApplySuccessNotice);
                break;
            case R.id.img_voiceNotice:
                //声音
                toggleSwitch(7,voiceNotice,imgVoiceNotice);
                break;
            case R.id.img_shockNotice:
                //震动
                toggleSwitch(8,shockNotice,imgShockNotice);
                break;
        }
    }

    /**
     * 切换开关
     * */
    private void toggleSwitch(int type, boolean b, ImageView imageView) {
        switch (type){
            case 1:
                chatMessageNotice = !b;
                break;
            case 2:
                broadcastNotice = !b;
                break;
            case 3:
                fabulousNotice = !b;
                break;
            case 4:
                commentNotice = !b;
                break;
            case 5:
                byRequestNotice = !b;
                break;
            case 6:
                applySuccessNotice = !b;
                break;
            case 7:
                voiceNotice = !b;
                break;
            case 8:
                shockNotice = !b;
                break;
        }
        imageView.setImageResource(b ? R.mipmap.isshow_open : R.mipmap.isshow_close);
    }
}
