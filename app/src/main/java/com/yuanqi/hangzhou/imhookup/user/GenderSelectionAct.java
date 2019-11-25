package com.yuanqi.hangzhou.imhookup.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.yuanqi.hangzhou.imhookup.R;
import com.yuanqi.hangzhou.imhookup.base.BaseActivity;
import com.yuanqi.hangzhou.imhookup.view.CautionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择的性别
 */
public class GenderSelectionAct extends BaseActivity {

    @BindView(R.id.img_male)
    ImageView imgMale;
    @BindView(R.id.tv_male)
    TextView tvMale;
    @BindView(R.id.img_female)
    ImageView imgFemale;
    @BindView(R.id.tv_female)
    TextView tvFemale;
    private Activity activity;
    /**
     * gender == 1 (男)
     * gender == 2 (女)
     * */
    private int gender = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, GenderSelectionAct.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genderselection_layout);
        ButterKnife.bind(this);

        activity = this;
        setToolbar(activity, 0, "");
        changeOptions();
    }

    @OnClick({R.id.img_male, R.id.img_female, R.id.tv_Determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_male:
                //选择男
                gender = 1;
                changeOptions();
                break;
            case R.id.img_female:
                //选择女
                gender = 2;
                changeOptions();
                break;
            case R.id.tv_Determine:
                //确定
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new CautionDialog(activity))
                        .show();
                break;
        }
    }

    private void changeOptions() {
        if (gender == 1){
            //男
            tvMale.setTextColor(getResources().getColor(R.color.color_7265F6));
            imgMale.setImageResource(R.mipmap.male_yes);
            tvFemale.setTextColor(getResources().getColor(R.color.black));
            imgFemale.setImageResource(R.mipmap.female_no);
        }else {
            //女
            tvMale.setTextColor(getResources().getColor(R.color.black));
            imgMale.setImageResource(R.mipmap.male_no);
            tvFemale.setTextColor(getResources().getColor(R.color.color_7265F6));
            imgFemale.setImageResource(R.mipmap.female_yes);
        }
    }
}
