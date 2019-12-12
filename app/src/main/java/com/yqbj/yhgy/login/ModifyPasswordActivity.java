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
                break;
        }
    }
}
