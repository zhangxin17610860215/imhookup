package com.yqbj.yhgy.action;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yqbj.yhgy.attachment.CurrencyRedPacketAttachment;
import com.yqbj.yhgy.message.CurrencyRedPackageActivity;

import java.util.Map;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public class CurrencyRedPackageAction extends BaseAction {

    private static final int CREATE_SINGLE_RED_PACKET = 10;

    public CurrencyRedPackageAction() {
        super(R.drawable.nim_message_plus_location_normalcurrencyredpackage, R.string.input_panel_currencyredpackage);
    }

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
        CurrencyRedPackageActivity.start(getActivity(),requestCode,getAccount());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            String redId = data.getStringExtra("redId");
            String redTitle = data.getStringExtra("redTitle");
            String redContent = data.getStringExtra("redContent");
            Map<String,Object> map = (Map<String, Object>) data.getSerializableExtra("redData");
            CurrencyRedPacketAttachment attachment = new CurrencyRedPacketAttachment();
            // 红包id，红包信息，红包名称
            attachment.setRpId(redId);
            attachment.setRpContent(redContent);
            attachment.setRpTitle(redTitle);
            // 不存云消息历史记录
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableHistory = true;
            IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "发来了一个红包", attachment, config);
            message.setRemoteExtension(map);
            sendMessage(message);
        }
    }
}
