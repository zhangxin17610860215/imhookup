package com.yuanqi.hangzhou.imhookup.utils;

import java.io.File;

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);

    void onDownLoadFailed();
}
