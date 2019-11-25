package com.yuanqi.hangzhou.imhookup.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;
import com.yuanqi.hangzhou.imhookup.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完善资料页面
 */
public class PerfectDataActivity extends BaseActivity {

    @BindView(R.id.img_upHead)
    ImageView imgUpHead;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_selecteCity)
    TextView tvSelecteCity;
    @BindView(R.id.tv_Birthday)
    TextView tvBirthday;
    @BindView(R.id.et_QQ)
    EditText etQQ;
    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.tv_isShow)
    TextView tvIsShow;
    private Activity activity;

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
    }

    @OnClick({R.id.img_upHead, R.id.tv_selecteCity, R.id.tv_Birthday, R.id.tv_isShow, R.id.tv_Submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_upHead:
                //上传头像
                break;
            case R.id.tv_selecteCity:
                //选择城市
                break;
            case R.id.tv_Birthday:
                //选择生日
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
}
