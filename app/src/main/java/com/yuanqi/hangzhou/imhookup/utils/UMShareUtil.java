package com.yuanqi.hangzhou.imhookup.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class UMShareUtil {

    /**
     * 分享文本内容
     * */
    public static void shareText(Activity mActivity, String url, String title, int imageId, String content, ShareListener listener){

        UMShareListener umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                // 分享开始的回调
                listener.onStart();
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(mActivity,"分享成功啦",Toast.LENGTH_LONG).show();
                listener.onResult();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(mActivity,"分享失败啦",Toast.LENGTH_LONG).show();
                listener.onError();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(mActivity,"分享取消啦",Toast.LENGTH_LONG).show();
                listener.onCancel();
            }
        };

        UMImage image = new UMImage(mActivity, imageId);//分享图标
        final UMWeb web = new UMWeb(url); //切记切记 这里分享的链接必须是http开头
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);//描述
        new ShareAction(mActivity)
                .setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (share_media == SHARE_MEDIA.WEIXIN) {
                            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN)
                                    .withMedia(web)
                                    .setCallback(umShareListener)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .withMedia(web)
                                    .setCallback(umShareListener)
                                    .share();
                        }
                    }
                }).open();

    }

    public interface ShareListener{
        void onStart();
        void onResult();
        void onError();
        void onCancel();
    }

}
