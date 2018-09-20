package com.fzp.mystudyandroid.workTest;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.main.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 工作测试界面
 * Created by Fubiven on 2018/5/15.
 */

public class WorkTestActivity extends BaseActivity {
    private TextView tvShowResult = null;
    private TextView tvCalculate = null;
    private byte[] canData;
    private int SPN;
    private int FMI;
    private JSONArray detailArr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initEvent();
        setTitleBar("工作测试",null, 0, null);
        SPN = parseSPN(canData);
        FMI = parseFMI(canData);
        tvShowResult.setText(SPN + "=====" + FMI);
    }

    private void initEvent() {
        tvShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailArr != null && detailArr.length() > 0){
                    for (int i = 0; i < detailArr.length(); i++){
                        try {
                            JSONObject itemObj = detailArr.getJSONObject(i);
                            if (SPN == itemObj.getInt("SPN") && FMI == itemObj.getInt("FMI")){
                                Toast.makeText(WorkTestActivity.this,
                                        itemObj.optString("describe"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        tvCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WorkTestActivity.this,
                        (1388 - 1388%10 + 10)+"", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 初始化参数
     */
    private void initView() {
        tvShowResult = findViewById(R.id.tv_show_result);
        tvCalculate = findViewById(R.id.tv_calculate);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        canData = new byte[]{ 2, 10, 0, 66, (byte) 175, 10, 0, 0};
        try {
            JSONArray malfuncteJson = new JSONArray(getJsonFromAssets(
                    WorkTestActivity.this, "MalfuncteData.json"));
            for (int i = 0; i < malfuncteJson.length(); i++){
                JSONObject typeObj = malfuncteJson.getJSONObject(i);
                if (typeObj.optString("type") .equals("00")){ //发动机系统
                    detailArr = typeObj.getJSONArray("detail");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String getJsonFromAssets(Context context, String fileName){
        StringBuilder sb = new StringBuilder();
        //获取assets资源管理器
        AssetManager assetManager = context.getAssets();
        //通过管理器打开文件并读取
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  sb.toString();
    }

    /**
     * 解析SPN 数据
     * @param data md1返回数据
     * @return
     */
    public static int parseSPN(byte[] data) {
        int num = data[4] & 0xFF;
        num |= ((data[3] << 8) & 0xFF00);
        num |= ((data[2] << 16) & 0xFF0000);
        return num >> 5;
    }

    /**
     * 解析FMI 数据
     * @param data md1返回数据
     * @return
     */
    public short parseFMI(byte[] data) {
        return (short) (data[4] & 0x1f);
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_work_test;
    }
}
