package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 平台使用规范       type == 1
 * 用户使用协议       type == 2
 * 用户隐私政策       type == 3
 */
public class AgreementActivity extends BaseActivity {

    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.webView_agreement)
    WebView webView;

    private Activity mActivity;
    private String type = "";
    private String webUrl = "";

    public static void start(Context context, String type) {
        Intent intent = new Intent(context, AgreementActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        if (type.equals("1")) {
            setToolbar(mActivity, 0, "平台使用规范");
            webUrl = "file:////android_asset/standard.html";
        } else if (type.equals("2")) {
            setToolbar(mActivity, 0, "用户使用协议");
            webUrl = "file:////android_asset/agreement.html";
        } else if (type.equals("3")) {
            setToolbar(mActivity, 0, "用户隐私政策");
            webUrl = "file:////android_asset/privacy.html";
        }
        mProgressBar.setMax(100);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 0) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        webView.loadUrl(webUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //挂在后台  资源释放
        webView.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        webView.setVisibility(View.GONE);
        webView.destroy();
        super.onDestroy();
    }
}
