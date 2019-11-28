package com.netease.nim.uikit.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.business.session.activity.WatchMessagePictureActivity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class GlideUtil {
    public static void loadCircular(final Context context, String url, final ImageView img, int placeholder) {
        if(null == context){
            return;
        }
        if (null == url){
            return;
        }
        Glide.with(context).asBitmap()
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .into(new BitmapImageViewTarget(img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        img.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public static void loadIMGFileToWatch(final String url, final Context context) {

        class Daemon implements Runnable {
            public void run() {
                File file = null;
                try {
                    file = Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    IMMessage message = MessageBuilder.createImageMessage("0", SessionTypeEnum.P2P, file, file.getName());
                    WatchMessagePictureActivity.start(context, message,false);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        Thread t = new Thread(new Daemon());
        t.setDaemon(true);
        t.start();

    }

    /**
     * 加载带有占位图的函数
     * */
    public static void loadHavePlaceholderImageView(Context context, String url, int placeholder, ImageView view){
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder);
        Glide.with(context).asBitmap()
                .load(url)
                .apply(requestOptions)
                .into(view);
    }

}
