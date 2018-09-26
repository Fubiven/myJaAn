package com.fzp.mystudyandroid.main;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.myAdapter.GuideViewPagerAdapter;
import com.fzp.mystudyandroid.myEvent.MyClickListener;
import com.fzp.mystudyandroid.utils.PreCacheUtil;
import com.fzp.mystudyandroid.utils.WindowImmersiveUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 导航页
 * 作者：Fuzp 2018.09.26
 */
public class GuideActivity extends AppCompatActivity {

    private View lastPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowImmersiveUtil.statusBarHide(this);
        setContentView(R.layout.activity_guide);
        initView();
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ImageView btnBegin = lastPage.findViewById(R.id.experience);
        btnBegin.setOnClickListener(onBeginClick);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.guide_page_first, null));
        views.add(inflater.inflate(R.layout.guide_page_second, null));
        lastPage = inflater.inflate(R.layout.guide_page_third, null);
        views.add(lastPage);
        GuideViewPagerAdapter pagerAdapter = new GuideViewPagerAdapter(views, this);
        ViewPager vpGuide = findViewById(R.id.vp_guide);
        vpGuide.setAdapter(pagerAdapter);

    }

    /**
     *  开始应用按钮监听事件
     */
    MyClickListener onBeginClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {
            PreCacheUtil.setValue(PreCacheUtil.PRE_ISFIRST, false);
            startActivity(new Intent(GuideActivity. this,LoginActivity.class));
            overridePendingTransition(R.anim.show, R.anim.hide);/* 淡入淡出平滑过渡 */
            GuideActivity.this.finish();
        }
    };


}
