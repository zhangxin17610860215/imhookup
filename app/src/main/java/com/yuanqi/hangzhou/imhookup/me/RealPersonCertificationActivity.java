package com.yuanqi.hangzhou.imhookup.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 真人认证
 */
public class RealPersonCertificationActivity extends BaseActivity {

    @BindView(R.id.tv_stageOne)
    TextView tvStageOne;
    @BindView(R.id.tv_stageTwo)
    TextView tvStageTwo;
    @BindView(R.id.tv_stageThree)
    TextView tvStageThree;
    @BindView(R.id.img_upPhotos)
    ImageView imgUpPhotos;
    @BindView(R.id.ll_stageOne_describe)
    LinearLayout llStageOneDescribe;
    @BindView(R.id.ll_stageTwo_describe)
    LinearLayout llStageTwoDescribe;
    @BindView(R.id.ll_stageThree_describe)
    LinearLayout llStageThreeDescribe;
    @BindView(R.id.tv_Next)
    TextView tvNext;
    @BindView(R.id.rl_tongGuo)
    RelativeLayout rlTongGuo;

    private Activity mActivity;
    /**
     * 记录进度
     * */
    private int progress = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, RealPersonCertificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realpersoncertification_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        tvNext.setClickable(false);
    }

    private void initData() {

    }

    @OnClick({R.id.img_upPhotos, R.id.tv_Next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_upPhotos:
                ImagePickerLauncher.pickImage(mActivity, RequestCode.PICK_IMAGE, R.string.input_panel_photo);
                break;
            case R.id.tv_Next:
                if (progress < 4){
                    progress ++ ;
                }
                progressTag();
                break;
        }
    }

    private void progressTag() {
        tvStageOne.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        tvStageTwo.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        tvStageThree.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        llStageOneDescribe.setVisibility(View.GONE);
        llStageTwoDescribe.setVisibility(View.GONE);
        llStageThreeDescribe.setVisibility(View.GONE);
        imgUpPhotos.setVisibility(View.GONE);
        rlTongGuo.setVisibility(View.GONE);
        switch (progress){
            case 1:
                tvStageOne.setBackground(getResources().getDrawable(R.mipmap.shixin_yuan_logo));
                llStageOneDescribe.setVisibility(View.VISIBLE);
                imgUpPhotos.setVisibility(View.VISIBLE);
                tvNext.setText("下一步");
                break;
            case 2:
                tvStageTwo.setBackground(getResources().getDrawable(R.mipmap.shixin_yuan_logo));
                llStageTwoDescribe.setVisibility(View.VISIBLE);
                imgUpPhotos.setVisibility(View.VISIBLE);
                tvNext.setText("开始识别");
                break;
            case 3:
                tvStageThree.setBackground(getResources().getDrawable(R.mipmap.shixin_yuan_logo));
                llStageThreeDescribe.setVisibility(View.VISIBLE);
                imgUpPhotos.setVisibility(View.VISIBLE);
                tvNext.setText("完成");
                break;
            case 4:
                tvStageThree.setBackground(getResources().getDrawable(R.mipmap.shixin_yuan_logo));
                llStageThreeDescribe.setVisibility(View.VISIBLE);
                tvNext.setText("更新面容记录");
                imgUpPhotos.setVisibility(View.GONE);
                rlTongGuo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.PICK_IMAGE:
                onPickImageActivityResult(requestCode, data);
                break;
        }
    }

    /**
     * 图片选取回调
     */
    private void onPickImageActivityResult(int requestCode, Intent data) {
        if (data == null) {
            ToastHelper.showToastLong(mActivity, R.string.picker_image_error);
            return;
        }
        sendImageAfterSelfImagePicker(data);
    }

    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(mActivity, data, new SendImageHelper.Callback() {
            @Override
            public void sendImage(File file, boolean isOrig) {
                Glide.with(mActivity).load(file.getPath()).into(imgUpPhotos);
                imgUpPhotos.setClickable(false);
                tvNext.setClickable(true);
            }
        });
    }
}
