package com.fzp.mystudyandroid.cnStudy.buildFirstApp;

import android.os.Bundle;
import android.widget.TextView;

import com.fzp.mystudyandroid.main.BaseActivity;
import com.fzp.mystudyandroid.R;

public class DisplayMessageActivity extends BaseActivity {

    private String message = "";
    private TextView tvShowMsg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        setTitleBar("显示消息",null, 0, null);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_display_message;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        tvShowMsg = (TextView) this.findViewById(R.id.tv_msg);
        tvShowMsg.setText(message);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initParam();
    }

    /**
     * 初始化参数
     */
    private void initParam() {
        message = getIntent().getStringExtra(InputMesageActivity.EXTRA_MESSAGE);

    }


}
