package com.yuanqi.hangzhou.imhookup.main;

import android.content.Context;

import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.activity.NormalTeamInfoActivity;

public class MyNimUIkit extends NimUIKit {

    /**
     * 打开讨论组或高级群资料页
     *
     * @param context 上下文
     * @param teamId  群id
     */
    public static void startTeamInfo(Context context, String teamId) {

        Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (team == null) {
            return;
        }
        if (team.getType() == TeamTypeEnum.Advanced) {
//            AdvancedTeamInfoAct.start(context, teamId); // 启动固定群资料页
        } else if (team.getType() == TeamTypeEnum.Normal) {
            NormalTeamInfoActivity.start(context, teamId); // 启动讨论组资料页
        }


    }



}
