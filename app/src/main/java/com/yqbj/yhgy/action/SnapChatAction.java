package com.yqbj.yhgy.action;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.impl.cache.ConfigConstants;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public class SnapChatAction extends PickImageAction {

    public SnapChatAction() {
        super(R.drawable.message_plus_snapchat_selector, R.string.input_panel_snapchat, false);
    }

    @Override
    protected void onPicked(File file) {
        SnapChatAttachment snapChatAttachment = new SnapChatAttachment();
        snapChatAttachment.setPath(file.getPath());
        snapChatAttachment.setSize(file.length());
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        config.enableRoaming = false;
        config.enableSelfSync = false;
        IMMessage stickerMessage = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "阅后即焚消息", snapChatAttachment, config);
        Map<String, Object> remoteExtension = new HashMap<>();
        remoteExtension.put(ConfigConstants.MESSAGE_ATTRIBUTE.ownWhetherSee,false);
        stickerMessage.setLocalExtension(remoteExtension);
        sendMessage(stickerMessage);
    }

}
