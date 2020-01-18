package com.yqbj.yhgy.me;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.security.rp.RPSDK;
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
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.UpLoadPhotoBean;
import com.yqbj.yhgy.bean.VfTokenBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.main.SeePictureActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    RoundedImageView imgUpPhotos;
    @BindView(R.id.ll_stageOne_describe)
    LinearLayout llStageOneDescribe;
    @BindView(R.id.ll_stageTwo_describe)
    LinearLayout llStageTwoDescribe;
    @BindView(R.id.ll_stageThree_describe)
    LinearLayout llStageThreeDescribe;
    @BindView(R.id.tv_Next)
    TextView tvNext;
    @BindView(R.id.tv_Tips)
    TextView tvTips;
    @BindView(R.id.rl_tongGuo)
    RelativeLayout rlTongGuo;

    private Activity mActivity;
    /**
     * 记录进度
     * */
    private int progress = 1;

    private String headerUrl = "";

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
    }

    private void initData() {
        String certification = Preferences.getCertification();
        if (certification.equals("1")){
            //通过真人认证
            progress = 4;
        }else {
            //未通过
            progress = 1;
        }
        progressTag();
    }

    @OnClick({R.id.img_upPhotos, R.id.tv_Next})
    public void onViewClicked(View view) {
        if (!NoDoubleClickUtils.isDoubleClick(500)) {
            switch (view.getId()) {
                case R.id.img_upPhotos:
                    if (StringUtil.isEmpty(headerUrl)) {
                        showSelector(R.string.input_panel_photo, RequestCode.PICK_IMAGE, true, 1);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(headerUrl);
                        SeePictureActivity.start(mActivity, 0, list, DemoCache.getAccount());
                    }
                    break;
                case R.id.tv_Next:
                    if (StringUtil.isEmpty(headerUrl)) {
                        toast("请先上传本人照片");
                        return;
                    }
                    if (progress == 2) {
                        getVfToken();
                        return;
                    } else {
                        if (progress < 4) {
                            progress++;
                        }
                        progressTag();
                    }
                    break;
            }
        }
    }

    /**
     * 获取活体人脸认证TOKEN
     * */
    private void getVfToken() {
        showProgress(false);
        List<UpLoadPhotoBean> upLoadPhotoBeans = new ArrayList<>();
        UpLoadPhotoBean bean = new UpLoadPhotoBean();
        bean.setUrl(headerUrl);
        bean.setStatusFlag(0);
        bean.setRedPacketFee(0);
        bean.setType(1);
        upLoadPhotoBeans.add(bean);
        String multimediaeInfo = JSON.toJSONString(upLoadPhotoBeans);
        UserApi.getVfToken("2", multimediaeInfo, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    VfTokenBean vfTokenBean = (VfTokenBean) object;
                    authentication(vfTokenBean);
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

    private void authentication(VfTokenBean vfTokenBean) {
        RPSDK.startVerifyByNative(vfTokenBean.getVerifyToken(), mActivity, new RPSDK.RPCompletedListener() {
            @Override
            public void onAuditResult(RPSDK.AUDIT audit, String code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (audit == RPSDK.AUDIT.AUDIT_PASS) {
//                            // 认证通过。
//                            authenticationPass(vfTokenBean);
//                        }else {
//                            toast("认证识别失败");
//                        }
                        authenticationPass(vfTokenBean);
                    }
                });

//                else if(audit == RPSDK.AUDIT.AUDIT_FAIL) {
//                    // 认证不通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
//                    // do something
//                } else if(audit == RPSDK.AUDIT.AUDIT_NOT) {
//                    // 未认证，具体原因可通过code来区分（code取值参见下方表格），通常是用户主动退出或者姓名身份证号实名校验不匹配等原因，导致未完成认证流程
//                    // do something
//                }
            }
        });
    }

    /**
     * 认证通过通知服务端
     *
     * @param vfTokenBean*/
    private void authenticationPass(VfTokenBean vfTokenBean) {
        showProgress(false);
        UserApi.authenticationPass("2", vfTokenBean.getTaskId(), mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    if (progress < 4){
                        progress ++ ;
                    }
                    progressTag();
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

    private void progressTag() {
        tvStageOne.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        tvStageTwo.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        tvStageThree.setBackground(getResources().getDrawable(R.mipmap.kongxin_yuan_logo));
        llStageOneDescribe.setVisibility(View.GONE);
        llStageTwoDescribe.setVisibility(View.GONE);
        llStageThreeDescribe.setVisibility(View.GONE);
        imgUpPhotos.setVisibility(View.GONE);
        rlTongGuo.setVisibility(View.GONE);
        tvTips.setVisibility(View.GONE);
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
                tvNext.setVisibility(View.GONE);
                imgUpPhotos.setVisibility(View.GONE);
                rlTongGuo.setVisibility(View.VISIBLE);
//                tvTips.setVisibility(View.VISIBLE);
                Constants.REFRESH = true;
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
                headerUrl = url;
                Glide.with(mActivity).load(url).placeholder(R.mipmap.zhanwei_logo).error(R.mipmap.zhanwei_logo).into(imgUpPhotos);
//                imgUpPhotos.setClickable(false);
            }
        });
    }

}
