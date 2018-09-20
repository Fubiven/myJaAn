package com.fzp.mystudyandroid.studyTest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fzp.mystudyandroid.R;
import com.fzp.mystudyandroid.utils.FileUtil;
import com.fzp.mystudyandroid.utils.ImageLoaderUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoadImageActivity extends AppCompatActivity {
    /**
     * 图片数据文件名
     */
    public static final String IMAGE_FILE_NAME ="ImageUrl.json";

    /**
     * 所需下载的图片url地址
     */
    private String mImageUrl = "";

    /**
     * 图片缓存路径
     */
    private String mPath = "";

    /**
     * 图片名
     */
    private String mPicName = "";

    /**
     * 所下载的图片序号
     */
    private int mIndex = 0;

    /**
     * 开始简单加载图片按钮
     */
    private LinearLayout laySimpleLoader = null;

    /**
     * 加载图片视图
     */
    private ImageView ivLoadImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        setTitle("图片加载");
        initData();
        initView();
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        laySimpleLoader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoaderUtil.getInstance().loadImage(mPicName, mImageUrl, mPath, new ImageLoaderUtil.ImageLoaderCallBack() {
                    @Override
                    public void onDecodeFile(BitmapFactory.Options options) {

                    }

                    @Override
                    public void onError(IOException e) {

                    }

                    @Override
                    public void onLoadOver(Bitmap bitmap, String path) {
                        ivLoadImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                });

                mIndex ++;
                getLoadInfo();
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        laySimpleLoader = findViewById(R.id.layout_simple_loader);
        ivLoadImage = findViewById(R.id.iv_load_image);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getLoadInfo();
        mPath = FileUtil.getImageCachePath(this);
    }

    /**
     * 获取图片信息
     */
    private void getLoadInfo(){
        JSONObject imageInfo = getImageInfo(this, mIndex);
        if (imageInfo != null){
            mImageUrl = imageInfo.optString("url");
            mPicName = imageInfo.optString("name");
        } else{
            mIndex = 0;
            getLoadInfo();
        }

    }


    /**
     * 图片信息
     * @param mContent  所调用界面上下文
     * @param index     所选图片的序号（与ImageUrl.json 文件的JsonArray 对应）
     * @return
     */
    public static JSONObject getImageInfo(Context mContent, int index){
        JSONObject imageObj = null;
        try {
            JSONArray imageJson = new JSONArray(FileUtil.getJsonFromAssets(mContent, IMAGE_FILE_NAME));
            if(imageJson.length() > index){
                imageObj = imageJson.getJSONObject(index);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageObj;
    }
}
