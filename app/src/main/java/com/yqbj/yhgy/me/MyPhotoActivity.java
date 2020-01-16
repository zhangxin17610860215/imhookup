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

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePicker;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.ui.ImageGridActivity;
import com.netease.nim.uikit.common.ui.widget.BlurTransformation;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.MyAlbumBean;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.bean.UpLoadPhotoBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.LogUtil;
import com.yqbj.yhgy.utils.StringUtil;
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
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_UpPhoto)
    TextView tv_UpPhoto;

    private Activity mActivity;
    private List<PhotoBean> photoList = new ArrayList<>();
    private List<PhotoBean> choiceList = new ArrayList<>();
    private EasyRVAdapter mAdapter;
    private int sendImageNum = 0;
    private String gender = "";
    private boolean isShowButton;       //是否显示底部阅后即焚和红包相册按钮(相当于是否是本人进来此页面)

    public static void start(Context context, List<PhotoBean> photoList,boolean isShowButton,String gender) {
        Intent intent = new Intent();
        intent.setClass(context, MyPhotoActivity.class);
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("isShowButton", isShowButton);
        intent.putExtra("Gender", gender);
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
        isShowButton = getIntent().getBooleanExtra("isShowButton",false);
        gender = getIntent().getStringExtra("Gender");
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
        tv_UpPhoto.setVisibility(isShowButton?View.VISIBLE:View.GONE);
        tv_title.setText(isShowButton?"我的相册":gender.equals("1")?"他的相册":"她的相册");
    }

    private void initData() {
        if (null != mAdapter){
            mAdapter.notifyDataSetChanged();
            return;
        }
        mAdapter = new EasyRVAdapter(mActivity,photoList,R.layout.item_mephoto_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                PhotoBean photoBean = photoList.get(position);
                RoundedImageView imgHead = viewHolder.getView(R.id.img_head);
                RelativeLayout rlBurnAfterReading = viewHolder.getView(R.id.rl_BurnAfterReading);
                RelativeLayout rlRedEnvelopePhotos = viewHolder.getView(R.id.rl_RedEnvelopePhotos);
                TextView tvRedEnvelopePhotos = viewHolder.getView(R.id.tv_RedEnvelopePhotos);
                TextView tvBurnedDown = viewHolder.getView(R.id.tv_BurnedDown);
                TextView tvIsBenRen = viewHolder.getView(R.id.tv_isBenRen);

                if (photoBean.isYellowish()){
                    Glide.with(mActivity).load(R.mipmap.shehuang_bg).into(imgHead);
                }else {
                    if (photoBean.isBurnAfterReading()){
                        //阅后即焚
                        if (!isShowButton){
                            Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                        }else {
                            Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                        }
                        if (photoBean.isBurnedDown()){
                            rlBurnAfterReading.setBackgroundResource(R.mipmap.burneddown_bg_logo);
                            tvBurnedDown.setText("已焚毁");
                            tvBurnedDown.setBackgroundResource(R.drawable.burneddown_bg_shape);
                        }else {
                            rlBurnAfterReading.setBackgroundResource(R.mipmap.burnafterreading_bg_logo);
                            tvBurnedDown.setText("阅后即焚");
                            tvBurnedDown.setBackgroundResource(R.mipmap.burnafterreading_logo);
                        }
                    }else {
                        Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                    }

                    if (photoBean.isRedEnvelopePhotos() && photoBean.isBurnAfterReading()){
                        //阅后即焚的红包照片
                        if (!isShowButton){
                            Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                        }else {
                            Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                        }
                        if (photoBean.isBurnedDown()){
                            rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redburneddown_bg_logo);
                            tvRedEnvelopePhotos.setText("已焚毁");
                            tvRedEnvelopePhotos.setBackgroundResource(R.drawable.burneddown_bg_shape);
                        }else {
                            rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redenvelopephotos_bg_logo);
                            tvRedEnvelopePhotos.setText(photoBean.isRedEnvelopePhotosPaid() ? "已付费" : "阅后即焚的红包照片");
                            tvRedEnvelopePhotos.setBackgroundResource(R.mipmap.burnafterreading_logo);
                        }
                    }else if (photoBean.isRedEnvelopePhotos()){
                        //只是红包照片
                        if (!isShowButton){
                            if (photoBean.isRedEnvelopePhotosPaid()){
                                Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                            }else {
                                Glide.with(mActivity).load(photoBean.getPhotoUrl()).optionalTransform(new BlurTransformation(mActivity, 25)).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                            }
                        }else {
                            Glide.with(mActivity).load(photoBean.getPhotoUrl()).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgHead);
                        }
                        if (photoBean.isBurnedDown()){
                            rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redburneddown_bg_logo);
                            tvRedEnvelopePhotos.setText("已焚毁");
                            tvRedEnvelopePhotos.setBackgroundResource(R.drawable.burneddown_bg_shape);
                        }else {
                            rlRedEnvelopePhotos.setBackgroundResource(R.mipmap.redenvelopephotos_bg_logo);
                            tvRedEnvelopePhotos.setText(photoBean.isRedEnvelopePhotosPaid() ? "已付费" : "红包照片");
                            tvRedEnvelopePhotos.setBackgroundResource(R.mipmap.burnafterreading_logo);
                        }
                    }

                    rlBurnAfterReading.setVisibility(photoBean.isBurnAfterReading() ? View.VISIBLE : View.GONE);
                    rlRedEnvelopePhotos.setVisibility(photoBean.isRedEnvelopePhotos() ? View.VISIBLE : View.GONE);
                    tvIsBenRen.setVisibility(photoBean.isOneself() ? View.VISIBLE : View.GONE);
                }
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
                        intent.putExtra("isShowButton",isShowButton);
                        intent.putExtra("isRequest",true);
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
                choiceList.clear();
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
//            photoList.clear();
//            photoList.addAll((List<PhotoBean>) data.getSerializableExtra("photoList"));
//            if (photoList.size() <= 0){
//                onFinish();
//            }else {
//                upLoadPhoto();
//            }
            choiceList = (List<PhotoBean>) data.getSerializableExtra("photoList");
            String type = data.getStringExtra("type");
            if (StringUtil.isEmpty(type)){
                return;
            }
            if (type.equals("1")){
                if (photoList.size() < 1 && choiceList.size() < 1){
                    onFinish();
                }
                if (choiceList.size() < 1){
                    return;
                }
                for (PhotoBean photoBean : photoList){
                    if (photoBean.getPhotoUrl().equals(choiceList.get(0).getPhotoUrl())){
                        return;
                    }
                }
                upLoadPhoto();
            }else if (type.equals("2")){
                getMyAlbum();
            }
        }else if (requestCode == 100){
            onPickImageActivityResult(requestCode, data);
        }
    }

    /**
     * 上传照片
     * */
    private void upLoadPhoto() {
        showProgress(false);
        List<UpLoadPhotoBean> upLoadPhotoBeans = new ArrayList<>();
        for (PhotoBean photoBean : choiceList){
            UpLoadPhotoBean bean = new UpLoadPhotoBean();
            bean.setType(1);
            bean.setRedPacketFee(photoBean.isRedEnvelopePhotos()?1:0);
            bean.setStatusFlag(photoBean.isBurnAfterReading()?1:0);
            bean.setUrl(photoBean.getPhotoUrl());
            upLoadPhotoBeans.add(bean);
        }
        String multimediaeInfo = JSON.toJSONString(upLoadPhotoBeans);
        UserApi.upLoadPhoto(multimediaeInfo, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    photoList.addAll(choiceList);
                    initData();
                    getMyAlbum();
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
     * 获取我的相册
     * */
    private void getMyAlbum() {
        showProgress(false);
        UserApi.getMyAlbum(mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    List<MyAlbumBean> albumBeanList = (List<MyAlbumBean>) object;
                    photoList.clear();
                    for (MyAlbumBean albumBean : albumBeanList){
                        PhotoBean photoBean = new PhotoBean();
                        photoBean.setId(albumBean.getId());
                        photoBean.setYellowish(albumBean.getCheckFlag() == 0);
                        photoBean.setBurnAfterReading(albumBean.getStatusFlag()==1);
                        photoBean.setRedEnvelopePhotos(albumBean.getPayFlag()==1);
                        photoBean.setPhotoUrl(albumBean.getUrl());
                        photoBean.setFee(albumBean.getFee()+"");
                        photoBean.setOneself(albumBean.getLabelFlag()>0);
                        photoList.add(photoBean);
                    }
                    initData();
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

    private void fileToUrl(File file, int imgListSize) {

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
                PhotoBean photoBean = new PhotoBean();
                photoBean.setBurnAfterReading(false);
                photoBean.setPhotoUrl(file.getPath());
                choiceList.add(photoBean);

                if (sendImageNum == imgListSize){
                    Intent intent = new Intent();
                    intent.setClass(mActivity, LookPhotoActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("photoList", (Serializable) choiceList);
                    intent.putExtra("accId",NimUIKit.getAccount());
                    intent.putExtra("type","1");
                    intent.putExtra("isShowButton",true);
                    intent.putExtra("isRequest",false);
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
