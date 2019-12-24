package com.yqbj.yhgy.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.yqbj.yhgy.R;

/**
 * 自定义下拉刷新头
 * */
public class MyRefreshHeader extends RelativeLayout implements RefreshHeader {
    private ImageView mArrow;
    private ImageView imgAnimation;
    private AnimationDrawable animationDrawable;
//    private boolean isArrowDown = false;

    public MyRefreshHeader(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public MyRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public MyRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setMinimumHeight(dp2px(context, 30));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        View headerView = View.inflate(context, R.layout.my_refresh_header, null);
        mArrow = headerView.findViewById(R.id.iv_refresh_center);
        imgAnimation = headerView.findViewById(R.id.img_animation);
        addView(headerView, params);
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) { // 尺寸定义完成

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {

    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        imgAnimation.clearAnimation();
        mArrow.clearAnimation();
        return 100; // 动画结束,延迟多少毫秒之后再收回
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        setBackgroundColor(getResources().getColor(R.color.activity_grey_bg));
    }

    @NonNull
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) { // 状态改变事件
        switch (newState) {
            case None: // 无状态
                break;
            case PullDownToRefresh: // 可以下拉状态
//                mTextView.setText("下拉即可刷新");
                imgAnimation.setVisibility(GONE);
                mArrow.setVisibility(VISIBLE);
                RotateAnimation ra = new RotateAnimation(180, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(200);
                ra.setRepeatCount(0);
                ra.setInterpolator(new LinearInterpolator());
                ra.setFillAfter(true);
                mArrow.startAnimation(ra);
                break;
            case Refreshing: // 刷新中状态
//                mTextView.setText("正在刷新数据...");
                mArrow.clearAnimation();
                imgAnimation.setVisibility(VISIBLE);
                mArrow.setVisibility(GONE);
                animationDrawable = (AnimationDrawable) imgAnimation.getDrawable();
                animationDrawable.start();
                break;
            case ReleaseToRefresh:  // 释放就开始刷新状态
//                mTextView.setText("松开立即刷新");
                imgAnimation.setVisibility(GONE);
                mArrow.setVisibility(VISIBLE);
                RotateAnimation raa = new RotateAnimation(0, 180,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                raa.setDuration(200);
                raa.setRepeatCount(0);
                raa.setInterpolator(new LinearInterpolator());
                raa.setFillAfter(true);
                mArrow.startAnimation(raa);
                break;
            case Loading:
                imgAnimation.setVisibility(VISIBLE);
                mArrow.setVisibility(GONE);
                break;
        }
    }

    /**
     * dp转px
     */
    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
