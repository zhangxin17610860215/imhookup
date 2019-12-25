package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePicker;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.ui.ImageGridActivity;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.utils.ImageFilter;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的相册
 */
public class MyPhotoActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private Activity mActivity;
    private List<PhotoBean> photoList = new ArrayList<>();
    private List<PhotoBean> sendImageList = new ArrayList<>();
    private EasyRVAdapter mAdapter;
    private int sendImageNum = 0;

    public static void start(Context context, List<PhotoBean> photoList) {
        Intent intent = new Intent();
        intent.setClass(context, MyPhotoActivity.class);
        intent.putExtra("photoList", (Serializable) photoList);
        ((Activity) context).startActivityForResult(intent, 20);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myphoto_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        photoList = (List<PhotoBean>) getIntent().getSerializableExtra("photoList");
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
    }

    private void initData() {
        mAdapter = new EasyRVAdapter(mActivity,photoList,R.layout.item_mephoto_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                PhotoBean photoBean = photoList.get(position);
                RoundedImageView imgHead = viewHolder.getView(R.id.img_head);
                RelativeLayout rlBurnAfterReading = viewHolder.getView(R.id.rl_BurnAfterReading);
                RelativeLayout rlRedEnvelopePhotos = viewHolder.getView(R.id.rl_RedEnvelopePhotos);
                TextView tvRedEnvelopePhotos = viewHolder.getView(R.id.tv_RedEnvelopePhotos);

                if (photoBean.isBurnAfterReading()){
                    //拿到初始图
                    Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
                    //处理得到模糊效果的图
                    Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
                    Glide.with(mActivity).load(blurBitmap).into(imgHead);
                }else {
                    Glide.with(mActivity).load(photoBean.getPhotoUrl()).into(imgHead);
                }

                if (photoBean.isRedEnvelopePhotos() && photoBean.isBurnAfterReading()){
                    //阅后即焚的红包照片
                    tvRedEnvelopePhotos.setText("阅后即焚的红包照片");
                    //拿到初始图
                    Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
                    //处理得到模糊效果的图
                    Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
                    Glide.with(mActivity).load(blurBitmap).into(imgHead);
                }else if (photoBean.isRedEnvelopePhotos()){
                    //只是红包照片
                    tvRedEnvelopePhotos.setText("红包照片");
                    //拿到初始图
                    Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
                    //处理得到模糊效果的图
                    Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
                    Glide.with(mActivity).load(blurBitmap).into(imgHead);
                }

                rlBurnAfterReading.setVisibility(photoBean.isBurnAfterReading() ? View.VISIBLE : View.GONE);
                rlRedEnvelopePhotos.setVisibility(photoBean.isRedEnvelopePhotos() ? View.VISIBLE : View.GONE);

                imgHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看删除该照片
                        Intent intent = new Intent();
                        intent.setClass(mActivity, LookPhotoActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("photoList", (Serializable) photoList);
                        intent.putExtra("accId",NimUIKit.getAccount());
                        intent.putExtra("type","2");
                        intent.putExtra("isShowButton",true);
                        mActivity.startActivityForResult(intent, 10);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.img_back, R.id.tv_UpPhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onFinish();
                break;
            case R.id.tv_UpPhoto:
                sendImageList.clear();
                showSelector(R.string.input_panel_photo, 100, true, 9);
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            photoList.clear();
            photoList.addAll((List<PhotoBean>) data.getSerializableExtra("photoList"));
            if (photoList.size() <= 0){
                onFinish();
            }else {
                mAdapter.notifyDataSetChanged();
            }
        }else if (requestCode == 100){
            onPickImageActivityResult(requestCode, data);
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
                sendImageNum++;
                PhotoBean photoBean = new PhotoBean();
                photoBean.setBurnAfterReading(false);
                photoBean.setPhotoUrl(file.getPath());
                sendImageList.add(photoBean);

                if (sendImageNum == imgListSize){
                    photoList.addAll(sendImageList);
                    Intent intent = new Intent();
                    intent.setClass(mActivity, LookPhotoActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("photoList", (Serializable) photoList);
                    intent.putExtra("accId",NimUIKit.getAccount());
                    intent.putExtra("type","1");
                    intent.putExtra("isShowButton",true);
                    startActivityForResult(intent, 10);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        onFinish();
    }

    private void onFinish(){
        Intent intent = new Intent();
        intent.putExtra("photoList", (Serializable) photoList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
