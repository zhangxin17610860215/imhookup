package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 女神认证
 */
public class GoddessCertificationActivity extends BaseActivity {

    @BindView(R.id.img_upPhotos)
    ImageView imgUpPhotos;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Tips)
    TextView tvTips;
    @BindView(R.id.tv_Submission)
    TextView tvSubmission;
    @BindView(R.id.tv_OK)
    TextView tvOK;

    private Activity mActivity;

    public static void start(Context context) {
        Intent intent = new Intent(context, GoddessCertificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goddesscertification_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        tvSubmission.setClickable(false);
    }

    private void initData() {

    }

    @OnClick({R.id.img_upPhotos, R.id.tv_Submission, R.id.tv_OK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_upPhotos:
                //上传图片
//                Intent intent = new Intent(mActivity, ImageGridActivity.class);
//                mActivity.startActivityForResult(intent, RequestCode.PICK_IMAGE);
                ImagePickerLauncher.pickImage(mActivity, RequestCode.PICK_IMAGE, R.string.input_panel_photo);
//                showSelector(R.string.input_panel_photo, RequestCode.PICK_IMAGE, true);
                break;
            case R.id.tv_Submission:
                //提交
                tvTitle.setText("正在审核");
                tvTips.setText("已提交");
                imgUpPhotos.setVisibility(View.GONE);
                tvSubmission.setVisibility(View.GONE);
                tvOK.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_OK:
                //好的
                finish();
                break;
        }
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, int requestCode, boolean multiSelect) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(1);
        ImagePickerLauncher.selectImage(mActivity, requestCode, option, titleId);
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
            public void sendImage(File file, boolean isOrig, int imgListSize) {
                Glide.with(mActivity).load(file.getPath()).into(imgUpPhotos);
                imgUpPhotos.setClickable(false);
                tvSubmission.setClickable(true);
            }
        });
    }
}
