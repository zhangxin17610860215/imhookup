package com.yqbj.yhgy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.main.SeePictureActivity;
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
 * 匿名举报
 */
public class AnonymousReportActivity extends BaseActivity {

    @BindView(R.id.tv_Advertising)
    TextView tvAdvertising;
    @BindView(R.id.tv_Harass)
    TextView tvHarass;
    @BindView(R.id.tv_Photo)
    TextView tvPhoto;
    @BindView(R.id.tv_vulgar)
    TextView tvVulgar;
    @BindView(R.id.tv_Liar)
    TextView tvLiar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_describe)
    EditText etDescribe;

    private Activity mActivity;
    private int reason;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private int selectorNumber = 5;
    private int sendImageNum = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, AnonymousReportActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anonymousreport_activity_layout);
        ButterKnife.bind(this);
        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
        onInitRightSure(mActivity, "提交", new onToolBarRightTextListener() {
            @Override
            public void onRight(View view) {
                toast("提交");
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
    }

    private void initData() {
        list.add("add");
        mAdapter = new EasyRVAdapter(mActivity,list,R.layout.dynamic_grid_item_layout) {
            @Override
            protected void onBindData(EasyRVHolder viewHolder, final int position, Object item) {
                if (null == list || list.size() == 0) {
                    return;
                }
                RoundedImageView imageView = viewHolder.getView(R.id.img_dynamic);
                ImageView imgDeletePictures = viewHolder.getView(R.id.img_deletePictures);

                if (StringUtil.isNotEmpty(list.get(position)) && list.get(position).equals("add")){
                    imageView.setImageResource(R.mipmap.add_img_icon);
                    imgDeletePictures.setVisibility(View.GONE);
                }else {
                    Glide.with(mActivity).load(list.get(position)).into(imageView);
                    imgDeletePictures.setVisibility(View.VISIBLE);
                }
                imgDeletePictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isShowAdd = false;
                        list.remove(position);

                        for (String url : list){
                            isShowAdd = url.equals("add") ? false : true;
                        }
                        if (isShowAdd){
                            list.add("add");
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).equals("add")){
                            //添加图片
                            showSelector(R.string.input_panel_photo, 100, true, selectorNumber - (list.size() - 1));
                        }else {
                            //查看图片
                            SeePictureActivity.start(mActivity,position,list,"123123");
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, int requestCode, boolean multiSelect, int number) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(number);
        option.setSaveRectangle(true);
        ImagePickerLauncher.selectImage(mActivity, requestCode, option, titleId);
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
            public void sendImage(File file, boolean isOrig, int imageListSize) {
                sendImageNum++;
                list.add(list.size() - 1,file.getPath());
                if (list.size() > 5){
                    list.remove(list.size() -1);
                }
                if (sendImageNum == imageListSize){
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.tv_Advertising, R.id.tv_Harass, R.id.tv_Photo, R.id.tv_vulgar, R.id.tv_Liar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_Advertising:
                //发广告
                reason = 1;
                selectReason();
                break;
            case R.id.tv_Harass:
                //骚扰
                reason = 2;
                selectReason();
                break;
            case R.id.tv_Photo:
                //虚假照片
                reason = 3;
                selectReason();
                break;
            case R.id.tv_vulgar:
                //色情低俗
                reason = 4;
                selectReason();
                break;
            case R.id.tv_Liar:
                //她是骗子
                reason = 5;
                selectReason();
                break;
        }
    }

    /**
     * 选择举报原因
     * */
    private void selectReason() {
        tvAdvertising.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.unselected_logo), null);
        tvHarass.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.unselected_logo), null);
        tvPhoto.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.unselected_logo), null);
        tvVulgar.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.unselected_logo), null);
        tvLiar.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.unselected_logo), null);
        switch (reason){
            case 1:
                tvAdvertising.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.selected_logo), null);
                break;
            case 2:
                tvHarass.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.selected_logo), null);
                break;
            case 3:
                tvPhoto.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.selected_logo), null);
                break;
            case 4:
                tvVulgar.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.selected_logo), null);
                break;
            case 5:
                tvLiar.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.selected_logo), null);
                break;
        }
    }
}
