package com.fzp.mystudyandroid.myFragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.myActivity.FlexibleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AtricleFragment extends Fragment {
    OnContentClickListener mCallBack;


    private TextView tvShowName = null;
    private String mName = "";
    private View mLayoutMain = null;
    private int colorRes;

    public static AtricleFragment createInstance(String name) {
        AtricleFragment f = new AtricleFragment();
        Bundle b = new Bundle();
        b.putString("name", name);
        f.setArguments(b);
        return f;
    }

    public static AtricleFragment createInstance(String name, int color) {
        AtricleFragment f = new AtricleFragment();
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putInt("color", color);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (OnContentClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "必须继承OnContentClickListener");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(inflater, container);
        initEvent();
        return mLayoutMain;
    }

    /**
     * 初始化布局
     */
    private void initView(LayoutInflater inflater, ViewGroup container) {
        Bundle arguments = getArguments();
        mLayoutMain = inflater.inflate(R.layout.fragment_atricle, container, false);
        mLayoutMain.setBackgroundColor(colorRes);
        tvShowName = mLayoutMain.findViewById(R.id.tvName);
        String msgFromPar = "";
        if(arguments != null){
            mName = arguments.getString("name");
            colorRes = arguments.getInt("color");
            msgFromPar = arguments.getString(FlexibleActivity.FLEXIBLEACTIVITY_ARGS_MSG, "无信息");
        }
        String showTitle = TextUtils.isEmpty(msgFromPar) ? mName : mName + "——" +msgFromPar;

        tvShowName.setText(showTitle);
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        tvShowName.setOnClickListener(onNameClickLister);
    }

    private View.OnClickListener onNameClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onClick(mName);
        }
    };


    public interface OnContentClickListener{
        void onClick(String msg);
    }



}
