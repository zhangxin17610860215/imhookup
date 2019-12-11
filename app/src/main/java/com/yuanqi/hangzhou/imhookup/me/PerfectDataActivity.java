package com.yuanqi.hangzhou.imhookup.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.netease.nim.uikit.business.session.helper.SendImageHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.CityBean;
import com.netease.nim.uikit.common.util.GetJsonDataUtil;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;
import com.yuanqi.hangzhou.imhookup.main.MainActivity;
import com.yuanqi.hangzhou.imhookup.utils.StatusBarsUtil;
import com.yuanqi.hangzhou.imhookup.utils.StringUtil;
import com.yuanqi.hangzhou.imhookup.view.ChoiceCityDialog;
import com.yuanqi.hangzhou.imhookup.view.CustomDatePicker;
import com.yuanqi.hangzhou.imhookup.view.MiddleListDialog;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善资料页面
 */
public class PerfectDataActivity extends BaseActivity {

    @BindView(R.id.img_upHead)
    HeadImageView imgUpHead;
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
    private CityBean cityBean = new CityBean();
    private CityBean occupationBean = new CityBean();
    private List<CityBean.ChildBeanX> city = new ArrayList<>();
    private List<CityBean.ChildBeanX> occupation = new ArrayList<>();
    private List<CityBean> options1Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Value = new ArrayList<>();
    private List<CityBean> occupation1Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> occupation2Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> occupation2Value = new ArrayList<>();
    private ArrayList<CityBean> beanList = new ArrayList<>();
    private ArrayList<CityBean> cityBeanList = new ArrayList<>();
    private ArrayList<CityBean> occupationBeanList = new ArrayList<>();
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
     * 已选过的城市/职业
     * */
    private List<String> selValueList = new ArrayList<>();
    private List<String> selTextList = new ArrayList<>();

    /**
     * 是否隐藏社交账号   默认关闭(不隐藏)
     * */
    private boolean isHideAccounts = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, PerfectDataActivity.class);
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
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        Log.e("TAG","CityJsonData>>>>>>>>" + JsonData);
        cityBeanList = parseData(JsonData);//用Gson 转成实体

        for (int i = 0; i < cityBeanList.size(); i ++ ){
            cityBean = cityBeanList.get(i);
            ArrayList<String> cityStringList = new ArrayList<>();
            ArrayList<String> cityStringValue = new ArrayList<>();
            for (int j = 0; j < cityBean.getChild().size(); j ++){
                city = cityBeanList.get(i).getChild();
                cityStringList.add(city.get(j).getText());
                cityStringValue.add(city.get(j).getValue());
            }

            options1Text.add(cityBean);
            options2Text.add(cityStringList);
            options2Value.add(cityStringValue);
        }
    }

    private void initOccupationJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "occupation.json");//获取assets目录下的json文件数据
        Log.e("TAG","OccupationJsonData>>>>>>>>" + JsonData);
        occupationBeanList = parseData(JsonData);//用Gson 转成实体

        for (int i = 0; i < occupationBeanList.size(); i ++ ){
            occupationBean = occupationBeanList.get(i);
            ArrayList<String> occupationStringList = new ArrayList<>();
            ArrayList<String> occupationStringValue = new ArrayList<>();
            for (int j = 0; j < occupationBean.getChild().size(); j ++){
                occupation = occupationBeanList.get(i).getChild();
                occupationStringList.add(occupation.get(j).getText());
                occupationStringValue.add(occupation.get(j).getValue());
            }

            occupation1Text.add(occupationBean);
            occupation2Text.add(occupationStringList);
            occupation2Value.add(occupationStringValue);
        }
    }

    public ArrayList<CityBean> parseData(String result) {//Gson 解析
        ArrayList<CityBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
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
                if (options1Text.size() >= 0 && options2Text.size() >= 0){
                    initPopupWindow(cityBeanList,selCityValueList,selCityTextList,1,4);
                }
                break;
            case R.id.tv_Birthday:
                //选择生日
                DatePicker();
                break;
            case R.id.tv_Occupation:
                //选择职业
                if (occupation1Text.size() >= 0 &&occupation2Text.size() >= 0){
                    initPopupWindow(occupationBeanList,selOccupationValueList,selOccupationTextList,2,1);
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
                                tvExpect.setTextColor(getResources().getColor(R.color.black));
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
                                tvExpect.setTextColor(getResources().getColor(R.color.black));
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
                MainActivity.start(activity);
                break;
        }
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

            }
        }, "1960-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        if (StringUtil.isEmpty(tvBirthday.getText().toString().trim()) || tvBirthday.getText().toString().equals("请选择")){
            customDatePicker.show(now);
        }else {
            customDatePicker.show(tvBirthday.getText().toString());
        }

    }

    private void initPopupWindow(ArrayList<CityBean> beanList, List<String> selValueList, List<String> selTextList, int type, int optionNumber) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        new XPopup.Builder(activity)
                .dismissOnTouchOutside(false)
                .maxHeight(height - StatusBarsUtil.getStatusBarHeight(activity))   //窗口高度减去状态栏高度
                .asCustom(new ChoiceCityDialog(activity, beanList, selValueList, selTextList, type, optionNumber, new ChoiceCityDialog.DetermineOnClickListener() {
                    @Override
                    public void determineOnClickListener(List<String> textList, List<String> valueList) {
                        if (null == valueList || textList.size() <= 0){
                            return;
                        }
                        if (type == 1){
                            String cityText = "";
                            for (int i = 0; i < textList.size(); i++){
                                cityText = cityText + textList.get(i) + "/";
                            }
                            if (cityText.contains("/")){
                                cityText = cityText.substring(0,cityText.length()-1);
                            }
                            tvSelecteCity.setText(cityText);
                            tvSelecteCity.setTextColor(getResources().getColor(R.color.color_333333));
                        }else if (type == 2){
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
                imgUpHead.setIsRect(true);
                Glide.with(activity).load(file.getPath()).into(imgUpHead);
            }
        });
    }
}
