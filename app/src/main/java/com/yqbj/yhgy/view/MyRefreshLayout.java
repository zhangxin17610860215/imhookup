package com.yqbj.yhgy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.yqbj.yhgy.R;

/**
 * 自定义RefreshLayout
 * */
public class MyRefreshLayout extends SmartRefreshLayout {

    public MyRefreshLayout(Context context) {
        super(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public static void init() {
        // 指定全局的下拉Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MyRefreshHeader(context);
            }
        });

        // 指定全局的上拉Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new MyRefreshFooter(context);
//                return new ClassicsFooter(context);
            }
        });
    }

    private void initView(Context context) {
        setReboundDuration(300); // 设置回弹动画时长
//        setHeaderMaxDragRate(1.0f);
//        setHeaderHeight(0.1f);
//        setHeaderTriggerRate(0.01f);
        setPrimaryColorsId(R.color.white);  // 主题色
        setEnableAutoLoadMore(false); // 设置是否监听列表在滚动到底部时触发加载事件
    }

}
