package com.fzp.mystudyandroid.main;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.utils.PreCacheUtil;
import com.fzp.mystudyandroid.utils.WindowImmersiveUtil;
import static com.fzp.mystudyandroid.utils.PreCacheUtil.*;

public class StartActivity extends AppCompatActivity  {

    /**
     * 启动页睡眠时间
     */
    private static final int SLEEP_TIME = 1000;

    /**
     * 跳转到导航界面标识
     */
    private static final int TO_GUIDE = 100000;
    /**
     * 跳转到登录界面标识
     */
    private static final int TO_LOGIN = 100001;
    /**
     * 跳转到主界面标识
     */
    private static final int TO_HOME = 100002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowImmersiveUtil.statusBarHide(this);
        setContentView(R.layout.activity_start);
        StartHandle handler = new StartHandle();
        if(!PreCacheUtil.getBoolean(PRE_ISFIRST, true)){
            if (PreCacheUtil.getBoolean(PRE_ISLOGIN, false)){ /* 已登录 */
                handler.sendEmptyMessage(TO_HOME);
            }else{ /* 未登录 */
                handler.sendEmptyMessageDelayed(TO_LOGIN, SLEEP_TIME);
            }
        }else{ /* 首次启动 */
            handler.sendEmptyMessageDelayed(TO_GUIDE, SLEEP_TIME);
        }
    }

    /**
     * 界面跳转控制句柄
     */
    @SuppressLint("HandlerLeak")
    private class StartHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what){
                case TO_GUIDE:
                    intent = new Intent(StartActivity.this, GuideActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.show, R.anim.hide);/* 淡入淡出平滑过渡 */
                    break;
                case TO_LOGIN:
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.show, R.anim.hide);/* 淡入淡出平滑过渡 */
                    break;
                case TO_HOME:
                    intent = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            finish();
        }
    }
}


