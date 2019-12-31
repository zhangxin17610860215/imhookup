package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.common.util.SPUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.UserBean;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.DemoCache;
import com.yqbj.yhgy.view.CautionDialog;

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
                Constants.USER_ATTRIBUTE.GENDER = gender == 1 ? "1" : "2";
                new XPopup.Builder(activity)
                        .dismissOnTouchOutside(false)
                        .asCustom(new CautionDialog(activity, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //注册
                                signup("2");
                            }
                        }))
                        .show();
                break;
        }
    }

    /**
     * 注册
     * */
    private void signup(String signupType) {
        showProgress(false);
        UserApi.signup(signupType, Constants.USER_ATTRIBUTE.PHONE, Constants.USER_ATTRIBUTE.VFCODE,
                Constants.USER_ATTRIBUTE.PSW, Constants.USER_ATTRIBUTE.GENDER,
                Constants.USER_ATTRIBUTE.WXTOKEN, Constants.USER_ATTRIBUTE.OPENID, Constants.USER_ATTRIBUTE.WXUUID,
                activity, new RequestCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        dismissProgress();
                        UserBean userBean = (UserBean) object;
                        DemoCache.setAccount(userBean.getInfo().getAccid());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_USERACCOUNT,userBean.getInfo().getAccount());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_USERACCID,userBean.getInfo().getAccid());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_NAME,userBean.getInfo().getNikename());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_YUNXINTOKEN,userBean.getInfo().getYunxinToken());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_USERTOKEN,userBean.getInfo().getUserToken());
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_WXTOKEN,Constants.USER_ATTRIBUTE.WXTOKEN);
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_WXUUID,Constants.USER_ATTRIBUTE.WXUUID);
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_OPENID,Constants.USER_ATTRIBUTE.OPENID);
                        SPUtils.getInstance(userBean.getInfo().getAccid()).put(Constants.USER_ATTRIBUTE.SP_GENDER,Constants.USER_ATTRIBUTE.GENDER);
                        //跳转完善资料
                        PerfectDataActivity.start(activity);
                    }

                    @Override
                    public void onFailed(String errMessage) {
                        dismissProgress();
                    }
                });
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
