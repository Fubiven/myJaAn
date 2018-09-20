package com.fzp.mystudyandroid.myActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.myFragment.AtricleFragment;

public class FlexibleActivity extends AppCompatActivity implements AtricleFragment.OnContentClickListener{

    public static  final  String FLEXIBLEACTIVITY_ARGS_MSG = "Args_string_msg";
    private Button btnChange = null;
    private FragmentManager mFragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible);
        mFragmentManager = this.getFragmentManager();

        initView();
        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        btnChange.setOnClickListener(onChangeClick);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        btnChange = (Button) this.findViewById(R.id.btn_change);
        AtricleFragment firstFragment = AtricleFragment.createInstance("第一个Fragment");
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment_container, firstFragment).commit();
    }


    /**
     * 切换卡片点击事件
     */
    private View.OnClickListener onChangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AtricleFragment changeFragment = AtricleFragment.createInstance("第二个Fragment");
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            Bundle args = changeFragment.getArguments();
            args.putString(FLEXIBLEACTIVITY_ARGS_MSG, "父activity传给第二个Fragment的内容");
            changeFragment.setArguments(args);
            mFragmentTransaction.replace(R.id.fragment_container,changeFragment);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        }
    };

    @Override
    public void onClick(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
