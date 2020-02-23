package com.yqbj.yhgy.main;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.yqbj.yhgy.action.SnapChatAttachment;
import com.yqbj.yhgy.attachment.CurrencyRedPacketAttachment;
import com.yqbj.yhgy.attachment.CurrencyRedPacketOpenedAttachment;
import com.yqbj.yhgy.attachment.DefaultCustomAttachment;
import com.yqbj.yhgy.attachment.RedPacketAttachment;
import com.yqbj.yhgy.attachment.RedPacketOpenedAttachment;
import com.yqbj.yhgy.attachment.StickerAttachment;
import com.yqbj.yhgy.utils.Base64;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type;
            JSONObject data;

            JSONObject msg = object.getJSONObject("msg");
            if(null != msg){
                type = msg.getInteger(KEY_TYPE);
                if (type == CustomAttachmentType.teamRobot){
                    String string = msg.getString(KEY_DATA).replaceAll(" " , "+");
                    byte[] decode = Base64.decode(string);
                    String dataStr = new String(decode);
//                    msg.put(KEY_DATA,dataStr);
                    data = msg.parseObject(dataStr);
                }else {
                    data = msg.getJSONObject(KEY_DATA);
                }
            }else{
                type = object.getInteger(KEY_TYPE);
                data = object.getJSONObject(KEY_DATA);
            }

            switch (type) {
//                case CustomAttachmentType.Guess:
//                    attachment = new GuessAttachment();
//                    break;
                case CustomAttachmentType.SnapChat:
                    return new SnapChatAttachment(data);
                case CustomAttachmentType.Sticker:
                    attachment = new StickerAttachment();
                    break;
//                case CustomAttachmentType.RTS:
//                    attachment = new RTSAttachment();
//                    break;
                case CustomAttachmentType.RedPacket:
                    attachment = new RedPacketAttachment();
                    break;
                case CustomAttachmentType.OpenedRedPacket:
                    attachment = new RedPacketOpenedAttachment();
                    break;
                case CustomAttachmentType.CurrencyRedPacket:
                    attachment = new CurrencyRedPacketAttachment();
                    break;
                case CustomAttachmentType.CurrencyOpenedRedPacket:
                    attachment = new CurrencyRedPacketOpenedAttachment();
                    break;
//                case CustomAttachmentType.SystemNotify:
//                    attachment = new SysNotifyAttachment();
//                    break;
//                case CustomAttachmentType.teamRobot:
//                    attachment = new TeamRobotNotifyAttachment();
//                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (null != attachment) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {
            Log.e("TAG",">>>>>>>>>>>>>>>>>>" + e.getMessage());
        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
