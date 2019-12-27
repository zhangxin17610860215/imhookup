package com.yqbj.yhgy.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.NoDoubleClickUtils;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.bean.PhotoBean;
import com.yqbj.yhgy.login.VipCoreActivity;
import com.yqbj.yhgy.utils.ImageFilter;
import com.yqbj.yhgy.view.MiddleDialog;
import com.yqbj.yhgy.view.PaySelect;

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
    private boolean isShowButton;       //是否显示底部阅后即焚和红包相册按钮(相当于是否是本人进来此页面)
    /**
     * type == 1   右上角显示    确定
     * type == 2   右上角显示    删除
     * */
    private String type = "";

    private View view;
    private PhotoBean bean;
    private LinearLayout llBurnedDown;
    private TextView tvGoVIP;
    private TextView tvWatermark;
    private ImageView imgHeader;
    private RelativeLayout rlRedenvelopephotosView;
    private Handler readyHandler = new Handler();
    private Handler endHandler = new Handler();

    public static void start(Context context, int position, List<PhotoBean> photoList, String accId, String type, boolean isShowButton) {
        Intent intent = new Intent();
        intent.setClass(context, LookPhotoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("accId", accId);
        intent.putExtra("type", type);
        intent.putExtra("isShowButton", isShowButton);
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
        isShowButton = getIntent().getBooleanExtra("isShowButton",false);
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
        tvDelete.setVisibility(isShowButton ? NimUIKit.getAccount().equals(accId) ? View.VISIBLE : View.GONE : View.GONE);
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
            view = View.inflate(mContext, R.layout.album_detail_item_layout,null);
            ImageView img = view.findViewById(R.id.img_album_detail_item);
            LinearLayout ll_burnedDown = view.findViewById(R.id.ll_burnedDown);
            RelativeLayout rl_redenvelopephotos_View = view.findViewById(R.id.rl_redenvelopephotos_View);
            TextView tv_addTime = view.findViewById(R.id.tv_addTime);
            TextView tv_goVIP = view.findViewById(R.id.tv_goVIP);
            TextView tv_sendRedPackage = view.findViewById(R.id.tv_sendRedPackage);
            TextView tv_Watermark = view.findViewById(R.id.tv_watermark);
            final TextView tvBurnAfterReading = view.findViewById(R.id.tv_BurnAfterReading);
            final TextView tvRedEnvelopePhotos = view.findViewById(R.id.tv_RedEnvelopePhotos);
            tvBurnAfterReading.setVisibility(isShowButton ? NimUIKit.getAccount().equals(accId) ? View.VISIBLE : View.GONE : View.GONE);
            tvRedEnvelopePhotos.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
            rl_redenvelopephotos_View.setVisibility(!isShowButton && photoBean.isRedEnvelopePhotos() ? View.VISIBLE : View.GONE);
            //拿到初始图
            Bitmap bmp= BitmapFactory.decodeFile(photoBean.getPhotoUrl());
            //处理得到模糊效果的图
            Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
            if (photoBean.isBurnAfterReading() || photoBean.isRedEnvelopePhotos()){
                Glide.with(mContext).load(isShowButton ? photoBean.getPhotoUrl() : blurBitmap).into(img);
                tv_Watermark.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
                if (photoBean.isRedEnvelopePhotos() && photoBean.isRedEnvelopePhotosPaid()){
                    //红包照片而且已付过费
                    Glide.with(mContext).load(photoBean.getPhotoUrl()).into(img);
                    tv_Watermark.setVisibility(View.VISIBLE);
                    rl_redenvelopephotos_View.setVisibility(View.GONE);
                }
            }else {
                Glide.with(mContext).load(photoBean.getPhotoUrl()).into(img);
                tv_Watermark.setVisibility(View.VISIBLE);
            }
            if (photoBean.isBurnedDown()){
                if (photoBean.isBurnAfterReading() && photoBean.isRedEnvelopePhotos() && photoBean.isRedEnvelopePhotosPaid()){
                    //阅后即焚而且是红包照片而且已付过费而且没有被焚毁
                    Glide.with(mContext).load(isShowButton ? photoBean.getPhotoUrl() : blurBitmap).into(img);
                    tv_Watermark.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
                    rl_redenvelopephotos_View.setVisibility(View.GONE);
                }
                ll_burnedDown.setVisibility(!isShowButton ? View.VISIBLE : View.GONE);
                tv_goVIP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VipCoreActivity.start(mActivity);
                    }
                });
            }
            tvBurnAfterReading.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(photoBean.isBurnAfterReading() ? R.mipmap.selected_logo : R.mipmap.unselected_logo), null, null, null);
            tvRedEnvelopePhotos.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(photoBean.isRedEnvelopePhotos() ? R.mipmap.selected_logo : R.mipmap.unselected_logo), null, null, null);

            tv_sendRedPackage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imgHeader = img;
                    rlRedenvelopephotosView = rl_redenvelopephotos_View;
                    llBurnedDown = ll_burnedDown;
                    tvGoVIP = tv_goVIP;
                    tvWatermark = tv_Watermark;
                    bean = photoBean;
                    showPayMode(view,"3.00");
                }
            });

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
                }
            });
            tvRedEnvelopePhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoBean.isRedEnvelopePhotos()){
                        photoBean.setRedEnvelopePhotos(false);
                        tvRedEnvelopePhotos.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.unselected_logo), null, null, null);
                    }else {
                        photoBean.setRedEnvelopePhotos(true);
                        tvRedEnvelopePhotos.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.selected_logo), null, null, null);
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
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!isShowButton && photoBean.isBurnAfterReading()){
                        if (photoBean.isBurnedDown()){
                            return false;
                        }
                        imgHeader = img;
                        llBurnedDown = ll_burnedDown;
                        tvGoVIP = tv_goVIP;
                        tvWatermark = tv_Watermark;
                        bean = photoBean;
                        showProgress(false);
                        readyHandler.postDelayed(readyShow, 2000);
                        return true;
                    }else {
                        return false;
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

    /**
     * 显示支付方式弹窗
     * */
    private void showPayMode(View v, String amount) {
        final PaySelect paySelect = new PaySelect(mActivity,amount,"红包照片",amount,1);
        new XPopup.Builder(mActivity)
                .atView(v)
                .asCustom(paySelect)
                .show();
        paySelect.setOnClickListenerOnSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即支付
                PaySelect.SelectPayType type = paySelect.getCurrSeletPayType();
                int payType = 1;
                switch (type) {
                    case ALI:
                        //支付宝支付
                        payType = 3;
                        break;
                    case WCHAT:
                        //微信支付
                        payType = 2;
                        break;
                    case WALLET:
                        //钱包支付
                        payType = 1;
                        break;
                }
                if (!NoDoubleClickUtils.isDoubleClick(2000)){
//                    getRedPageId(amount,payType);
                    toast(payType == 3 ? "支付宝支付" : "微信支付");
                    bean.setRedEnvelopePhotosPaid(true);
                    paySelect.dismiss();
                    if (!isShowButton && bean.isBurnAfterReading()){
                        if (bean.isBurnedDown()){
                            return;
                        }
                        rlRedenvelopephotosView.setVisibility(View.GONE);
                        showProgress(false);
                        readyHandler.postDelayed(readyShow, 2000);

                    }else if (!isShowButton && !bean.isBurnAfterReading()){
                        rlRedenvelopephotosView.setVisibility(View.GONE);
                        Glide.with(mActivity).load(bean.getPhotoUrl()).into(imgHeader);
                        tvWatermark.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
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

    Runnable readyShow = new Runnable() {
        @Override
        public void run() {
            dismissProgress();
            if (bean.isRedEnvelopePhotos() && !bean.isRedEnvelopePhotosPaid()){
                toast("图片不存在");
            }else {
                bean.setBurnedDown(true);
                Glide.with(mActivity).load(bean.getPhotoUrl()).into(imgHeader);
                tvWatermark.setVisibility(View.VISIBLE);
                endHandler.postDelayed(endShow, 2000);
            }
        }
    };

    Runnable endShow = new Runnable() {
        @Override
        public void run() {
            //拿到初始图
            Bitmap bmp= BitmapFactory.decodeFile(bean.getPhotoUrl());
            //处理得到模糊效果的图
            Bitmap blurBitmap = ImageFilter.blurBitmap(mActivity, bmp, 25f);
            Glide.with(mActivity).load(blurBitmap).into(imgHeader);
            tvWatermark.setVisibility(View.GONE);
            llBurnedDown.setVisibility(View.VISIBLE);
            tvGoVIP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VipCoreActivity.start(mActivity);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        readyHandler.removeCallbacks(readyShow);
        endHandler.removeCallbacks(endShow);
    }
}
