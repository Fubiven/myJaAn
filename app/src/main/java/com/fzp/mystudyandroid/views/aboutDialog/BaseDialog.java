package com.fzp.mystudyandroid.views.aboutDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fzp.mystudyandroid.R;

/**
 * 基础对话框控件
 * Created by Fuzp on 2018/3/21.
 */

public abstract class BaseDialog extends Dialog{
    /**
     * 上下文
     */
    private Context mContext = null;
    private View mDialogView = null;
    /**
     * 标题显示内容字符串
     */
    private String mTitle = "";
    /**
     * 按钮1文本
     */
    private String mBtn1Msg = "";
    /**
     * 按钮2文本
     */
    private String mBtn2Msg = "";
    /**
     * 按钮3文本
     */
    private String mBtn3Msg = "";
    /**
     * 按钮数量 （有效区间：0-3）
     */
    private int mBtnNum = 0;
    /**
     * 中间按钮监听事件
     */
    private MiddleListener mMiddleListener;
    /**
     * 取消按钮监听事件
     */
    private CancelListener mCancelListener;
    /**
     * 确认按钮监听事件
     */
    private ConfirmListener mConfirmListener;
    /**
     * 对话框标题
     */
    private TextView tvTitle = null;
    /**
     * 对话框主体容器
     */
    private FrameLayout container = null;
    /**
     * 底部按钮分割线
     */
    private TextView tvLine = null;
    /**
     * 对话框按钮栏
     */
    private LinearLayout layoutBottom = null;
    /**
     * 对话框按钮1
     */
    private TextView tvBtn2 = null;
    /**
     * 对话框按钮2
     */
    private TextView tvBtn3 = null;
    /**
     * 对话框按钮3
     */
    private TextView tvBtn1 = null;

    public BaseDialog(@NonNull Context context, String title, int btnNum,
                      CancelListener cancelListener, String btn1Text, MiddleListener middleListener,
                      String btn2Text, ConfirmListener confirmListener, String btn3Text) {
        super(context);
        mContext = context;
        mTitle = title;
        mBtn1Msg = btn1Text;
        mBtn2Msg = btn2Text;
        mBtn3Msg = btn3Text;
        mBtnNum = btnNum;
        mCancelListener = cancelListener;
        mMiddleListener = middleListener;
        mConfirmListener = confirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_base_view,null);
        setContentView(mDialogView);
        initBaseView();
        initBaseEvent();
    }

    /**
     * 初始化监听事件
     */
    private void initBaseEvent() {
        tvBtn1.setOnClickListener(btn1ClickListener);
        tvBtn2.setOnClickListener(btn2ClickListener);
        tvBtn3.setOnClickListener(btn3ClickListener);
    }

    /**
     * 初始化布局
     */
    private void initBaseView() {
        bindBaseView();
        setDialogTitle();
        container.addView(getMainView(), 0);
        setDialogBottom();
    }

    /**
     * 设置对话框底部按钮布局显示样式
     */
    private void setDialogBottom() {
        if (mBtnNum == 0){
            tvLine.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.GONE);
        } else if (mBtnNum == 1){
            tvBtn1.setText(mBtn1Msg);
            tvBtn2.setVisibility(View.GONE);
            tvBtn3.setVisibility(View.GONE);
        }else if (mBtnNum == 2){
            tvBtn1.setText(mBtn1Msg);
            tvBtn2.setVisibility(View.GONE);
            tvBtn3.setText(mBtn3Msg);
        }else{
            tvBtn1.setText(mBtn1Msg);
            tvBtn2.setText(mBtn2Msg);
            tvBtn3.setText(mBtn3Msg);
        }
    }

    /**
     * 设置对话框标题显示内容
     */
    private void setDialogTitle() {
        if (TextUtils.isEmpty(mTitle)){
            mTitle = "友情提示";
        }
        tvTitle.setText(mTitle);
    }

    /**
     * 获取对话框主体布局
     * @return
     */
    protected abstract View getMainView();

    /**
     * 绑定界面
     */
    private void bindBaseView() {
        tvTitle = mDialogView.findViewById(R.id.tv_dialog_title);
        container = mDialogView.findViewById(R.id.fl_dialog_container);
        tvLine = mDialogView.findViewById(R.id.tv_dialog_line);
        layoutBottom = mDialogView.findViewById(R.id.layout_bottom);
        tvBtn1 = mDialogView.findViewById(R.id.tv_dialog_btn1);
        tvBtn2 = mDialogView.findViewById(R.id.tv_dialog_btn2);
        tvBtn3 = mDialogView.findViewById(R.id.tv_dialog_btn3);
    }

    /**
     * 第一个（取消）按钮监听事件
     */
    private View.OnClickListener btn1ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (mCancelListener != null) {
                mCancelListener.onClicked();
            }
        }
    };

    /**
     * 第二个（中间）按钮监听事件
     */
    private View.OnClickListener btn2ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (mMiddleListener != null) {
                mMiddleListener.onClicked();
            }
        }
    };

    /**
     * 第三个（确认）按钮监听事件
     */
    private View.OnClickListener btn3ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (mConfirmListener != null) {
                mConfirmListener.onClicked();
            }
        }
    };


    /**
     * 确认按钮监听接口类
     */
    public interface ConfirmListener {
        void onClicked();
    }

    /**
     * 中间按钮监听接口类
     */
    public interface MiddleListener {
        void onClicked();
    }

    /**
     * 取消按钮监听接口类
     */
    public interface CancelListener {
        void onClicked();
    }
}
