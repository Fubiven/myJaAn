package com.fzp.mystudyandroid.main;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.utils.PreCacheUtil;
import com.fzp.mystudyandroid.utils.WindowImmersiveUtil;
import com.fzp.mystudyandroid.utils.db.DBHelper;
import com.fzp.mystudyandroid.utils.db.DBOperate;
import com.fzp.mystudyandroid.views.aboutDialog.BaseDialog;
import com.fzp.mystudyandroid.views.aboutDialog.PromptDialog;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 双击退出允许时长
     */
    private final static int DOUBLE_CLICK_EXIT_TIME = 1000;
    /**
     * 数据库辅助类实例
     */
    private DBHelper mDBHelper = null;
    /**
     * 数据库操作对象
     */
    public DBOperate mDBOperate = null;

    /**
     * 标题栏标题
     */
    private TextView tvTitle = null;

    /**
     * 标题栏左按钮
     */
    private LinearLayout layoutLeft = null;
    /**
     * 标题栏左按钮图标
     */
    private ImageView ivLeft = null;

    /**
     * 标题栏右按钮
     */
    private LinearLayout layoutRight = null;
    /**
     * 标题栏右按钮图标
     */
    private ImageView ivRight = null;

    /**
     * 标题栏布局
     */
    private LinearLayout layoutTitleBar = null;

    /**
     * 子界面布局容器
     */
    public FrameLayout container = null;
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
//        WindowImmersiveUtil.statusBarTintColor(this, getResources().getColor(R.color.colorPrimary));
//        WindowImmersiveUtil.statusBarHide(this);
//        WindowImmersiveUtil.actionBarHide(this);
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        setContentView(view);//填充界面布局
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }
        bindView();
        WindowImmersiveUtil.setTransparentStatusBar(this, layoutTitleBar);
        initDefaultView(getLayoutID());
        setTitleBarVisibility(true);//默认显示标题栏
        setTitle("标题");//默认标题
        setEvent();
    }

    /**
     * 初始化数据库相关工具
     */
    private void initDB() {
        mDBHelper = DBHelper.getInstance(this);
        mDBOperate = DBOperate.getInstance(this);
    }

    /**
     * 初始化事件
     */
    protected void setEvent() {
        layoutLeft.setOnClickListener(onBackPressListener);
    }

    /**
     * 设置标题内容
     *
     * @param titleStr 标题文本
     */
    public void setTitle(String titleStr) {
        if (!TextUtils.isEmpty(titleStr)) {
            tvTitle.setText(titleStr);
        }
    }

    /**
     * 设置是否显示标题栏
     *
     * @param ifShow 是否显示
     */
    public void setTitleBarVisibility(boolean ifShow) {
        layoutTitleBar.setVisibility(ifShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题看
     *
     * @param titleStr   标题名称            （不需要则给“”）
     * @param leftClick  左键事件监听事件    （不需要则给null）
     * @param imageRes   右键按钮图标        （不需要则给0）
     * @param rightClick 右键按钮监听事件    （没有则给null）
     */
    public void setTitleBar(String titleStr, View.OnClickListener leftClick,
                            @DrawableRes int imageRes, View.OnClickListener rightClick) {
        /* 设置标题 */
        if (!TextUtils.isEmpty(titleStr)) {
            tvTitle.setText(titleStr);
        }
        /* 设置左边按钮监听 */
        if (leftClick != null) {
            layoutLeft.setOnClickListener(leftClick);
        }

        /* 设置右边按钮 */
        if (imageRes != 0) {
            layoutRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(imageRes);

            //右边按钮监听事件
            if (rightClick != null) {
                layoutRight.setOnClickListener(rightClick);
            }
        }

    }


    /**
     * 初始化布局
     */
    private void bindView() {
        tvTitle = this.findViewById(R.id.tv_title_title);
        layoutLeft = this.findViewById(R.id.layout_title_left);
        ivLeft = this.findViewById(R.id.iv_title_left);
        layoutRight = this.findViewById(R.id.layout_title_right);
        ivRight = this.findViewById(R.id.iv_title_right);
        layoutTitleBar = this.findViewById(R.id.layout_title_bar);
        container = this.findViewById(R.id.fl_child_container);

    }


    /**
     * 加载子类Activity的布局
     *
     * @param layoutResID 子类Activity布局id
     */
    private void initDefaultView(int layoutResID) {
        View childView = getLayoutInflater().inflate(layoutResID, null);
        container.addView(childView, 0);
    }


    /**
     * 返回子界面布局ID
     *
     * @return
     */
    protected abstract int getLayoutID();

    /**
     * 顶部栏返回按钮监听事件
     */
    private View.OnClickListener onBackPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * 程序注销
     */
    public void logOff(){
        PreCacheUtil.setValue(PreCacheUtil.PRE_ISLOGIN, false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        mDBHelper.closeDB();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(HomeActivity.TAG_EXIT, true);
        startActivity(intent);
    }

    /**
     * 双击回退键退出程序
     */
    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (isExit) {
            new PromptDialog(this, "友情提示", "是否退出程序？", 2,
                    new BaseDialog.CancelListener() {
                        @Override
                        public void onClicked() {
                            logOff();
                        }
                    }, "注销", null, "",
                    new BaseDialog.ConfirmListener() {
                        @Override
                        public void onClicked() {
                            exitApp();
                        }
                    }, "确定").show();
        } else {
            isExit = true;
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, DOUBLE_CLICK_EXIT_TIME);//指定时间内未再次点击则自动取消点击记忆
        }
    }

}
