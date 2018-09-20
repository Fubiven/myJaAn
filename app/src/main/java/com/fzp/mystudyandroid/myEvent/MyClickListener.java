package com.fzp.mystudyandroid.myEvent;

import android.view.View;

/**
 * 封装点击监听时间（防止短时间内多次响应点击）
 * Created by Fuzp on 2018/4/9.
 */

public abstract class MyClickListener implements View.OnClickListener{
    /**
     * 最小点击间隔
     */
    private static final int MIN_CLICK_DELAY_TIME = 500;
    /**
     * 上次点击时间毫秒值
     */
    private long lastClickTime;

    /**
     * 点击事件响应方法
     */
    protected abstract void myClick(View view);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();//本次点击时间毫秒值
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME){ //超过最小点击间隔
            lastClickTime = curClickTime;
            myClick(v);
        }

    }
}
