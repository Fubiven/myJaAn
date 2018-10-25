package com.fzp.mystudyandroid.utils;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import org.apache.http.util.EncodingUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * 文件处理工具类
 * Created by Fuzp on 2018/4/11.
 */

public class FileUtil {
    /**
     * 图片缓存路径
     */
    private static String imageCachePath ="";
    /**
     * 音频缓存路径
     */
    private static String audioCachePath ="";
    /**
     * 视频缓存路径
     */
    private static String videoCachePath ="";
    /**
     * 信息缓存路径
     */
    private static String msgCachePath ="";

    /**
     * 从Assets文件夹中获取JSON文件内容
     * @return  json数据字符串
     */
    public static String getJsonFromAssets(Context context, String fileName){
        StringBuilder sb = new StringBuilder();

        //获取assets资源管理器
        AssetManager am = context.getAssets();

        //通过管理器打开文件并读取数据
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  sb.toString();
    }

    /**
     * 向文件写入字符串
     * @param strData   写入内容
     * @param fileName  文件名
     * @param isAppend  是否续传
     * @return
     */
    public static boolean writeStringToFile(String strData, String fileName, boolean isAppend){
        return writeStringToFile(strData, "",fileName, isAppend);
    }

    /**
     * 向文件写入字符串
     *
     * @param strData       写入内容
     * @param directoryPath 目录路径
     * @param fileName      文件名
     * @param isAppend      是否续传
     * @return 是否成功
     */
    public static boolean writeStringToFile(
            String strData, String directoryPath, String fileName, boolean isAppend) {
        boolean result = false;
        if (TextUtils.isEmpty(strData)) {
            return result;
        }
        if (!TextUtils.isEmpty(directoryPath)) { //需创建新目录
            final File directoryFile = new File(directoryPath);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }

        }
        final File file = new File(fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final FileOutputStream fos = new FileOutputStream(file, isAppend);
            byte[] buffer;
            buffer = strData.getBytes();
            fos.write(buffer);
            fos.flush();
            result = true;
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  result;
    }

