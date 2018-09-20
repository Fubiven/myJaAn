package com.fzp.mystudyandroid.utils.db;

import android.provider.BaseColumns;

/**
 * 足球运动员信息数据库操作类
 * Created by Fuzp on 2018/7/12.
 */

public class FootballerDB {

    /**
     * 足球运动员信息表
     */
    public final static String TABLE_FOOTBALLER = "footballer";

    /**
     *  足球运动员信息表字段
     */
    public interface FootballerColumns extends BaseColumns{

        /**
         * 球员姓名
         */
        final static String TABLE_NAME = "name";

        /**
         * 年龄
         */
        final static String TABLE_AGE = "age";

        /**
         * 身高(单位：mm)
         */
        final static String TABLE_HEIGHT = "height";

        /**
         * 体重(kg)
         */
        final static String TABLE_WEIGHT = "weight";

        /**
         * 位置
         */
        final static String TABLE_POSITION = "position";

        /**
         * 惯用脚 (0: 未知， 1：左脚；2：右脚；3：左右脚)
         */
        final static String TABLE_HABITUAL_FEET = "habitual_feet";

        /**
         * 出生地
         */
        final static String TABLE_HOMETOWN = "hometown";

        /**
         * 国籍
         */
        final static String TABLE_NATIONALITY = " nationality";

        /**
         * 俱乐部
         */
        final static String TABLE_FOOTBALL_CLUB = " football_club";

        /**
         * 身价(英镑)
         */
        final static String TABLE_VALUE = "value";

    }

    /**
     * 足球运动员信息表创建语句
     */
    static final String CREATE_FOOTBALLER_TABLE = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(TABLE_FOOTBALLER)
            .append(" (")
            .append(FootballerColumns._ID).append(" INTEGER PRIMARY KEY,")
            .append(FootballerColumns.TABLE_NAME).append(" TEXT,")
            .append(FootballerColumns.TABLE_AGE).append(" TEXT,")
            .append(FootballerColumns.TABLE_HEIGHT).append(" INTEGER,")
            .append(FootballerColumns.TABLE_WEIGHT).append(" INTEGER,")
            .append(FootballerColumns.TABLE_POSITION).append(" TEXT,")
            .append(FootballerColumns.TABLE_HABITUAL_FEET).append(" INTEGER,")
            .append(FootballerColumns.TABLE_HOMETOWN).append(" TEXT,")
            .append(FootballerColumns.TABLE_NATIONALITY).append(" TEXT,")
            .append(FootballerColumns.TABLE_FOOTBALL_CLUB).append(" TEXT,")
            .append(FootballerColumns.TABLE_VALUE).append(" INTEGER")
            .append(")")
            .toString();



}
