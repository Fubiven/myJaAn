package com.fzp.mystudyandroid.studyTest;

import android.content.Intent;
import android.media.DrmInitData;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.cnStudy.CNStudyListActivity;
import com.fzp.mystudyandroid.main.BaseActivity;
import com.fzp.mystudyandroid.main.HomeActivity;

/**
 * 学习模块测试主界面
 */
public class StudyTestActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout layLoadImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("学习测试");
        initData();
        initView();
        initEvent();


    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        layLoadImage.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化界面
     */
    private void initView() {
        layLoadImage = findViewById(R.id.layout_study_test);

    }

    /**
     * 返回子界面布局ID
     *
     * @return  返回视图
     */
    @Override
    protected int getLayoutID() {
        return R.layout.activity_study_test;
    }

    /**
     * 当某个视图被点击时调用
     *
     * @param v 被点击的视图
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.layout_study_test:
                intent = new Intent(StudyTestActivity.this, LoadImageActivity.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(StudyTestActivity.this, "未知控件", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
