package com.yuanqi.hangzhou.imhookup.main;

import android.content.Context;

import com.netease.nimlib.sdk.msg.model.IMMessage;

public class ModuleUIComFn {

    private static ModuleUIComFn instance;

    public ToGroupDetails ToGroupDetailsInstance;
    public ToPersonChatMsg toPersonChatMsgInstance;
    public ToGlobalSearch toGlobalSearchInstance;
    public GetTeamConfig getTeamConfig;
    public AitMessagePassing aitMessagePassing;
    public StartRobotChat startRobotChat;

    public static ModuleUIComFn getInstance() {
        if (instance == null) {
            instance = new ModuleUIComFn();
        }
        return instance;
    }

    public void toGroupDetailsClick(Context context, String var1) {
        if (ToGroupDetailsInstance != null) {
            ToGroupDetailsInstance.toGroupDetailsClick(context, var1);
        }
    }

    public void toPersonChatMsgClick(Context context, String var1) {
        if (toPersonChatMsgInstance != null) {
            toPersonChatMsgInstance.toPersonChatMsgClick(context, var1);
        }
    }

    public void toGlobalSearchClick(Context context) {
        if (toGlobalSearchInstance != null) {
            toGlobalSearchInstance.toGlobalSearch(context);
        }
    }

    public void getTeamConfig(Context context, String var1, GetTeamConfigCallback callback) {
        if (getTeamConfig != null) {
            getTeamConfig.getTeamConfig(context, var1, callback);
        }
    }

    public void aitMessagePassing(Context context, IMMessage message) {
        if (aitMessagePassing != null) {
            aitMessagePassing.aitMessagePassing(context, message);
        }
    }

    public void startRobotChat(Context context, String id, IMMessage anchor) {
        if (startRobotChat != null) {
            startRobotChat.startRobotChat(context, id, anchor);
        }
    }

    public interface ToGroupDetails {
        void toGroupDetailsClick(Context context, String var1);
    }

    public interface ToPersonChatMsg {
        void toPersonChatMsgClick(Context context, String var1);
    }

    public interface ToGlobalSearch {
        void toGlobalSearch(Context context);
    }

    public interface GetTeamConfig {
        void getTeamConfig(Context context, String var1, GetTeamConfigCallback callback);
    }

    public interface GetTeamConfigCallback {
        void onSuccess(int code, Object object);
        void onFailed(String errMessage);
    }

    public interface AitMessagePassing {
        void aitMessagePassing (Context context, IMMessage message);
    }

    public interface StartRobotChat {
        void startRobotChat (Context context, String id, IMMessage anchor);
    }

}
