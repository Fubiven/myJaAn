package com.fzp.mystudyandroid.main;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.fzp.mystudyandroid.utils.FileUtil;
import com.fzp.mystudyandroid.utils.PermissionUtils;
import com.fzp.mystudyandroid.utils.PreCacheUtil;
import com.fzp.mystudyandroid.utils.db.DBHelper;
import com.fzp.mystudyandroid.views.aboutDialog.PromptDialog;

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
        DBHelper.initDBHelper(mContext);
    }

}
