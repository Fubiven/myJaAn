package com.fzp.mystudyandroid.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

/**
 * 全局缓存键值对操作类
 * Created by Administrator on 2018/4/8.
 */

public class PreCacheUtil {
    final static String Tag = PreCacheUtil.class.getName();
    private static SharedPreferences mPreCacheUtil = null;
    private static SharedPreferences.Editor mEditor = null;
    /**
     * prefs所在内部文件夹名称
     */
    public static final String PREFS_FOLDER = "shared_prefs";
    /**
     * 文件名称
     */
    public static final String BASICINFO = "BasicInfo";

    /**
     * 首次运行程序
     */
    public static final String PRE_ISFIRST = "isFirst";

    /**
     * 登录状态
     */
    public static final String PRE_ISLOGIN = "isLogin";
    /**
     * 用户名
     */
    public static final String PRE_USERNAME = "UserName";
    /**
     * 登录密码
     */
    public static final String PRE_PSW = "password";


    /**
     * 缓存数据值
     * @param key       键
     * @param value     值
     */
    public static void setValue(String key, Object value){
        if(value instanceof  String){
            mEditor.putString(key, (String) value);
        }else if (value instanceof Boolean){
            mEditor.putBoolean(key, (Boolean) value);
        }else if (value instanceof Integer){
            mEditor.putInt(key, (Integer) value);
        }else if (value instanceof Float){
            mEditor.putFloat(key, (Float) value);
        }else if (value instanceof Long){
            mEditor.putLong(key, (Long) value);
        }else{
            Log.e(Tag, "SharedPreferences 不支持此类数据的缓存");
        }

        mEditor.commit();
    }

    /**
     * 获取字符串类型缓存值
     * @param key                键
     * @param defaultValue      默认值
     * @return                   缓存数据
     */
    public static String getString(String key, String defaultValue){
        return mPreCacheUtil.getString(key, defaultValue);
    }

    /**
     * 获取布尔类型缓存值
     * @param key                键
     * @param defaultValue      默认值
     * @return                   缓存数据
     */
    public static boolean getBoolean(String key, boolean defaultValue){
        return mPreCacheUtil.getBoolean(key, defaultValue);
    }


    /**
     * 清空共享缓存
     */
    public static void clearCache(){
        if (mEditor != null){
            mEditor.clear().commit();
        }
    }

    /**
     * 初始化共享缓存实例
     * @param context   所使用上下文
     * @param fileName  文件名
     */
    @SuppressLint("CommitPrefEdits")
    public static  void initPreCache(Context context, String fileName){
        mPreCacheUtil = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        mEditor = mPreCacheUtil.edit();
    }


}
