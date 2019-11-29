package com.yuanqi.hangzhou.imhookup.action;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.common.ToastHelper;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public class RedPackageAction extends BaseAction {

    public RedPackageAction() {
        super(R.drawable.nim_message_plus_location_normalredpackage, R.string.input_panel_redpackage);
    }

    @Override
    public void onClick() {
        ToastHelper.showToast(getActivity(),"红包");
    }
}
