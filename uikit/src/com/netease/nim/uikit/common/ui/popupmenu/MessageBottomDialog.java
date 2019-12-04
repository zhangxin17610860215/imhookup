package com.netease.nim.uikit.common.ui.popupmenu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ModuleUIComFn;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;

/**
 * 底部弹窗(关闭消息,拉黑,举报,取消)
 * */
public class MessageBottomDialog extends BottomPopupView implements View.OnClickListener {

    private Activity mActivity;
    private TextView tv_CloseMsgRemind;
    private TextView tv_PullBlack;
    private TextView tv_Report;
    private TextView tv_cancel;

    private String account = "";        //对方的account
    private boolean black;
    private boolean notice;

    public MessageBottomDialog(@NonNull Activity activity, String account) {
        super(activity);
        this.mActivity = activity;
        this.account = account;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_messagebottom_layout;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        // 实现一些UI的初始和逻辑处理
        initView();
        initData();
    }

    private void initView() {
        tv_CloseMsgRemind = findViewById(R.id.tv_CloseMsgRemind);
        tv_PullBlack = findViewById(R.id.tv_PullBlack);
        tv_Report = findViewById(R.id.tv_Report);
        tv_cancel = findViewById(R.id.tv_cancel);

        tv_CloseMsgRemind.setOnClickListener(this);
        tv_PullBlack.setOnClickListener(this);
        tv_Report.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    private void initData() {
        black = NIMClient.getService(FriendService.class).isInBlackList(account);
        notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);

        tv_PullBlack.setText(black ? "取消拉黑" : "拉黑");
        tv_CloseMsgRemind.setText(notice ? "关闭消息提醒" : "开启消息提醒");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_CloseMsgRemind){
            //关闭消息提示
            setMessageNotify();
            dismiss();
        }else if (v.getId() == R.id.tv_PullBlack){
            //拉黑
            setBlack();
            dismiss();
        }else if (v.getId() == R.id.tv_Report){
            //举报
            ModuleUIComFn.getInstance().toReportActivityClick(mActivity);
            dismiss();
        }else if (v.getId() == R.id.tv_cancel){
            //取消
            dismiss();
        }
    }

    /**
     * 设置拉黑
     * */
    private void setBlack() {
        if (black) {
            NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    ToastHelper.showToast(mActivity, "加入黑名单成功");
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        ToastHelper.showToast(mActivity, R.string.network_is_not_available);
                    } else {
                        ToastHelper.showToast(mActivity, "on failed：" + code);
                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        } else {
            NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    ToastHelper.showToast(mActivity, "移除黑名单成功");
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        ToastHelper.showToast(mActivity, R.string.network_is_not_available);
                    } else {
                        ToastHelper.showToast(mActivity, "on failed:" + code);
                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }

    /**
     * 设置消息提醒
     * */
    private void setMessageNotify() {
        NIMClient.getService(FriendService.class).setMessageNotify(account, black).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                if (notice) {
                    ToastHelper.showToast(mActivity, "开启消息提醒成功");
                } else {
                    ToastHelper.showToast(mActivity, "关闭消息提醒成功");
                }
            }

            @Override
            public void onFailed(int code) {
                if (code == 408) {
                    ToastHelper.showToast(mActivity, R.string.network_is_not_available);
                } else {
                    ToastHelper.showToast(mActivity, "on failed:" + code);
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }
}
