package com.fzp.mystudyandroid.views.aboutDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fzp.mystudyandroid.R;

/**
 * 纯文本提示对话框 <br/>
 *  <ul>
 *      <li>带按钮的提示框（不会自动关闭）</li>
 *      <li>不带按钮的提示框（会自动关闭）</li>
 *  </ul>
 * Created by Fuzp on 2018/3/23.
 */

public class PromptDialog extends BaseDialog implements Runnable{
    /**
     * 纯文本对话框持续显示时长
     */
    private static final int DISPLAY_DURATION = 2000;
    /**
     * 文本内容字符串
     */
    private String mMessage = "";
    /**
     * 显示对话框所对应的上下文
     */
    private Context mContext = null;
    /**
     * 对话框主体局
     */
    private View mMainView = null;
    /**
     * 显示对话框内容文本框
     */
    private TextView tvMessage = null;

    /**
     * 开启不带按钮提示对话框
     * @param context       上下文
     * @param title         对话框标题
     * @param message       对话框显示内容
     */
    public PromptDialog(@NonNull Context context, String title, String message) {
        super(context, title, 0, null, "",
                null, "", null, "");
        mContext = context;
        mMessage = message;
        new Thread(this).start();
    }

    /**
     * 开启带按钮提示对话框
     * @param context           上下文
     * @param title             对话框标题8
     * @param message           对话框显示内容
     * @param btnNum            按钮数量（1-3与）
     * @param cancelListener    第一（取消）按钮监听事件
     * @param btn1Str           第一按钮文本
     * @param middleListener    第二（中间）按钮监听事件
     * @param btn2Str           第二按钮文本
     * @param confirmListener   第三（确定）按钮监听事件
     * @param btn3Str           第三按钮文本
     */
    public PromptDialog(@NonNull Context context, String title, String message, int btnNum, CancelListener
            cancelListener, String btn1Str, MiddleListener middleListener, String btn2Str, ConfirmListener confirmListener, String btn3Str){
        super(context, title, btnNum, cancelListener, btn1Str, middleListener, btn2Str, confirmListener, btn3Str);
        mContext = context;
        mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvMessage = this.findViewById(R.id.tv_dialog_msg);
        tvMessage.setText(mMessage);
    }



    @Override
    protected View getMainView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mMainView = inflater.inflate(R.layout.view_text_dialog_main, null);
        return mMainView;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(DISPLAY_DURATION);
            if (isShowing()){
                dismiss();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
