package com.fzp.mystudyandroid.utils;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.views.aboutDialog.BaseDialog;
import com.fzp.mystudyandroid.views.aboutDialog.PromptDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态权限申请工具类代码
 * Created by Fuzp on 2018/7/18.
 */

public class PermissionUtils {
    /**
     * 类标识
     */
    private static final String TAG =  PermissionUtils.class.getSimpleName();
    /**
     *  录制音频权限标识号
     */
    public static final int CODE_RECORD_AUDIO = 0;
    /**
     *  账户获取权限标识号
     */
    public static final int CODE_GET_ACCOUNTS = 1;
    /**
     *  手机状态权限标识号
     */
    public static final int CODE_READ_PHONE_STATE = 2;
    /**
     *  拨号权限标识号
     */
    public static final int CODE_CALL_PHONE = 3;
    /**
     *  相机权限标识号
     */
    public static final int CODE_CAMERA = 4;
    /**
     * 精准定位权限标识号
     */
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    /**
     * 大致定位权限标识号
     */
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    /**
     *  读写入存储权限标识号
     */
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 7;
    /**
     * 多权限请求标识号
     */
    public static final int CODE_MULTI_PERMISSION = 100;

    /**
     *  录制音频权限
     */
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    /**
     *  账户获取权限
     */
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    /**
     *  手机状态权限
     */
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    /**
     *  拨号权限标
     */
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    /**
     *  相机权限标
     */
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    /**
     *  精准定位权限
     */
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    /**
     *  大致定位权限
     */
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    /**
     *  读写存储权限
     */
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    /**
     * 动态申请所有权限数组
     */
    private static final String[] requestPermissions = {
            PERMISSION_RECORD_AUDIO,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_WRITE_EXTERNAL_STORAGE
    };

    /**
     * 授权回调
     */
    public interface GrantedCallBack{
        void onGrantedCallBack(int requestCode);
    }


