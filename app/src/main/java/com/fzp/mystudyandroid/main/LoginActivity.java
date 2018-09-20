package com.fzp.mystudyandroid.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.myEvent.MyClickListener;
import com.fzp.mystudyandroid.utils.FileUtil;
import com.fzp.mystudyandroid.utils.PermissionUtils;
import com.fzp.mystudyandroid.utils.PreCacheUtil;
import com.fzp.mystudyandroid.utils.WindowImmersiveUtil;
import com.fzp.mystudyandroid.utils.db.DBHelper;
import com.fzp.mystudyandroid.views.aboutDialog.PromptDialog;

import java.io.File;

import static com.fzp.mystudyandroid.utils.PreCacheUtil.*;
import static java.io.File.*;
import static java.lang.String.format;

/**
 * 登录界面
 * Created by Fuzp on 2018/4/8.
 */

public class LoginActivity extends AppCompatActivity {
    private static final int TO_HOME = 100002;
    /**
     * 数据库辅助类实例
     */
    public DBHelper mDBHelper = null;
    /**
     * 账号
     */
    private String mAccount = "";
    /**
     * 密码
     */
    private String mPSW = "";

    /**
     * 账号输入框
     */
    private EditText edtAccount = null;
    /**
     * 密码输入框
     */
    private EditText edtPSW = null;
    /**
     * 登录按钮
     */
    private LinearLayout layoutLoginBtn = null;
    /**
     * 忘记密码文本
     */
    private TextView tvForgotPSW = null;
    /**
     * 用户注册文本
     */
    private TextView tvUserRegistration = null;
    /**
     * 版本信息文本
     */
    private TextView tvVersionInfo = null;
    /**
     * 服务条款文本
     */
    private TextView tvTerms = null;
    private LoginHandler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowImmersiveUtil.statusBarHide(this);
        setContentView(R.layout.activity_login);
        initData();
        initView();
        initEvent();
    }

    /**
     * 初始化事件监听
     */
    private void initEvent() {
        edtAccount.addTextChangedListener(mAccountWatcher);
        edtPSW.addTextChangedListener(mPSWtWatcher);
        tvForgotPSW.setOnClickListener(onFogetPSWClick);
        tvUserRegistration.setOnClickListener(onRegistrationClick);
        tvVersionInfo.setOnClickListener(onVersionInfoClick);
        tvTerms.setOnClickListener(onTermsClick);
        layoutLoginBtn.setOnClickListener(onLoginClick);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        bindView();
        edtAccount.setText(mAccount);
        edtPSW.setText(mPSW);
    }

    private void bindView() {
        edtAccount = this.findViewById(R.id.edt_login_account);
        edtPSW = this.findViewById(R.id.edt_login_psw);
        layoutLoginBtn = this.findViewById(R.id.layout_login_btn);
        tvForgotPSW = this.findViewById(R.id.tv_forgot_psw);
        tvUserRegistration = this.findViewById(R.id.tv_user_registration);
        tvVersionInfo = this.findViewById(R.id.tv_version_info);
        tvTerms = this.findViewById(R.id.tv_terms);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        PermissionUtils.requestMultiPermissions(this, new PermissionUtils.GrantedCallBack() {
            @Override
            public void onGrantedCallBack(int requestCode) {

            }
        });
        mDBHelper = DBHelper.getInstance(this);
        mHandler = new LoginHandler();
        mAccount = getUserName();
        mPSW = getPassword();
    }


    @Override
    public void onBackPressed() {
        mDBHelper.closeDB();
        finish();
    }


    /**
     * 登录按钮点击事件监听
     */
    private MyClickListener onLoginClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {
            if (TextUtils.isEmpty(mAccount)) {
                new PromptDialog(LoginActivity.this, "友情提示", "请输入账号").show();
                return;
            }
            if (TextUtils.isEmpty(mPSW)) {
                new PromptDialog(LoginActivity.this, "友情提示", "请输入密码").show();
                return;
            }
            PreCacheUtil.setValue(PreCacheUtil.PRE_ISLOGIN,true);
            PreCacheUtil.setValue(PreCacheUtil.PRE_USERNAME, mAccount);
            PreCacheUtil.setValue(PreCacheUtil.PRE_PSW, mPSW);
            mHandler.sendEmptyMessage(TO_HOME);

        }
    };

    /**
     * 服务条款文本点击事件监听
     */
    private MyClickListener onTermsClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {
            new PromptDialog(LoginActivity.this, "友情提示", "压根没这个东西！").show();
        }
    };

    /**
     * 账号注册文本点击事件监听
     */
    private MyClickListener onRegistrationClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {
            new PromptDialog(LoginActivity.this, "友情提示", "不需要注册，随便登录！").show();
        }
    };

    /**
     * 版本信息文本点击事件监听
     */
    private MyClickListener onVersionInfoClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {

            int copyType = copyPrefsAndDB();
            String hint;
            switch (copyType) {
                case 0:
                    hint = "拷贝失败！";
                    break;
                case 1:
                    hint = "prefs拷贝成功，DB拷贝失败！";
                    break;
                case 2:
                    hint = "prefs 拷贝失败，DB拷贝成功！";
                    break;
                case 3:
                    hint = "拷贝成功！";
                    break;
                default:
                    hint = "未知情况！";
                    break;
            }
            new PromptDialog(LoginActivity.this, "友情提示", hint).show();
        }
    };

    /**
     * 拷贝数据库及 全局缓存文件到外部存储
     *
     * @return <ul>
     * <li>0 - 拷贝失败</li>
     * <li>1 - prefs拷贝成功，DB拷贝失败</li>
     * <li>2 - prefs 拷贝失败，DB拷贝成功</li>
     * <li>3 - 拷贝成功</li>
     * </ul>
     */
    private int copyPrefsAndDB() {
        int type = 0;
        String destPath = FileUtil.FileConstants.EXTERNAL_ROOT_DIR;
        /* 内部prefs文件路径 */
        String sysPrefsPath = (getFilesDir().getPath() + separator + PreCacheUtil.BASICINFO
                + FileUtil.PostfixConstants.XML_POSTFIX).replace("files", PreCacheUtil.PREFS_FOLDER);
        /* 目标prefs文件路径 */
        String destPrefsPath = destPath + separator + BASICINFO + FileUtil.PostfixConstants.XML_POSTFIX;
        /* 拷贝Prefs 文件 */
        if (FileUtil.copyFile(sysPrefsPath, destPrefsPath)) type = 1;

        /* 内部DB文件路径 */
        String sysDBPath = getDatabasePath(DBHelper.DATABASE_NAME).getPath();
        /* 目标DB文件路径 */
        String destDBPath = destPath  + separator + DBHelper.DATABASE_NAME;
        /* 拷贝DB文件 */
        if (FileUtil.copyFile(sysDBPath, destDBPath)) type = type == 1 ? 3 : 2;
        return type;
    }

    /**
     * 忘记密码文本点击事件监听
     */
    private MyClickListener onFogetPSWClick = new MyClickListener() {
        @Override
        protected void myClick(View view) {
            new PromptDialog(LoginActivity.this,
                    "友情提示", "忘记了怪谁，我也不知道！").show();
        }
    };

    /**
     * 账号输入框监听器
     */
    private TextWatcher mAccountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mAccount = s.toString();
        }
    };

    /**
     * 密码输入框监听器
     */
    private TextWatcher mPSWtWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mPSW = s.toString();
        }
    };

    @SuppressLint("HandlerLeak")
    private class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent;
            switch (msg.what) {
                case TO_HOME:
                    FileUtil.initCacheFile(LoginActivity.this);
                    if (FileUtil.checkExternalStorageState() == 1){//初始化数据库
                        if (!FileUtil.createDir(FileUtil.FileConstants.EXTERNAL_ROOT_DIR)){
                            Toast.makeText(LoginActivity.this,
                                    "创建缓存文件失败，确保开启存储权限！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            finish();
        }
    }

}
