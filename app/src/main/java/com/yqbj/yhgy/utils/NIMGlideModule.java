package com.yqbj.yhgy.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by huangjun on 2017/4/1.
 */
public class NIMGlideModule implements GlideModule {
    private static final String TAG = "NIMGlideModule";

    private static final int M = 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 256 * M;

    /**
     * ************************ Memory Cache ************************
     */

    static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * ************************ GlideModule override ************************
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // sdcard/Android/data/com.netease.wulewan.haichui/glide
        final String cachedDirName = "glide";
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cachedDirName, MAX_DISK_CACHE_SIZE));
        LogUtil.i(TAG, "NIMGlideModule apply options, disk cached path=" + context.getExternalCacheDir() + File.pathSeparator + cachedDirName);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