    /**
     *  单一权限申请
     * @param activity      发起申请的界面
     * @param requestCode   申请权限标识
     * @param callBack      授权回调
     */
    public static void requestPremission(Activity activity, int requestCode, GrantedCallBack callBack){
        if (activity == null) {
            return;
        }

        if (requestCode < 0 || requestCode>= requestPermissions.length){
            return;
        }

        final String requestPermission = requestPermissions[requestCode];
        /* 检查授权 */
        if (ActivityCompat.checkSelfPermission(activity, requestPermission)!= PackageManager.PERMISSION_GRANTED){

            /* 是否需要解释权限申请原因 */
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)){
                showRationale(activity, requestCode, requestPermission);
            }else{
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
            }

        }else{
            Toast.makeText(activity, "已授权:" + requestPermissions[requestCode], Toast.LENGTH_SHORT).show();
            callBack.onGrantedCallBack(requestCode);
        }

    }

    /**
     * 一次申请多条权限
     * @param activity  发起申请的界面
     * @param callBack  授权回调
     */
    public static void requestMultiPermissions(final Activity activity, GrantedCallBack callBack){
        final List<String> noRationalePermissions = getNoGrantedPermission(activity, false);
        final List<String> needRationalePermissions = getNoGrantedPermission(activity, true);

        if (noRationalePermissions.size() == 0 && needRationalePermissions.size() == 0){//权限已全部授权
            callBack.onGrantedCallBack(CODE_MULTI_PERMISSION);
        }

        /* 不需要显示原因的未授权权限 */
        if (noRationalePermissions.size() > 0){
            ActivityCompat.requestPermissions(activity, noRationalePermissions.
                    toArray(new String[noRationalePermissions.size()]), CODE_MULTI_PERMISSION);
        }

        /* 需要显示原因的未授权权限 */
        if (needRationalePermissions.size() >0){
            showRationaleDialog(activity, "需授权这些权限", new BaseDialog.ConfirmListener() {
                @Override
                public void onClicked() {
                    ActivityCompat.requestPermissions(activity, needRationalePermissions.
                            toArray(new String[needRationalePermissions.size()]), CODE_MULTI_PERMISSION);
                }
            });
        }
    }

    /**
     * 权限申请结果回调
     * @param activity      发起申请的界面
     * @param requestCode   请求权限标识
     * @param permissions   权限数组
     * @param grantResults  授权结果
     * @param callBack      授权回调
     */
    public static void requestPermissionsResult(Activity activity, int requestCode,
           @NonNull String[] permissions, @NonNull int[] grantResults, GrantedCallBack callBack){
        if (activity == null) return;

        if (requestCode == CODE_MULTI_PERMISSION){ //请求多个权限
            requestMultiResult(activity, permissions, grantResults, callBack);
            return;
        }
        if (requestCode < 0 || requestCode >= requestPermissions.length)return;

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){//授权成功
            callBack.onGrantedCallBack(requestCode);
        }else{
            String[] permissionHints = activity.getResources().getStringArray(R.array.permissions);
            openSettingActivity(activity, "需授权：" + permissions[requestCode]);
        }

    }

    /**
     * 多权限申请结果回调
     * @param activity         发起申请的界面
     * @param permissions      权限数组
     * @param grantResults     授权结果
     * @param callBack         授权回调
     */
    private static void requestMultiResult(Activity activity,
             String[] permissions, int[] grantResults, GrantedCallBack callBack) {
        if (activity == null) return;
        Map<String, Integer> perms = new HashMap<>();//权限及授权情况对照表
        ArrayList<String> noGranted = new ArrayList<>();//未授权权限列表
        for (int i =0; i< permissions.length; i++) {
            String permission = permissions[i];
            int grantState = grantResults[i];
            perms.put(permission, grantState);
            if (grantState != PackageManager.PERMISSION_GRANTED)noGranted.add(permission);
        }

        if (noGranted.size() > 0){
            openSettingActivity(activity, "这些权限需要授权!");
        }else{
            callBack.onGrantedCallBack(CODE_MULTI_PERMISSION);
            Toast.makeText(activity, "所有权限已授权: " + noGranted, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 进入设置界面
     * @param activity  发起申请的界面
     * @param message   对话框提示文本
     */
    private static void openSettingActivity(final Activity activity, String message){
        showRationaleDialog(activity, message, new BaseDialog.ConfirmListener() {
            @Override
            public void onClicked() {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * 获取未许可权限
     * @param activity              发起申请的界面
     * @param isShouldRationale     是否需要显示原因
     */
    private static ArrayList<String> getNoGrantedPermission(Activity activity, boolean isShouldRationale){
        ArrayList<String> permissionList = new ArrayList<>();

        for (String requestPermission : requestPermissions) {//检查所有权限授权情况

            int checkSelfPermission = -1;
            checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){ //未授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)){
                    if (isShouldRationale) permissionList.add(requestPermission);
                }else{
                    if (!isShouldRationale) permissionList.add(requestPermission);
                }
            }

        }

        return  permissionList;
    }


    /**
     * 显示申请权限原因
     * @param activity              发起申请的界面
     * @param requestCode           申请权限标识
     * @param requestPermission     申请权限字符串
     */
    private static void showRationale(
            final Activity activity, final int requestCode, final String requestPermission) {
        String[] permissionHints = activity.getResources().getStringArray(R.array.permissions);
        showRationaleDialog(activity, "原因: " + permissionHints[requestCode],
                new BaseDialog.ConfirmListener() {
                    @Override
                    public void onClicked() {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{requestPermission}, requestCode);
                        Log.d(TAG, "显示需请求权限的原因:" + requestPermission);
                    }
                });
    }


    private static void showRationaleDialog(
            Activity context, String message, BaseDialog.ConfirmListener okListener){
            new PromptDialog(context, "友情提示", message, 2, new BaseDialog.CancelListener() {
                @Override
                public void onClicked() {}
            }, "取消", null, "",okListener, "确定").show();
    }
}
