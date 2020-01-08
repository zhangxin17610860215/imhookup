package com.yqbj.yhgy.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yqbj.yhgy.view.DialogUtils;
import com.yqbj.yhgy.view.LoadingDialog;

/**
 * Fragment的基类，处理通用的逻辑
 */
public class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected Activity mActivity;
    //是否可见状态
    private boolean isVisible;
    //View已经初始化完成
    private boolean isPrepared;
    //是否第一次加载完
    private boolean isFirstLoad = true;

    // protected AlertDialog mProgressDialog;
    protected LoadingDialog mProgressDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        this.mActivity = getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void toast(String tips) {
        Toast.makeText(getActivity(), tips, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Loading对话框
     *
     * @param cancelable 是否可取消
     */
    protected void showProgress(boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDialog(getActivity(), cancelable);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setCancelable(cancelable);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog.show();
    }

    protected void dismissProgress() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.dismiss();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    //这个是不在viewpager中的隐藏和显示
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        lazyLoadData();
        isFirstLoad = false;
    }

    protected void lazyLoadData() {

    }

    @Override
    public void onDestroy() {
        mActivity = null;
        super.onDestroy();
    }

}
