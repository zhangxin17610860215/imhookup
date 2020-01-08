package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.util.CityBean;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.main.MainActivity;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StatusBarsUtil;
import com.yqbj.yhgy.utils.StringUtil;
import com.yqbj.yhgy.view.ChoiceCityDialog;
import com.yqbj.yhgy.view.CustomDatePicker;
import com.yqbj.yhgy.view.MiddleListDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yqbj.yhgy.config.Constants.CITYBEANLIST;
import static com.yqbj.yhgy.config.Constants.OCCUPATIONBEANLIST;

/**
 * 完善资料页面
 */
public class PerfectDataActivity extends BaseActivity {

    @BindView(R.id.img_upHead)
    RoundedImageView imgUpHead;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_selecteCity)
    TextView tvSelecteCity;
    @BindView(R.id.tv_Birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_Occupation)
    TextView tvOccupation;
    @BindView(R.id.tv_Expect)
    TextView tvExpect;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.et_QQ)
    EditText etQQ;
    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.et_introduce)
    EditText etIntroduce;
    @BindView(R.id.tv_isShow)
    TextView tvIsShow;

    private Activity activity;
    private List<CityBean> options1Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Value = new ArrayList<>();
    private List<CityBean> occupation1Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> occupation2Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> occupation2Value = new ArrayList<>();
    /**
     * 已选过的城市
     * */
    private List<String> selCityValueList = new ArrayList<>();
    private List<String> selCityTextList = new ArrayList<>();

    /**
     * 已选过的职业
     * */
    private List<String> selOccupationValueList = new ArrayList<>();
    private List<String> selOccupationTextList = new ArrayList<>();

    /**
     * 是否隐藏社交账号   默认关闭(不隐藏)
     * */
    private boolean isHideAccounts = false;

    private AbortableFuture<LoginInfo> loginRequest;

    private String headUrl,nikeName,city,birthday,job,desiredGoals,qq,weChat,hidecontactinfo,
                    height,weight,description;
    private String type = "";//type == 0,未登录   type == 1,已登录

    public static void start(Context context,String type) {
        Intent intent = new Intent(context, PerfectDataActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfectdata_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");

        initData();
        initCityData();
        initOccupationData();
        hideAccounts();
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
//        imgUpHead.setIsRect(true);
        headUrl = Preferences.getHeadImag();
        nikeName = Preferences.getNikename();
        Glide.with(activity).load(headUrl).error(R.mipmap.default_head_logo).into(imgUpHead);
        if (StringUtil.isNotEmpty(nikeName)){
            etName.setText(nikeName);
        }
        //默认选中城市
        selCityValueList.clear();
        selCityValueList.add("140800");
        selCityValueList.add("110100");
        selCityTextList.clear();
        selCityTextList.add("运城市");
        selCityTextList.add("北京市");
        String cityText = "";
        for (int i = 0; i < selCityTextList.size(); i++){
            cityText = cityText + selCityTextList.get(i) + "/";
        }
        if (cityText.contains("/")){
            cityText = cityText.substring(0,cityText.length()-1);
        }
        tvSelecteCity.setText(cityText);
        tvSelecteCity.setTextColor(getResources().getColor(R.color.color_333333));

        //默认选中职业
        selOccupationValueList.clear();
        selOccupationValueList.add("1002");
        selOccupationTextList.clear();
        selOccupationTextList.add("IT");
        tvOccupation.setText(selOccupationTextList.get(0));
        tvOccupation.setTextColor(getResources().getColor(R.color.color_333333));
    }

    /**
     * 初始化城市json数据
     * */
    private void initCityData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initCityJsonData();
            }
        }).start();
    }

    /**
     * 初始化职业json数据
     * */
    private void initOccupationData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initOccupationJsonData();
            }
        }).start();
    }

    private void initCityJsonData() {
        CityBean cityBean;
        List<CityBean.ChildBeanX> city;
        for (int i = 0; i < CITYBEANLIST.size(); i ++ ){
            cityBean = CITYBEANLIST.get(i);
            ArrayList<String> cityStringList = new ArrayList<>();
            ArrayList<String> cityStringValue = new ArrayList<>();
            for (int j = 0; j < cityBean.getChild().size(); j ++){
                city = CITYBEANLIST.get(i).getChild();
                cityStringList.add(city.get(j).getText());
                cityStringValue.add(city.get(j).getValue());
            }

            options1Text.add(cityBean);
            options2Text.add(cityStringList);
            options2Value.add(cityStringValue);
        }
    }

    private void initOccupationJsonData() {
        CityBean occupationBean;
        List<CityBean.ChildBeanX> occupation;
        for (int i = 0; i < OCCUPATIONBEANLIST.size(); i ++ ){
            occupationBean = OCCUPATIONBEANLIST.get(i);
            ArrayList<String> occupationStringList = new ArrayList<>();
            ArrayList<String> occupationStringValue = new ArrayList<>();
            for (int j = 0; j < occupationBean.getChild().size(); j ++){
                occupation = OCCUPATIONBEANLIST.get(i).getChild();
                occupationStringList.add(occupation.get(j).getText());
                occupationStringValue.add(occupation.get(j).getValue());
            }

            occupation1Text.add(occupationBean);
            occupation2Text.add(occupationStringList);
            occupation2Value.add(occupationStringValue);
        }
    }

    @OnClick({R.id.img_upHead, R.id.tv_selecteCity, R.id.tv_Birthday, R.id.tv_Occupation, R.id.tv_Expect, R.id.tv_height, R.id.tv_weight, R.id.tv_isShow, R.id.tv_Submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_upHead:
                //上传头像
                showSelector(R.string.input_panel_photo, 100, true, 1);
                break;
            case R.id.tv_selecteCity:
                //选择城市
                if (options1Text.size() > 0 && options2Text.size() > 0){
                    initPopupWindow(CITYBEANLIST,selCityValueList,selCityTextList,1,4);
                }
                break;
            case R.id.tv_Birthday:
                //选择生日
                DatePicker();
                break;
            case R.id.tv_Occupation:
                //选择职业
                if (occupation1Text.size() > 0 &&occupation2Text.size() > 0){
                    initPopupWindow(OCCUPATIONBEANLIST,selOccupationValueList,selOccupationTextList,2,1);
                }
                break;
            case R.id.tv_Expect:
                //期望对象
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new MiddleListDialog(activity, 1, new MiddleListDialog.OnClickListener() {
                            @Override
                            public void onClick(String text) {
                                tvExpect.setText(text);
                                tvExpect.setTextColor(getResources().getColor(R.color.black));
                            }
                        }))
                        .show();
                break;
            case R.id.tv_height:
                //身高
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(true)
                        .asCustom(new MiddleListDialog(activity, 2, new MiddleListDialog.OnClickListener() {
                            @Override
                            public void onClick(String text) {
                                tvHeight.setText(text);
                                tvHeight.setTextColor(getResources().getColor(R.color.black));
                            }
                        }))
                        .show();
                break;
            case R.id.tv_weight:
                //体重
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(true)
                        .asCustom(new MiddleListDialog(activity, 3, new MiddleListDialog.OnClickListener() {
                            @Override
                            public void onClick(String text) {
                                tvWeight.setText(text);
                                tvWeight.setTextColor(getResources().getColor(R.color.black));
                            }
                        }))
                        .show();
                break;
            case R.id.tv_isShow:
                //是否隐藏社交账号
                isHideAccounts = !isHideAccounts;
                hideAccounts();
                break;
            case R.id.tv_Submission:
                //提交
                readyData();
                break;
        }
    }

    private void readyData() {
        city = "";
        job = "";
        for (String cityValue : selCityValueList){
            city = city + cityValue + ",";
        }
        if (StringUtil.isNotEmpty(city) && city.contains(",")){
            city = city.substring(0, city.length() - 1);
        }
        for (String occupationValue : selOccupationValueList){
            job = job + occupationValue + ",";
        }
        if (StringUtil.isNotEmpty(job) && job.contains(",")){
            job = job.substring(0, job.length() - 1);
        }
        nikeName = etName.getText().toString().trim();
        birthday = tvBirthday.getText().toString().trim();
        desiredGoals = tvExpect.getText().toString().trim();
        height = tvHeight.getText().toString().trim();
        weight = tvWeight.getText().toString().trim();
        qq = etQQ.getText().toString().trim();
        weChat = etWechat.getText().toString().trim();
        description = etIntroduce.getText().toString().trim();
        hidecontactinfo = isHideAccounts ? "1":"0";
        if (StringUtil.isEmpty(nikeName) || nikeName.equals("用户昵称")){
            toast("请填写昵称");
            return;
        }
        if (StringUtil.isEmpty(headUrl)){
            toast("请上传头像");
            return;
        }
        if (StringUtil.isEmpty(birthday) || birthday.equals("请选择")){
            toast("请选择生日");
            return;
        }
        if (StringUtil.isEmpty(height) || height.equals("请选择")){
            toast("请选择身高");
            return;
        }
        if (StringUtil.isEmpty(weight) || weight.equals("请选择")){
            toast("请选择体重");
            return;
        }
        if (StringUtil.isEmpty(desiredGoals) || desiredGoals.equals("请选择")){
            toast("请选择期望对象");
            return;
        }
        if (StringUtil.isEmpty(job) || job.equals("请选择")){
            toast("请选择职业");
            return;
        }
        if (StringUtil.isEmpty(city) || city.equals("请选择")){
            toast("请选择城市");
            return;
        }
        if (StringUtil.isEmpty(qq) && StringUtil.isEmpty(weChat)){
            toast("社交账号请至少填写一个");
            return;
        }
        upDateInfo();
    }

    /**
     * 更新用户信息
     * */
    private void upDateInfo() {
        showProgress(false);
        UserApi.upDateUserInfo(nikeName, headUrl, birthday, height, weight, job, city, description,
                "", desiredGoals, hidecontactinfo, qq, weChat, activity, new RequestCallback() {
                    @Override
                    public void onSuccess(int code, Object object) {
                        dismissProgress();
                        if (code == Constants.SUCCESS_CODE){
                            toast("信息更改成功");
                            if (StringUtil.isNotEmpty(type) && type.equals("0")){
                                MainActivity.start(activity);
                            }else {
                                Constants.REFRESH = true;
                            }
                            finish();
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
     * 切换隐藏社交账号
     * */
    private void hideAccounts() {
        tvIsShow.setBackgroundResource(isHideAccounts ? R.mipmap.isshow_open : R.mipmap.isshow_close);
    }

    private CustomDatePicker customDatePicker;
    private String now;

    /**
     * 显示时间
     */
    private void DatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        //获取当前时间
        now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvBirthday.setText(time.split(" ")[0]);
                tvBirthday.setTextColor(getResources().getColor(R.color.black));
                birthday=time.split(" ")[0];

            }
        }, "1960-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        if (StringUtil.isEmpty(birthday) || birthday.equals("请选择")){
            customDatePicker.show(now);
        }else {
            customDatePicker.show(birthday);
        }

    }

    private void initPopupWindow(ArrayList<CityBean> beanList, List<String> selValueList, List<String> selTextList, final int selType, int optionNumber) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        new XPopup.Builder(activity)
                .dismissOnTouchOutside(false)
                .maxHeight(height - StatusBarsUtil.getStatusBarHeight(activity))   //窗口高度减去状态栏高度
                .asCustom(new ChoiceCityDialog(activity, beanList, selValueList, selTextList, selType, optionNumber, new ChoiceCityDialog.DetermineOnClickListener() {
                    @Override
                    public void determineOnClickListener(List<String> textList, List<String> valueList) {
                        if (null == valueList || textList.size() <= 0){
                            if (selType == 1){
                                tvSelecteCity.setText("请选择");
                                tvSelecteCity.setTextColor(getResources().getColor(R.color.edit_hint_color));
                            }else if (selType == 2){
                                tvOccupation.setText("请选择");
                                tvOccupation.setTextColor(getResources().getColor(R.color.edit_hint_color));
                            }
                            return;
                        }
                        if (selType == 1){
                            String cityText = "";
                            for (int i = 0; i < textList.size(); i++){
                                cityText = cityText + textList.get(i) + "/";
                            }
                            if (cityText.contains("/")){
                                cityText = cityText.substring(0,cityText.length()-1);
                            }
                            tvSelecteCity.setText(cityText);
                            tvSelecteCity.setTextColor(getResources().getColor(R.color.color_333333));
                        }else if (selType == 2){
                            tvOccupation.setText(textList.get(0));
                            tvOccupation.setTextColor(getResources().getColor(R.color.color_333333));
                        }

                    }
                }))
                .show();
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, int requestCode, boolean multiSelect, int number) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(multiSelect).setSelectMax(number);
        option.setSaveRectangle(true);
        ImagePickerLauncher.selectImage(activity, requestCode, option, titleId);
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
            ToastHelper.showToastLong(activity, R.string.picker_image_error);
            return;
        }
        sendImageAfterSelfImagePicker(data);
    }

    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(activity, data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig, int imgListSize) {
                fileToUrl(file);
            }
        });
    }

    /**
     * 将file转换成URL
     * */
    private void fileToUrl(File file) {
        if (file == null) {
            return;
        }
        NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG).setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int i, final String url, Throwable throwable) {
                if (i != ResponseCode.RES_SUCCESS){
                    toast("上传头像失败，请稍后重试");
                    return;
                }
                Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
                fields.put(UserInfoFieldEnum.AVATAR, url);
                NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void aVoid, Throwable throwable) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            toast("头像上传成功");
                            Glide.with(activity).load(url).error(R.mipmap.default_head_logo).into(imgUpHead);
                            headUrl = url;
                        } else {
                            toast("上传头像失败，请稍后重试");
                        }
                    }
                });
            }
        });
    }
}
