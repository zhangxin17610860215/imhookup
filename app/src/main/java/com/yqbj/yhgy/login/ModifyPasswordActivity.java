package com.yqbj.yhgy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.config.Constants;
import com.yqbj.yhgy.requestutils.RequestCallback;
import com.yqbj.yhgy.requestutils.api.UserApi;
import com.yqbj.yhgy.utils.Preferences;
import com.yqbj.yhgy.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.et_OriginalPsw)
    EditText etOriginalPsw;
    @BindView(R.id.et_newPsw)
    EditText etNewPsw;

    private Activity mActivity;
    private String oldPasssword = "";
    private String passsword = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, ModifyPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypassword_layout);
        ButterKnife.bind(this);

        mActivity = this;
        setToolbar(mActivity, 0, "");
    }

    @OnClick({R.id.tv_forgetPsw, R.id.tv_Submission})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgetPsw:
                //忘记密码
                ForgetPasswordActivity.start(mActivity);
                break;
            case R.id.tv_Submission:
                oldPasssword = etOriginalPsw.getText().toString().trim();
                passsword = etNewPsw.getText().toString().trim();
                if (StringUtil.isEmpty(oldPasssword)){
                    toast("请输入原密码");
                    return;
                }
                if (StringUtil.isEmpty(passsword)){
                    toast("请输入新密码");
                    return;
                }
                resetPassword();
                break;
        }
    }

    /**
     * 重置密码
     * */
    private void resetPassword() {
        showProgress(false);
        UserApi.resetPassword(oldPasssword, passsword, mActivity, new RequestCallback() {
            @Override
            public void onSuccess(int code, Object object) {
                dismissProgress();
                if (code == Constants.SUCCESS_CODE){
                    Preferences.savePsw(passsword);
                    toast("密码修改成功");
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
}
