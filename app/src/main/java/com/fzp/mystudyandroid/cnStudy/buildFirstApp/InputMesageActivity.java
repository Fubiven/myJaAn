package com.fzp.mystudyandroid.cnStudy.buildFirstApp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fzp.mystudyandroid.main.BaseActivity;
import com.fzp.mystudyandroid.R;

public class InputMesageActivity extends BaseActivity {
    public static final String EXTRA_MESSAGE = "com.example.myStudyAndroid.MESSAGE";
    private EditText mEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //TODO 可在此处对高级版本进行特殊处理
            initView();
        } else {
            initView();
        }
        setTitleBar("输入消息",null, 0, null);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mEditText = (EditText) this.findViewById(R.id.editText);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = mEditText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
