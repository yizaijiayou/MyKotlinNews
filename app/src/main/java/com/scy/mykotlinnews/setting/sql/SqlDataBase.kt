package com.scy.mykotlinnews.setting.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.R.id.name

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/8 9:22
 * 本类描述:
 */
class SqlDataBase(val context: Context, name: String, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val sqlUser = "CREATE TABLE IF NOT EXISTS "+ context.getString(R.string.sqlUser) +"(" +
                "_id INTEGER PRIMARY KEY NOT NULL," +   //主键如果是integer类型的默认是自增长的
                "name VARCHAR," +
                "autograph VARCHAR," +
                "image VARCHAR," +
                "sex VARCHAR);"
        db.execSQL(sqlUser)

        try {
            val values = ContentValues()
            values.put("_id", "0")
            values.put("name", "暂无用户")
            values.put("sex", "")
            values.put("autograph", "")
            values.put("image", "")
            db.insert(context.getString(R.string.sqlUser),null,values)
        }catch ( e:Exception){
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS "+ context.getString(R.string.sqlUser))
        onCreate(db)
    }
}