package com.fzp.mystudyandroid.utils.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.fzp.mystudyandroid.utils.FileUtil;

/**
 * 数据库辅助类
 * Created by Fuzp on 2018/7/10.<br/>
 *  <b>注意:</b> 单例模式，需使用getInstance()获取实例
 *
 */

public class DBHelper {
    /**
     * 数据库所在内部文件夹名称
     */
    public static final String DB_FOLDER = "databases";
    /**
     * 数据库名称
     */
    public static final String DATABASE_NAME = "StudyAndroid" + FileUtil.PostfixConstants.DB_POSTFIX;
    /**
     * 数据库版本
     */
    private final int DATABASE_VER = 1;
    /**
     * 辅助类实例
     */
    private static DBHelper mInstance = null;
    /**
     * 数据库创建和版本管理助手
     */
    private DatabaseHelper mDatabaseHelper = null;
    /**
     * 数据库实例
     */
    private static SQLiteDatabase db = null;

    public static void initDBHelper(Context context){
        mInstance = new DBHelper(context);
    }

    /**
     * 获取数据库辅助类实例
     * @param context   程序上下文
     * @return 辅助类实例
     */
    public synchronized static DBHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    /**
     * 打开可读写数据库
     * @return  数据库实例
     */
     SQLiteDatabase getDB(){
        if (isClose()){
            db = mDatabaseHelper.getWritableDatabase();
        }
        return  db;
    }

    /**
     * 数据库是否关闭
     * @return 是否关闭
     */
    private boolean isClose() {
        return (db == null) || !db.isOpen();
    }

    /**
     * 清空数据库操作相关实例
     */
    public void closeDB(){
        if (!isClose()){ //关闭数据库
            db.close();
            db = null;
        }

        if (mDatabaseHelper != null){ //清空数据库创建及版本管理助手实例
            mDatabaseHelper.close();
            mDatabaseHelper = null;
        }

        if (mInstance != null){ //清空数据库操作辅助工具实例
            mInstance = null;
        }
    }


    /**
     * 私有化构造方法
     * @param context   程序上下文
     */
    private DBHelper(Context context){
        mDatabaseHelper = new DatabaseHelper(context);
        db = mDatabaseHelper.getWritableDatabase();
    }


    /**
     * 数据库创建和版本管理帮助类
     */
    private class DatabaseHelper extends SQLiteOpenHelper{

        /**
         *  构造方法
         * @param context   程序上下文
         */
        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VER);
        }

        /**
         * 表创建及参数初始化 ，数据库首次创建时调用。
         * @param db 数据库.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FootballerDB.CREATE_FOOTBALLER_TABLE);
        }

        /**
         * 在需要升级数据库（版本增加）时调用
         * @param db            数据库.
         * @param oldVersion    旧版本号.
         * @param newVersion    新版本号.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
