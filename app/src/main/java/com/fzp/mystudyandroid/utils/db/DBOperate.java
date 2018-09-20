package com.fzp.mystudyandroid.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Locale;

/**
 * 数据库操作类
 * Created by Fuzp on 2018/7/12.<br/>
 * <b>注意:</b> 单例模式，需使用getInstance()获取实例
 */

public class DBOperate {

    /**
     * 操作类实例
     */
    private static DBOperate mInstance = null;
    /**
     * 数据库实例
     */
    private static SQLiteDatabase db = null;

    private DBOperate(Context context){
        db = DBHelper.getInstance(context).getDB();
        db.setLocale(Locale.CHINA);
    }

    /**
     * 获取数据库操作类实例
     * @param context   上下文
     * @return  操作实例
     */
    public static DBOperate getInstance(Context context){
        if (mInstance == null){
            mInstance = new DBOperate(context);
        }
        return  mInstance;
    }

    /**
     * 开始事务处理
     */
    public void beginTransaction(){
        db.beginTransaction();
    }

    /**
     * 结束事务处理
     */
    public void endTransaction(){
        try {
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }


    /**
     * 删除数据库表
     * @param tableName    表名
     */
    public void deleteTable(String tableName){
        db.execSQL("DROP  TABLE " + tableName);
    }


    /**
     * 清空数据库表
     * @param tableName     表名
     */
    public void clearTable(String tableName){
        db.execSQL("delete from " + tableName);
    }

    /**
     * 更新数据库表数据
     * @param tableName     表名
     * @param cv            更新数据
     * @param whereClause   条件语句
     * @param whereArgs     条件值数组
     */
    public void updateData(String tableName, ContentValues cv, String whereClause, String[] whereArgs){
        db.update(tableName, cv, whereClause, whereArgs);
    }

    /**
     * 从数据库表删除数据
     * @param tableName     表名
     * @param whereClause   条件语句
     * @param whereArgs     条件值数组
     */
    public void deleteData(String tableName, String whereClause, String[] whereArgs){
        db.delete(tableName, whereClause, whereArgs);
    }

    /**
     * 向数据库表插入数据
     * @param tableName     表名
     * @param cv            插入数据
     */
    public void insertData(String tableName, ContentValues cv){
        db.insert(tableName, null, cv);
    }

    /**
     * 根据条件从数据库查询相应数据
     * @param distinct      行唯一性（是否过滤重复数据）
     * @param tableName     表名
     * @param columns       需返回列数组（传递 null 则返回所有列）
     * @param whereClause   条件语句
     * @param whereArgs     条件值数组
     * @param groupBy       分组方式（传递 null 则不分组）
     * @param having        需返回哪些行（未分组时必须传递 null 则表示返回所有行组）
     * @param orderBy       行排序方式 （传递 null 则使用默认排序方式，可能是无序）
     * @param limit         限制返回行数 （传递 null 表示没有限制）
     * @return
     */
    public Cursor queryData(boolean distinct, String tableName, String[] columns,
                            String whereClause, String[] whereArgs, String groupBy,
                            String having, String orderBy, String limit){
        Cursor cur;
        cur = db.query(distinct, tableName, columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
        return cur;
    }

    /**
     *  根据条件获取某个表是否存在相应记录
     * @param tableName     表名
     * @param whereClause   条件语句
     * @param whereArgs     条件值数组
     * @return              是否存在
     */
    public boolean isExist(String tableName, String whereClause, String[] whereArgs){
        boolean isExist = false;
        Cursor cur = db.query(true, tableName, null,
                whereClause, whereArgs, null, null, null, null);

        if (cur != null && cur.getCount() > 0){
            isExist = true;
        }

        if (cur != null){
            cur.close();
        }

        return  isExist;
    }



}
