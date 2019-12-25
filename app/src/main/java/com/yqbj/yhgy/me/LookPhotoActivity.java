package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.api.NimUIKit;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.utils.ImageFilter;
import com.yqbj.yhgy.view.MiddleDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看照片
 */
public class LookPhotoActivity extends BaseActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_Title)
    TextView tvTitle;
    @BindView(R.id.tv_Delete)
    TextView tvDelete;

    private Activity mActivity;
    private List<PhotoBean> photoList = new ArrayList<>();
    private MyPagerAdapter pagerAdapter;
    private int position;
    private String accId = "";
    /**
     * type == 1   右上角显示    确定
     * type == 2   右上角显示    删除
     * */
    private String type = "";

    public static void start(Context context, int position, List<PhotoBean> photoList, String accId, String type) {
        Intent intent = new Intent();
        intent.setClass(context, LookPhotoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("accId", accId);
        intent.putExtra("type", type);
        ((Activity) context).startActivityForResult(intent, 10);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookphoto_activity_layout);
        ButterKnife.bind(this);

        mActivity = this;
        initData();
    }

    private void initData() {
        position = getIntent().getIntExtra("position",0);
        photoList = (List<PhotoBean>) getIntent().getSerializableExtra("photoList");
        accId = getIntent().getStringExtra("accId");
        type = getIntent().getStringExtra("type");
        pagerAdapter = new MyPagerAdapter(mActivity,photoList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
        tvTitle.setText(position + 1 + "/" + photoList.size());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(position + 1 + "/" + photoList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvDelete.setVisibility(NimUIKit.getAccount().equals(accId) ? View.VISIBLE : View.GONE);
        if (type.equals("1")){
            tvDelete.setText("确定");
        }else if (type.equals("2")){
            tvDelete.setText("删除");
        }
    }

    @OnClick({R.id.img_back, R.id.tv_Delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onFinish();
                break;
            case R.id.tv_Delete:
                if (type.equals("1")){
                    onFinish();
                }else if (type.equals("2")){
                    //删除
                    new XPopup.Builder(mActivity)
                            .dismissOnTouchOutside(false)
                            .asCustom(new MiddleDialog(mActivity, "提示", "确定要删除这张吗?", new MiddleDialog.Listener() {
                                @Override
                                public void onConfirmClickListener() {
                                    photoList.remove(mViewPager.getCurrentItem());
                                    pagerAdapter.notifyDataSetChanged();
                                    tvTitle.setText(mViewPager.getCurrentItem() + 1 + "/" + photoList.size());
                                    if (photoList.size() <= 0){
                                        onFinish();
                                    }
                                }

                                @Override
                                public void onCloseClickListener() {

                                }
                            }))
                            .show();
                }
                break;
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<PhotoBean> mData;

        public MyPagerAdapter(Context context ,List<PhotoBean> list) {
            mContext = context;
            mData = list;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PhotoBean photoBean = mData.get(position);
            View view = View.inflate(mContext, R.layout.album_detail_item_layout,null);
            ImageView img = view.findViewById(R.id.img_album_detail_item);
            final TextView tvBurnAfterReading = view.findViewById(R.id.tv_BurnAfterReading);
            tvBurnAfterReading.setVisibility(NimUIKit.getAccount().equals(accId) ? View.VISIBLE : View.GONE);
            if (photoBean.isBurnAfterReading()){
                //拿到初始图
                Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
                //处理得到模糊效果的图
                Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
                Glide.with(mContext).load(blurBitmap).into(img);
            }else {
                Glide.with(mContext).load(photoBean.getPhotoUrl()).into(img);
            }
            tvBurnAfterReading.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(photoBean.isBurnAfterReading() ? R.mipmap.selected_logo : R.mipmap.unselected_logo), null, null, null);
            tvBurnAfterReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoBean.isBurnAfterReading()){
                        photoBean.setBurnAfterReading(false);
                        tvBurnAfterReading.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.unselected_logo), null, null, null);
                    }else {
                        photoBean.setBurnAfterReading(true);
                        tvBurnAfterReading.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.selected_logo), null, null, null);
                    }
                    if (photoBean.isBurnAfterReading()){
                        //拿到初始图
                        Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
                        //处理得到模糊效果的图
                        Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
                        Glide.with(mContext).load(blurBitmap).into(img);
                    }else {
                        Glide.with(mContext).load(photoBean.getPhotoUrl()).into(img);
                    }
                }
            });
            view.findViewById(R.id.mRl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equals("2")){
                        onFinish();
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            //增加此函数为了支持ViewPager的刷新功能
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    private void onFinish(){
        Intent intent = new Intent();
        intent.putExtra("photoList", (Serializable) photoList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
