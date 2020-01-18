package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePicker;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.ui.ImageGridActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.UpLoadPhotoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.main.SeePictureActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.StringUtil;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 女神认证
 */
public class GoddessCertificationActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_Tips)
    TextView tvTips;
    @BindView(R.id.tv_Submission)
    TextView tvSubmission;
    @BindView(R.id.tv_OK)
    TextView tvOK;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private EasyRVAdapter mAdapter;
    private Activity mActivity;
    private int selectorNumber = 4;
    private int sendImageNum = 0;
    private List<UpLoadPhotoBean> list = new ArrayList<>();

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
    }

    private void initData() {
        UpLoadPhotoBean photoBean = new UpLoadPhotoBean();
        photoBean.setStatusFlag(0);
        photoBean.setUrl("add");
        photoBean.setRedPacketFee(0);
        photoBean.setType(1);
        list.add(photoBean);
        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.dynamic_grid_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                UpLoadPhotoBean bean = list.get(position);
                RoundedImageView imageView = viewHolder.getView(R.id.img_dynamic);
                ImageView imgDeletePictures = viewHolder.getView(R.id.img_deletePictures);

                if (StringUtil.isNotEmpty(bean.getUrl()) && bean.getUrl().equals("add")){
                    imageView.setImageResource(R.mipmap.add_img_icon);
                    imgDeletePictures.setVisibility(View.GONE);
                }else {
                    Glide.with(mActivity).load(bean.getUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imageView);
                    imgDeletePictures.setVisibility(View.VISIBLE);
                }
                imgDeletePictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isShowAdd = false;
                        list.remove(bean);

                        for (UpLoadPhotoBean loadPhotoBean : list){
                            isShowAdd = loadPhotoBean.getUrl().equals("add");
                        }
                        if (isShowAdd){
                            list.add(bean);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getUrl().equals("add")){
                            //添加图片
                            showSelector(R.string.input_panel_photo, 100, true, selectorNumber - (list.size() - 1));
                        }else {
                            //查看图片
//                            SeePictureActivity.start(mActivity,position,list,"123123");
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.tv_Submission, R.id.tv_OK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_Submission:
                //提交
                if (null == list || list.size() <= 0){
                    toast("请先上传图片");
                    return;
                }
                goddessPass();
                break;
            case R.id.tv_OK:
                //好的
                finish();
                break;
        }
    }

    /**
     * 女神认证
     * */
    private void goddessPass() {
        showProgress(false);
        for (UpLoadPhotoBean bean : list){
            if (bean.getUrl().equals("add")){
                list.remove(bean);
            }
        }
        if (null == list || list.size() <= 0){
            toast("请先上传图片");
            return;
        }
        String multimediaeInfo = JSON.toJSONString(list);
        UserApi.goddessPass(multimediaeInfo, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    tvTitle.setText("正在审核");
                    tvTips.setText("已提交");
                    mRecyclerView.setVisibility(View.GONE);
                    tvSubmission.setVisibility(View.GONE);
                    tvOK.setVisibility(View.VISIBLE);
                }else {
                    toast((String) object);
                }
            }

            @Override
            public void onFailed(String errMessage) {
                dismissProgress();
                toast(errMessage);
            }
        });
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, int requestCode, boolean multiSelect, int number) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(number);
        option.setSaveRectangle(true);
//        ImagePickerLauncher.selectImage(mActivity, requestCode, option, titleId);
        ImagePicker.getInstance().setOption(option);
        Intent intent = new Intent(mActivity, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
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
        sendImageNum = 0;
        SendImageHelper.sendImageAfterSelfImagePicker(mActivity, data, new SendImageHelper.Callback() {
            @Override
            public void sendImage(File file, boolean isOrig, int imgListSize) {
                fileToUrl(file,imgListSize);
            }
        });
    }

    /**
     * 将file转换成URL
     * */
    private void fileToUrl(File file,int imgListSize) {
        if (file == null) {
            return;
        }
        NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG).setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int i, final String url, Throwable throwable) {
                if (i != ResponseCode.RES_SUCCESS){
                    toast("图片上传失败，请稍后重试");
                    return;
                }
                sendImageNum++;
                UpLoadPhotoBean bean = new UpLoadPhotoBean();
                bean.setType(1);
                bean.setRedPacketFee(0);
                bean.setStatusFlag(0);
                bean.setUrl(url);
                list.add(list.size() - 1,bean);
                if (list.size() > 4){
                    list.remove(list.size() -1);
                }
                if (sendImageNum == imgListSize){
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}
