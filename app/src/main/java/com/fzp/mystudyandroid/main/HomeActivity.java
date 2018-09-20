package com.fzp.mystudyandroid.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.cnStudy.CNStudyListActivity;
import com.fzp.mystudyandroid.studyTest.StudyTestActivity;
import com.fzp.mystudyandroid.views.aboutDialog.BaseDialog;
import com.fzp.mystudyandroid.views.aboutDialog.PromptDialog;
import com.fzp.mystudyandroid.workTest.WorkTestActivity;


public class HomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 退出程序标识
     */
    public static final String TAG_EXIT = "exit";
    /**
     * 前往安卓中文教学按钮
     */
    private TextView tvToCNStudy = null;
    /**
     * 前往工作测试
     */
    private TextView tvToWorkTest = null;
    /**
     * 前往学习测试
     */
    private TextView tvToStudyTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        setTitleBar("首页", null, R.mipmap.icon_add_press, addSomeThing);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        tvToCNStudy.setOnClickListener(this);
        tvToWorkTest.setOnClickListener(this);
        tvToStudyTest.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        tvToCNStudy = container.findViewById(R.id.tv_toCNStudy);
        tvToWorkTest= container.findViewById(R.id.tv_toWorkTest);
        tvToStudyTest= container.findViewById(R.id.tv_toStudyTest);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }


    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()) {
            case R.id.tv_toCNStudy:
                intent = new Intent(HomeActivity.this, CNStudyListActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_toWorkTest:
                intent = new Intent(HomeActivity.this, WorkTestActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_toStudyTest:
                intent = new Intent(HomeActivity.this, StudyTestActivity.class);
                startActivity(intent);
                break;

            default:
                Toast.makeText(HomeActivity.this, "步骤异常", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private View.OnClickListener addSomeThing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new PromptDialog(HomeActivity.this, "提示", "添加了什么东东！",
                    1, new BaseDialog.CancelListener() {
                @Override
                public void onClicked() {
                    Toast.makeText(HomeActivity.this, "确实添加了！", Toast.LENGTH_SHORT).show();
                }
            }, "取消", null, "", null, "").show();
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                finish();
            }
        }

    }
}
