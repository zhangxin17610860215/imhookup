package com.yqbj.yhgy.viewholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.attachment.RedPacketAttachment;
import com.yqbj.yhgy.message.CashRedPackageDetailsActivity;
import com.yqbj.yhgy.message.OthersCashRPDetailsActivity;

public class MsgViewHolderRedPacket extends MsgViewHolderBase {

    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;    // 红包描述
    private TextView sendTitleText, revTitleText;    // 红包名称

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.red_packet_item;
    }

    @Override
    protected void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_name_send);
        sendView = findViewById(R.id.bri_send);
        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_name_rev);
        revView = findViewById(R.id.bri_rev);
    }

    @Override
    protected void bindContentView() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
//        if (!isReceivedMessage() && message.getStatus() != MsgStatusEnum.read) {
//            revView.setBackgroundResource(R.drawable.red_packet_rev_normal);
//        } else {
//            revView.setBackgroundResource(R.drawable.red_packet_rev_press);
//        }

        String isGray = "No";

        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendContentText.setText(attachment.getRpContent());
            sendTitleText.setText(attachment.getRpTitle());
            if (isGray.equals("Yes")){
                sendView.setBackgroundResource(R.mipmap.red_packet_send_bg);
            }else {
                sendView.setBackgroundResource(R.mipmap.red_packet_send_bg);
            }
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revContentText.setText(attachment.getRpContent());
            revTitleText.setText(attachment.getRpTitle());
            if (isGray.equals("Yes")){
                revView.setBackgroundResource(R.mipmap.red_packet_rev_bg);
            }else {
                revView.setBackgroundResource(R.mipmap.red_packet_rev_bg);
            }
        }
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    protected void onItemClick() {
        // 拆红包
        if (!isReceivedMessage()){
            //自己发送的
            sendView.setBackgroundResource(R.mipmap.red_packet_send_bg);
            //查看自己发出去的红包页面
            CashRedPackageDetailsActivity.start(context);
        }else {
            //别人发送的
            revView.setBackgroundResource(R.mipmap.red_packet_rev_bg);
            //查看别人发出去的红包页面
            OthersCashRPDetailsActivity.start(context);
        }

    }
}
