package com.yuanqi.hangzhou.imhookup.utils;

import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

/**
 * 该类是用于封装EventBus操作的工具类
 */

public class EventBusUtils {
    private static EventBus mEventBus;

    public static void init() {
        initInternal();
    }

    private static void initInternal() {
        mEventBus = EventBus.getDefault();
    }

    public static void register(Object subscriber) {
        mEventBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        mEventBus.unregister(subscriber);
    }

    public static void post(int id) {
        post(new CommonEvent(id));
    }

    public static void post(int id, Bundle data) {
        post(new CommonEvent(id, data));
    }

    public static void post(CommonEvent event) {
        mEventBus.post(event);
    }

    public static void postSticky(Object event) {
        mEventBus.postSticky(event);
    }

    /**
     * 通用事件类型，如无特殊情况请使用该事件类型发送事件
     * {@link CommonEvent#id}事件类型标识
     * {@link CommonEvent#data}事件附加数据
     * <p>
     * 注意：不要使用data传递过大的数据量
     */
    public static class CommonEvent {
        public int id;
        public Bundle data;

        public Intent intent;

        public CommonEvent() {
        }

        public CommonEvent(int id) {
            this.id = id;
        }

        public CommonEvent(int id, Bundle data) {
            this.id = id;
            this.data = data;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setData(Bundle data) {
            this.data = data;
        }
    }

    /**
     * 事件类型标识码统一在此处添加
     */
    public static final int EVENTDELIVERYTYPE_WXLOGINCOMPLETE = 1001;//微信登陆成功

}
