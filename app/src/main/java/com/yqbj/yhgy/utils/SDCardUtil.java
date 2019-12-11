package com.yqbj.yhgy.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.yqbj.yhgy.config.Constants;

import java.io.File;
import java.lang.reflect.Field;

public class SDCardUtil {

    /**
     * 用反射生成 SharedPreferences
     * @param context
     * @param
     * @param fileName 文件名,不需要 '.xml' 后缀
     * @return
     */
    public static SharedPreferences getMySharedPreferences(Context context, String fileName) {
        try {
            // 获取 ContextWrapper对象中的mBase变量。该变量保存了 ContextImpl 对象
            Field field_mBase = ContextWrapper.class.getDeclaredField("mBase");
            field_mBase.setAccessible(true);
            // 获取 mBase变量
            Object obj_mBase = field_mBase.get(context);
            // 获取 ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            Field field_mPreferencesDir = obj_mBase.getClass().getDeclaredField("mPreferencesDir");
            field_mPreferencesDir.setAccessible(true);
            // 创建自定义路径
            String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + Constants.APP_NAME;
            File file = new File(FILE_PATH);
            // 修改mPreferencesDir变量的值
            field_mPreferencesDir.set(obj_mBase,file);
            // 返回修改路径以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml
            Log.e("SDCardUtil","getMySharedPreferences filep="+file.getAbsolutePath()+"| fileName="+fileName);
            return context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.e("SDCardUtil","getMySharedPreferences end filename="+fileName);
        // 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
        return context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

}
