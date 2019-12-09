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
    private List<CityBean.ChildBeanX> city = new ArrayList<>();
    private List<CityBean> options1Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Text = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Value = new ArrayList<>();
    private ArrayList<CityBean> cityBeanList = new ArrayList<>();
    /**
     * 已选过的城市
     * */
    private List<String> selValueList = new ArrayList<>();
    private List<String> selTextList = new ArrayList<>();

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
    }

    private void initData() {
        selValueList.clear();
        selValueList.add("140800");
        selValueList.add("110100");
        selTextList.clear();
        selTextList.add("运城市");
        selTextList.add("北京市");
        String cityText = "";
        for (int i = 0; i < selTextList.size(); i++){
            cityText = cityText + selTextList.get(i) + "/";
        }
        if (cityText.contains("/")){
            cityText = cityText.substring(0,cityText.length()-1);
        }
        tvSelecteCity.setText(cityText);
        tvSelecteCity.setTextColor(getResources().getColor(R.color.color_333333));
    }

    /**
     * 初始化城市json数据
     * */
    private void initCityData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initJsonData();
            }
        }).start();
    }

    private void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        Log.e("TAG","JsonData>>>>>>>>" + JsonData);
        cityBeanList = parseData(JsonData);//用Gson 转成实体

        for (int i = 0; i < cityBeanList.size(); i ++ ){
            cityBean = cityBeanList.get(i);
//            LogUtil.e(TAG, "省>>>>"+cityBean.getText());
            ArrayList<String> cityStringList = new ArrayList<>();
            ArrayList<String> cityStringValue = new ArrayList<>();
//            ArrayList<ArrayList<String>> countyStringList = new ArrayList<>();
//            ArrayList<ArrayList<String>> countyStringValue = new ArrayList<>();
            for (int j = 0; j < cityBean.getChild().size(); j ++){
                ArrayList<String> strings = new ArrayList<>();
                ArrayList<String> stringv = new ArrayList<>();
                city = cityBeanList.get(i).getChild();
//                LogUtil.e(TAG, "市>>>>"+city.get(j).getText());
                cityStringList.add(city.get(j).getText());
                cityStringValue.add(city.get(j).getValue());
//                for (int x = 0; x < city.get(j).getChild().size(); x ++){
//                    child = city.get(j).getChild();
////                    LogUtil.e(TAG, "县>>>>"+child.get(x).getText());
//                    strings.add(child.get(x).getText());
//                    stringv.add(child.get(x).getValue());
//                }
//                countyStringList.add(strings);
//                countyStringValue.add(stringv);
            }

            options1Text.add(cityBean);
            options2Text.add(cityStringList);
            options2Value.add(cityStringValue);
//            options3Text.add(countyStringList);
//            options3Value.add(countyStringValue);
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
                    initPopupWindow();
                }
                break;
            case R.id.tv_Birthday:
                //选择生日
                DatePicker();
                break;
            case R.id.tv_Occupation:
                //选择职业
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
                //是否隐藏
                break;
            case R.id.tv_Submission:
                //提交
                MainActivity.start(activity);
                break;
        }
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

    private void initPopupWindow() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        new XPopup.Builder(activity)
                .dismissOnTouchOutside(false)
                .maxHeight(height - StatusBarsUtil.getStatusBarHeight(activity))   //窗口高度减去状态栏高度
                .asCustom(new ChoiceCityDialog(activity, cityBeanList, selValueList, selTextList, 1, 4, new ChoiceCityDialog.DetermineOnClickListener() {
                    @Override
                    public void determineOnClickListener(List<String> textList, List<String> valueList) {
                        if (null == valueList || textList.size() <= 0){
                            return;
                        }
                        String cityText = "";
                        for (int i = 0; i < textList.size(); i++){
                            cityText = cityText + textList.get(i) + "/";
                        }
                        if (cityText.contains("/")){
                            cityText = cityText.substring(0,cityText.length()-1);
                        }
                        tvSelecteCity.setText(cityText);
                        tvSelecteCity.setTextColor(getResources().getColor(R.color.color_333333));
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
            public void sendImage(File file, boolean isOrig) {
                imgUpHead.setIsRect(true);
                Glide.with(activity).load(file.getPath()).into(imgUpHead);
            }
        });
    }
}
