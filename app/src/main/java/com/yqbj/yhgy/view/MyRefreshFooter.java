package com.yqbj.yhgy.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.yqbj.yhgy.R;

/**
 * 自定义上拉加载脚
 * */
public class MyRefreshFooter extends RelativeLayout implements RefreshFooter {

    private ImageView imgAnimation;
    private AnimationDrawable animationDrawable;

    public MyRefreshFooter(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public MyRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public MyRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setMinimumHeight(dp2px(context, 30));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        View headerView = View.inflate(context, R.layout.my_refresh_header, null);
        imgAnimation = headerView.findViewById(R.id.img_animation);
        imgAnimation.setVisibility(VISIBLE);
        addView(headerView, params);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
    }

    @Override
    public void setPrimaryColors(int... colors) {
        setBackgroundColor(getResources().getColor(R.color.activity_grey_bg));
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        animationDrawable = (AnimationDrawable) imgAnimation.getDrawable();
        animationDrawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        imgAnimation.clearAnimation();
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
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }

    /**
     * dp转px
     */
    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
