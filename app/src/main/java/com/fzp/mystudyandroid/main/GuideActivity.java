package com.fzp.mystudyandroid.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.utils.WindowImmersiveUtil;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowImmersiveUtil.statusBarHide(this);
        setContentView(R.layout.activity_guide);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

    }
}
