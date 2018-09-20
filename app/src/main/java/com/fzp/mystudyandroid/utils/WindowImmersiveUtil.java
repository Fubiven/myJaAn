package com.fzp.mystudyandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.fzp.mystudyandroid.R;

/**
 * 沉浸式界面工具
 * Created by Fuzp on 2018/3/3.
 */

public class WindowImmersiveUtil {

    /**
     * 设置沉浸式状态栏（状态栏与ActionBar颜色相同）<br/>
     * <b>兼容安卓4.4以上版本</b>
     * @param activity      活动界面
     * @param color         状态栏色值
     */
    public static void statusBarTintColor(Activity activity, @ColorInt int color){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){  //安卓5.0以上
            activity.getWindow().setStatusBarColor(color);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //安卓4.4以上
            /* 透明状态栏 */
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup androidContainer = activity.findViewById(android.R.id.content);
            /*  留出高度 setFitsSystemWindows true代表会调整布局，会把状态栏的高度留出来 */
            View contentView = androidContainer.getChildAt(0);
            if(contentView != null){
                contentView.setFitsSystemWindows(true);
            }
            /* 在原来位置上添加状态栏 */
            View statusBarView = createStatusBarView(activity);
            androidContainer.addView(statusBarView, 0);
            statusBarView.setBackgroundColor(color);
        }
    }

    /**
     * 创建一个需要填充状态栏布局
     * @param activity  活动界面
     * @return  statusBarView（状态栏）
     */
    private static View createStatusBarView(Activity activity){
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams statusBarParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(statusBarParams);
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     * @param context   界面上下文
     * @return  状态栏高度值（px）
     */
    private static int getStatusBarHeight(Context context){
        int height = 0;
        /* 获状态栏资源ID */
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     *  设置导航栏（NavigationBar）显示状态
     * @param activity  活动界面
     * @param ifShow   是否显示
     */
    public  static void setNavigationBar(Activity activity,boolean ifShow){
        /* 获取window中最顶级视图 */
        View decorView  = activity.getWindow().getDecorView();
        if(!ifShow){  // 隐藏导航栏NavigationBar
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
    }

    /**
     * 设置透明状态栏
     * @param activity 活动界面
     * @param topView  最顶部视图（可用于留出状态栏高度）
     */
    public static void setTransparentStatusBar(Activity activity, View topView){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(
                    activity.getResources().getColor(R.color.colorHalfTransparentGray));
            if(topView != null){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topView.getLayoutParams();
                params.setMargins(0,
                        WindowImmersiveUtil.getStatusBarHeight(activity), 0, 0);
            }
        }
    }

    /**
     * 隐藏状态栏（StatusBar）
     * @param activity  活动界面
     */
    public static void statusBarHide(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){  //安卓5.0以上
           /* 方案一 （下拉会出现状态栏，但不会再次隐藏）*/
//            View decorView = activity.getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);

//          /* 方案二 （下拉会出现状态栏，过一段时间后自动再次隐藏）*/
            Window window = activity.getWindow();
            /* 隐藏状态栏 */
                //定义全屏参数
            int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
            //设置当前窗体为全屏显示
            window.setFlags(flag, flag);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ //安卓4.4以上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 隐藏工具栏（StatusBar）
     * @param activity  活动界面
     */
    public static void actionBarHide(Activity activity){
        if(activity instanceof AppCompatActivity){ /* AppCompatActivity隐藏标题栏 */
            ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
            if (actionBar != null){
                actionBar.hide();
            }
        }else{   /* Activity隐藏标题栏  */
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }



}
