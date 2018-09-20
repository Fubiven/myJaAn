package com.fzp.mystudyandroid.cnStudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fzp.mystudyandroid.main.BaseActivity;
import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.cnStudy.buildFirstApp.InputMesageActivity;

public class CNStudyListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 主界面布局
     */
    private TextView tvSimpleInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        setTitleBar("中文网学习教程",null, 0, null);

    }

    /**
     * 初始化时间
     */
    private void initEvent() {
        tvSimpleInterface.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        tvSimpleInterface = container.findViewById(R.id.tv_toSimpleInterface);
    }



    @Override
    protected int getLayoutID() {
        return R.layout.activity_cnstudy_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_toSimpleInterface:
                Intent toInputMsg = new Intent(
                        CNStudyListActivity.this, InputMesageActivity.class);
                startActivity(toInputMsg);
                break;

             default:
                 Toast.makeText(CNStudyListActivity.this, "未知学习步骤", Toast.LENGTH_SHORT).show();
             break;
        }
    }
}
