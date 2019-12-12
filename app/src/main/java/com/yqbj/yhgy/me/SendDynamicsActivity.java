package com.yqbj.yhgy.me;

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

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
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
 * 发布动态
 */
public class SendDynamicsActivity extends BaseActivity {

    @BindView(R.id.et_Content)
    EditText etContent;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_NoComment)
    ImageView imgNoComment;
    @BindView(R.id.img_hide)
    ImageView imgHide;

    private Activity mActivity;
    private EasyRVAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private boolean noCommentNotice = false;            //禁止评论
    private boolean hideNotice = false;                 //隐藏
    private int sendImageNum = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, SendDynamicsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senddynamics_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initView() {
        setToolbar(mActivity, 0, "");
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
                HeadImageView imageView = viewHolder.getView(R.id.img_dynamic);
                if (StringUtil.isNotEmpty(list.get(position)) && list.get(position).equals("add")){
                    imageView.setImageResource(R.mipmap.add_img_icon);
                }else {
                    Glide.with(mActivity).load(list.get(position)).into(imageView);
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (list.get(position).equals("add")){
                            //添加图片
                            showSelector(R.string.input_panel_photo, RequestCode.PICK_IMAGE, true);
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
    private void showSelector(int titleId, int requestCode, boolean multiSelect) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(9);
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
        sendImageNum = 0;
        SendImageHelper.sendImageAfterSelfImagePicker(mActivity, data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig, int imgListSize) {
                sendImageNum++;
                list.add(list.size() - 1,file.getPath());
                if (list.size() > 9){
                    list.remove(list.size() -1);
                }
                if (sendImageNum == imgListSize){
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.img_NoComment, R.id.img_hide, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_NoComment:
                //是否禁止评论
                toggleSwitch(1,noCommentNotice,imgNoComment);
                break;
            case R.id.img_hide:
                //是否对同性别用户隐藏
                toggleSwitch(2,hideNotice,imgHide);
                break;
            case R.id.tv_send:
                //发布
                break;
        }
    }

    /**
     * 切换开关
     * */
    private void toggleSwitch(int type, boolean b, ImageView imageView) {
        switch (type){
            case 1:
                noCommentNotice = !b;
                break;
            case 2:
                hideNotice = !b;
                break;
        }
        imageView.setImageResource(b ? R.mipmap.isshow_open : R.mipmap.isshow_close);
    }
}
