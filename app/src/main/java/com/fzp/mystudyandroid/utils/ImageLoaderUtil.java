package com.fzp.mystudyandroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.graphics.BitmapFactory.*;

/**
 * 简易图片数据加载类(带本地缓存和内存缓存)
 * Created by Fuzp on 2018/6/19.
 * <b>注意：单例模式，需使用getInstance获取实例</b>
 */

public class ImageLoaderUtil {
    /**
     * 超时时长
     */
    private static final int TIME_OUT = 6000;

    /**
     * 图片下载完成标识
     */
    private static final int LOAD_FINISH = 0;

    /**
     * 工具类实例
     */
    private static ImageLoaderUtil mInstance = null;

    /**
     * 线程池
     */
    private ExecutorService mExecutor = null;

    /**
     * 图片内存缓存
     */
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * 默认线程数
     */
    private int defaultCapacity = 5;

    /**
     * <b>加载图片</b><br/>
     * 先从缓存中获取，不存在则本地缓存路径中读取，依旧找不到才通过url下载
     *
     * @param photoName 缓存文件名称
     * @param url       图片下载地址
     * @param cachePath 图片缓存文件夹路径
     * @param callback  回调接口
     */
    public void loadImage(final String photoName, final String url, final String cachePath,
                          final ImageLoaderCallBack callback) {
        /*
         * 将消息句柄放置到主线程中
         */
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOAD_FINISH:
                        if (msg.obj != null) {
                            String imagePath = msg.getData().getString("ImagePath");
                            Bitmap bmp = (Bitmap) msg.obj;
                            callback.onLoadOver(bmp, imagePath);
                        }
                        break;
                }
            }
        };

        /*
         *  线程池中开辟线程下载图片
         */
        getThreadPool(defaultCapacity).execute(new Runnable() {
            @Override
            public void run() {

                /*  减少无关紧要的线程分配的CPU */
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                /* 图片缓存路径 */
                String sPath = cachePath + photoName + FileUtil.PostfixConstants.IMAGE_POSTFIX;

                Bundle bundle = new Bundle();
                bundle.putString("ImagePath", sPath);

                Bitmap bitmap = getBitmapFromCache(photoName);
                if (bitmap != null) { //缓存中存在
                    Message msg = handler.obtainMessage(LOAD_FINISH, bitmap);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } else { // 缓存中未找到
                    File file = new File(cachePath);
                    if (!file.exists()) { //检查缓存本地文件是否存在，不存在则创建
                        file.mkdir();
                    }

                    bitmap = loadFromFile(sPath, callback);
                    if (bitmap != null) { //存在本地图片缓存文件
                        addBitmapToCache(photoName, bitmap);

                        Message msg = handler.obtainMessage(LOAD_FINISH, bitmap);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } else { //本地不存在则下载
                        InputStream is = null;
                        try {
                            is = loadFromNet(url);
                            writeToFileCache(sPath, is);
                        } catch (IOException e) {
                            callback.onError(e);
                            e.printStackTrace();
                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Options options = new Options();
                        callback.onDecodeFile(options);
                        bitmap = BitmapFactory.decodeFile(sPath, options);
                        if (bitmap != null) {
                            addBitmapToCache(photoName, bitmap);

                            Message msg = handler.obtainMessage(LOAD_FINISH, bitmap);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        });
    }

    /**
     * 写入图片缓存
     *
     * @param sPath 图片本地缓存路径
     * @param is    输入流
     */
    private void writeToFileCache(String sPath, InputStream is) {
        FileLock fl = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(sPath);
            fl = fos.getChannel().tryLock();//锁定文件避免多下称操作同一文件
            if (fl != null && fl.isValid()) { //文件可操作
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fl.release();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fl != null && fl.isValid()) {
                    fl.release();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 通过url下载图片
     *
     * @param urlStr 图片下载地址
     * @return 图片InputStream
     */
    private InputStream loadFromNet(String urlStr) throws IOException {
        HttpURLConnection conn;
        URL url = new URL(urlStr);
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true);
        conn.connect();

        return conn.getInputStream();
    }

    /**
     * 从本地缓存中查找相应图片
     *
     * @param sPath    图片文件缓存路径
     * @param callback 图片加载回调接口
     * @return 本地缓存图片
     */
    private Bitmap loadFromFile(String sPath, ImageLoaderCallBack callback) {
        Bitmap bitmap = null;
        File file = new File(sPath);
        if (file.exists()) {
            Options options = new Options();
            callback.onDecodeFile(options);
            bitmap = BitmapFactory.decodeFile(sPath, options);
            if (bitmap == null) {
                file.delete();//删除可能错误的文件
            }
        }
        return bitmap;

    }

    /**
     * 获取线程池
     *
     * @param poolCapacity 线程池容量
     * @return 线程池对象
     */
    public ExecutorService getThreadPool(int poolCapacity) {
        if (mExecutor == null || poolCapacity != defaultCapacity) {
            synchronized (ExecutorService.class) {
                defaultCapacity = poolCapacity;
                mExecutor = Executors.newFixedThreadPool(defaultCapacity);
            }
        }
        return mExecutor;
    }

    /**
     * 从内存缓存中获取图片
     *
     * @param key 缓存键名称
     * @return 不存在则返回null
     */
    private Bitmap getBitmapFromCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 将图片添加至缓存
     *
     * @param key    缓存键名称
     * @param bitmap 图片
     */
    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 构造方法
     */
    private ImageLoaderUtil() {
        // 获取系统分给应用程序的最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        // 区最大内存的八分之一作为缓存空间
        int mCacheSize = (int) (maxMemory / 8);
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            /**
             * 必须重写此方法，来测量Bitmap的大小
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 获取工具实例类
     *
     * @return 工具类实例
     */
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ImageLoaderUtil();
        }
        return mInstance;
    }


    /**
     * 图片加载回调接口类
     */
    public interface ImageLoaderCallBack {

        /**
         * 设置图片获取格式
         *
         * @param options 转换规格
         */
        void onDecodeFile(Options options);

        /**
         * 下载异常回调
         *
         * @param e 所发生异常
         */
        void onError(IOException e);

        /**
         * 图片加载完毕回调
         *
         * @param bitmap 图片
         * @param path   缓存路径
         */
        void onLoadOver(Bitmap bitmap, String path);
    }

}
