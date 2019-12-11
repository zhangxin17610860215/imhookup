package com.yqbj.yhgy.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.nos.NosService;
import com.yqbj.yhgy.R;
import com.yqbj.yhgy.base.BaseActivity;
import com.yqbj.yhgy.utils.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.netease.nim.uikit.business.session.constant.Extras.EXTRA_FILE_PATH;

/**
 * 查看图片
 * */
public class SeePictureActivity extends BaseActivity {

    private Activity mActivity;

    private Handler handler;
    private int position;
    private List<String> urlList = new ArrayList<>();
    private ViewPager mViewPager;
    private MyPagerAdapter pagerAdapter;

    protected CustomAlertDialog alertDialog;
    private String indexUrl = "";
    private int pos;
    private String accId = "";

    public static void start(Context context, int position, List<String> urlList, String accId) {
        Intent intent = new Intent();
        intent.setClass(context, SeePictureActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("urlList", (Serializable) urlList);
        intent.putExtra("accId",accId);
        ((Activity) context).startActivityForResult(intent, 10);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seepicture_activity_layout);
        mActivity = this;

        position = getIntent().getIntExtra("position",0);
        urlList = (List<String>) getIntent().getSerializableExtra("urlList");
        accId = getIntent().getStringExtra("accId");
        initView();
    }

    private void initView() {
        alertDialog = new CustomAlertDialog(this);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        for (String url : urlList){
            if (StringUtil.isNotEmpty(url) && url.equals("add")){
                urlList.remove(urlList.size() - 1);
            }
        }
        pagerAdapter = new MyPagerAdapter(mActivity,urlList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);

        findViewById(R.id.img_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = mViewPager.getCurrentItem();
                if (null != urlList || urlList.size() > 0){
                    String url = urlList.get(pos);
                    onLongClickAction(url);
                }
            }
        });
    }

    public class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private List<String> mData;

        public MyPagerAdapter(Context context ,List<String> list) {
            mContext = context;
            mData = list;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = View.inflate(mContext, R.layout.album_detail_item_layout,null);
            ImageView img = (ImageView) view.findViewById(R.id.img_album_detail_item);
            Glide.with(mContext).load(mData.get(position)).into(img);

            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pos = position;
                    onLongClickAction(mData.get(position));
                    return true;
                }
            });

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFinish();
                }
            });

            view.findViewById(R.id.mRl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFinish();
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

    private void onFinish(){
        Intent intent = new Intent();
        intent.putExtra("urlList", (Serializable) urlList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onLongClickAction(final String url) {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
            return;
        }
        handler = new Handler();
        indexUrl = url;
        alertDialog.clearData();
        if (accId.equals(NimUIKit.getAccount())){
            //是本人
            alertDialog.addItem("更换图片", new CustomAlertDialog.onSeparateItemClickListener() {

                @Override
                public void onClick() {
                    upDatePicture();
                }
            });
        }
        alertDialog.addItem("取消", new CustomAlertDialog.onSeparateItemClickListener() {

            @Override
            public void onClick() {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void upDatePicture() {
        //更换图片
        if (null != alertDialog){
            alertDialog.dismiss();
        }
//        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
//        option.titleResId = R.string.set_album_image;
//        option.crop = true;
//        option.multiSelect = false;
//        option.cropOutputImageWidth = 720;
//        option.cropOutputImageHeight = 720;
//        PickImageHelper.pickImage(context, 101, option);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            showProgress(false);
            String path = data.getStringExtra(EXTRA_FILE_PATH);
            upDataAlbum(path);
        }
    }

    private void upDataAlbum(String path) {
        if (StringUtil.isEmpty(path)){
            return;
        }
        File file = new File(path);
        if (file == null) {
            return;
        }
        AbortableFuture<String> upload = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        upload.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int i, final String url, Throwable throwable) {
                Log.e("TAG",">>>>>>>>" + url);
                if (StringUtil.isEmpty(url)){
                    return;
                }
                if (urlList.size() == 1){
                    urlList.clear();
                    urlList.add(url);
                }else {
                    if (urlList.contains(indexUrl)){
                        Iterator<String> iterator = urlList.iterator();
                        while (iterator.hasNext()){
                            String next = iterator.next();
                            if (indexUrl.equals(next)){
                                iterator.remove();
                                urlList.add(pos,url);
                                break;
                            }
                        }
                    }
                }
                Gson gson = new Gson();
                String cardUrlInfo = gson.toJson(urlList);
                Log.e("TAG",">>>>>>>" + cardUrlInfo);
//                UserApi.upDateUserBusinessCard(cardUrlInfo, context, new requestCallback() {
//                    @Override
//                    public void onSuccess(int code, Object object) {
//                        dismissProgress();
//                        if (code == Constants.SUCCESS_CODE){
//                            pagerAdapter.notifyDataSetChanged();
//                            toast("上传成功");
//                        }else {
//                            toast((String) object);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(String errMessage) {
//                        dismissProgress();
//                        toast(errMessage);
//                    }
//                });
            }
        });
    }

}