    /**
     * 读取文件,返回类型是字符串String类型
     *
     * @param fileName 文件名
     * @return 返回文件内容字符串
     */
    public static String readString(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        final File file = new File(fileName);
        if (!file.exists()) { //文件不存在
            return null;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String data;
            try {
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString().trim();
    }

    /**
     * 读取asset数据
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 返回文件内容字符串
     */
    public static String readAssetsString(Context context, String fileName) {
        String result = "";
        try {
            final InputStream is = context.getResources().getAssets().open(fileName);
            final int length = is.available();// 获取文件字节数
            byte[] buffer = new byte[length]; // 创建匹配字节长度的字节数组
            is.read(buffer); //读取数据
            result = EncodingUtils.getString(buffer, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检查SD卡是否可用(方法已过时，请使用checkExternalStorageState代替)
     * @return 是否可用
     */
    @Deprecated
    public static Boolean checkSDCard() {
        final String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查外部存储状态
     * @return  0：不可用
     *          1：读写
     *          2：只读
     */
    public static int checkExternalStorageState(){
        int state = 0;
        final String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)){
            state = 1;
        }else if(status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            state = 2;
        }
        return  state;
    }

    /**
     * 获取外部存储缓存目录
     *
     * @param context 上下文
     * @return 目录文件
     */
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }


    /**
     * 获取内部存储缓存目录
     *
     * @param context 上下文
     * @return 目录文件
     */
    public static File getAppCacheDir(Context context) {
        return context.getCacheDir();
    }

    /**
     * 获取缓存目录
     *
     * @return 目录文件
     */
    private static File getDiskCacheDir(Context context) {
        final String cachePath = checkExternalStorageState() == 1 ?
                getExternalCacheDir(context).getPath() : getAppCacheDir(context).getPath();
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }

    /**
     * 获取文件路径空间大小
     *
     * @param file 对应文件
     * @return 空间大小
     */
    public static long getUsableSpace(File file) {
        StatFs statFs = new StatFs(file.getPath());//获取存储空间类
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    /**
     * 文件/空间 大小单位格式化
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        String suffix = null;
        float fSize;
        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
                if (fSize >= 1024) {
                    suffix = "GB";
                    fSize /= 1024;
                }
            }
        } else {
            fSize = size;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder(df.format(fSize));
        if (!TextUtils.isEmpty(suffix)) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * 获取手机内部储存files文件目录
     *
     * @param context 上下文
     * @return 返回文件目录地址绝对路径
     */
    public static String getAppFileDirPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取手机内部存储cache文件目录
     *
     * @param context 上下文
     * @return 返回缓存目录地址路径
     */
    public static String getAppCacheDirPath(Context context) {
        return context.getCacheDir().getPath();
    }


    /**
     * 检查app缓存目录，不存在则创建
     *
     * @param context 上下文
     * @return 是否存在
     */
    public static boolean checkAppCacheDirectory(Context context) {
        File appCacheDir = FileUtil.getDiskCacheDir(context);
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
        }
        return true;
    }

    /**
     * 获取信息缓存路径字符串
     * @param context   上下文
     * @return  信息缓存地址字符串
     */
    public static String getMsgCachePath(Context context){
        String sPath = msgCachePath;

        if (TextUtils.isEmpty(sPath)){ //不存在则重新获取
            final String cacheDirPath = getDiskCacheDir(context).getAbsolutePath();
            sPath = cacheDirPath + FileConstants.CACHE_MESSAGE_DIR;
            msgCachePath = sPath;
        }
        return  sPath;
    }

    /**
     * 获取视频缓存路径字符串
     * @param context   上下文
     * @return  视频缓存地址字符串
     */
    public static String getVideoCachePath(Context context){
        String sPath = videoCachePath;

        if (TextUtils.isEmpty(sPath)){ //不存在则重新获取
            final String cacheDirPath = getDiskCacheDir(context).getAbsolutePath();
            sPath = cacheDirPath + FileConstants.CACHE_VIDEO_DIR;
            videoCachePath = sPath;
        }
        return  sPath;
    }

    /**
     * 获取音频缓存路径字符串
     * @param context   上下文
     * @return  音频缓存地址字符串
     */
    public static String getAudioCachePath(Context context){
        String sPath = audioCachePath;

        if (TextUtils.isEmpty(sPath)){ //不存在则重新获取
            final String cacheDirPath = getDiskCacheDir(context).getAbsolutePath();
            sPath = cacheDirPath + FileConstants.CACHE_AUDIO_DIR;
            audioCachePath = sPath;
        }
        return  sPath;
    }

    /**
     * 获取图片缓存路径字符串
     * @param context   上下文
     * @return  图片缓存地址字符串
     */
    public static String getImageCachePath(Context context){
        String sPath = imageCachePath;

        if (TextUtils.isEmpty(sPath)){ //不存在则重新获取
            final String cacheDirPath = getDiskCacheDir(context).getAbsolutePath();
            sPath = cacheDirPath + FileConstants.CACHE_IMAGE_DIR;
            imageCachePath = sPath;
        }
        return  sPath;
    }

    /**
     * 创建项目缓存文件目录
     *
     * @param context 上下文
     */
    public static void initCacheFile(Context context) {
        final String cacheDirPath = getDiskCacheDir(context).getAbsolutePath();

        /*  创建图片缓存文件 */
        final String imageDirPath = cacheDirPath + FileConstants.CACHE_IMAGE_DIR;
        final File imageFileDir = new File(imageDirPath);
        if (!imageFileDir.exists()) {
            imageFileDir.mkdirs();
        }
        imageCachePath = imageDirPath;

        /* 创建音频缓存文件 */
        final String audioDirPath = cacheDirPath + FileConstants.CACHE_AUDIO_DIR;
        final File audioFileDir = new File(audioDirPath);
        if (!audioFileDir.exists()) {
            audioFileDir.mkdirs();
        }
        audioCachePath = audioDirPath;

        /* 创建视频缓存文件 */
        final String videoDirPath = cacheDirPath + FileConstants.CACHE_VIDEO_DIR;
        final File videoFileDir = new File(videoDirPath);
        if (!videoFileDir.exists()) {
            videoFileDir.mkdirs();
        }
        videoCachePath = videoDirPath;

        /* 创建信息缓存文件 */
        final String messageDirPath = cacheDirPath + FileConstants.CACHE_MESSAGE_DIR;
        final File messageFileDir = new File(messageDirPath);
        if (!messageFileDir.exists()) {
            messageFileDir.mkdirs();
        }
        msgCachePath = messageDirPath;
    }

    /**
     * 创建目录
     * @param dirPath   目录路径
     * @return  是否成功
     */
    public static Boolean createDir(String dirPath) {
        File dir = new File(dirPath);
        return dir.exists() || dir.mkdirs();

    }

    /**
     * 目录拷贝
     * @param srcFolderPath    原始目录路径
     * @param destFolderPath   目标目录路径
     * @return  是否成功
     */
    public static boolean copyFolder(String srcFolderPath, String destFolderPath){
        File[] allFiles;
        File srcFile = new File(srcFolderPath);
        File destFile = new File(destFolderPath);

        if (!srcFile.exists()){
            return  false;
        }else{
            allFiles = srcFile.listFiles();//获取全部文件
        }

        if(!destFile.exists()){//不存在则创建
            destFile.mkdirs();
        }

        for (File itemFile : allFiles) {
            if (itemFile.isDirectory()) { //当前项为子目录则递归
                copyFolder(itemFile.getPath() + File.separator,
                        destFolderPath + itemFile.getName() + File.separator);
            } else { //为文件则进行文件拷贝
                copyFile(itemFile.getPath(), destFolderPath + itemFile.getName());
            }
        }

        return true;
    }


    /**
     * 文件拷贝
     * @param fromFilePath  源文件路径
     * @param toFilePath    源文件路径
     * @return  是否成功
     */
    public static boolean copyFile(String fromFilePath, String toFilePath){
        try {
            InputStream is = new FileInputStream(fromFilePath);
            OutputStream os = new FileOutputStream(toFilePath);
            int readLen;
            byte[] buf = new byte[1024];
            while ((readLen = is.read(buf))> 0){
                os.write(buf, 0, readLen);
            }
            os.close();
            is.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 文件路径常量
     */
    public static class FileConstants {
        /**
         * 外部存储根路径
         */
        public static final String EXTERNAL_ROOT_DIR = Environment
                .getExternalStorageDirectory().getPath() + File.separator + "MyStudyAndroid";
        /**
         * 本地图片缓存路径
         */
        public static final String CACHE_IMAGE_DIR = "/image";

        /**
         * 本地音频缓存路径
         */
        public static final String CACHE_AUDIO_DIR = "/audio";

        /**
         * 本地视频片缓存路径
         */
        public static final String CACHE_VIDEO_DIR = "/video";

        /**
         * 本地信息缓存路径
         */
        public static final String CACHE_MESSAGE_DIR = "/message";
    }

    /**
     * 后缀名常量
     */
    public static class PostfixConstants{
        /**
         * 通用后缀名
         */
        public static final String NORMAL_POSTFIX = ".dat";
        /**
         * 图片格式后缀名
         */
        public static final String IMAGE_POSTFIX = ".jpg";
        /**
         * 数据库格式后缀名
         */
        public static final String DB_POSTFIX = ".db";
        /**
         *
         */
        public static final String XML_POSTFIX =".xml";
    }
}
