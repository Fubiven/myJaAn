package com.fzp.mystudyandroid.main;

import android.app.Application;
import android.content.Context;
import com.fzp.mystudyandroid.utils.PreCacheUtil;

/**
 * 应用程序界面
 * Created by Fuzp on 2018/4/8.
 */

public class MyApp extends Application {
    /**
     * 应用程序上下文
     */
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        initApp();

    }



    /**
     * 初始化应用程序
     */
    private void initApp() {
        PreCacheUtil.initPreCache(mContext, PreCacheUtil.BASICINFO);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mContext = getApplicationContext();
    }

}
