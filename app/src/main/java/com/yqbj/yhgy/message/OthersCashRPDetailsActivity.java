package com.yqbj.yhgy.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.attachment.CurrencyRedPacketAttachment;
import com.yqbj.yhgy.attachment.RedPacketAttachment;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.RedpacketDetailsBean;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看别人发出去的虚拟币红包
 */
public class OthersCashRPDetailsActivity extends BaseActivity {

    @BindView(R.id.img_header)
    RoundedImageView imgHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_redPackaheDetails)
    TextView tvRedPackaheDetails;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private Activity mActivity;
    private IMMessage message;
    private RedPacketAttachment attachment;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent(context, OthersCashRPDetailsActivity.class);
        intent.putExtra("message",message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherscashredpackagedetails_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        message = (IMMessage) getIntent().getSerializableExtra("message");
        initData();
    }

    private void initData() {
        attachment = (RedPacketAttachment) message.getAttachment();
        showProgress(false);
        UserApi.getRedPacketDetails(attachment.getRpId(), mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                RedpacketDetailsBean detailsBean = (RedpacketDetailsBean) object;
                loadData(detailsBean);
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    private void loadData(RedpacketDetailsBean detailsBean) {
        tvRedPackaheDetails.setText("“"+attachment.getRpContent()+"”");
        tvName.setText(detailsBean.getPayerName()+"的红包");
        tvMoney.setText(detailsBean.getAmount());
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(message.getSessionId());
        Glide.with(mActivity).load(userInfo.getAvatar()).placeholder(R.mipmap.default_home_head).error(R.mipmap.default_home_head).into(imgHeader);
    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
