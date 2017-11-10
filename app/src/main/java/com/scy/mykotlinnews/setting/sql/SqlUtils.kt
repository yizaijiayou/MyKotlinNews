package com.scy.mykotlinnews.setting.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.bumptech.glide.Glide.init
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.R.id.name
import com.scy.mykotlinnews.base.BaseApplication
import com.scy.mykotlinnews.setting.bean.User

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/8 9:49
 * 本类描述:
 */
class SqlUtils {
    companion object {
        private var sqlUtils: SqlUtils? = null
        val getInstance: SqlUtils
            get() {
                if (sqlUtils == null)
                    sqlUtils = SqlUtils()
                return sqlUtils as SqlUtils
            }
    }

    private val sqliteDataBase: SQLiteDatabase
    private val context: Context
    private val id: Int = SharepreferencesUtils.getInstance.getString("id").toInt()


    private val user: String

    init {
        context = BaseApplication.appContext!!
        user = context.getString(R.string.sqlUser)
        val sqlDataBase = SqlDataBase(context, "kotlinNews.db", 1)
        sqliteDataBase = sqlDataBase.readableDatabase
    }

    //---------------------------------------user-------------------------------------
    fun addUser(name: String, image: String, sex: String, autograph: String) {
        val values = ContentValues()
        values.put("name", name)
        values.put("sex", sex)
        values.put("autograph", autograph)
        values.put("image", image)
        sqliteDataBase.insert(user, null, values)
    }

    fun deleteUser() {
        if (id != 0)
            sqliteDataBase.execSQL("delete from $user where _id=$id")
    }

    fun updateUser(value: String, updateStr: String) {
        val values = ContentValues()
        values.put(value, updateStr)
        sqliteDataBase.update(user, values, "_id=?", arrayOf(id.toString()))
    }

    fun checkUser(): User {
        val all = sqliteDataBase.rawQuery("select * from $user ", null)
        Log.v("数据库的全部数目", "-------->" + all.count)
        var allId = ""
        while (all.moveToNext()) {
            allId = allId + all.getString(all.getColumnIndex("_id")) + "<--->"
        }
        Log.v("数据库的Id的综合", "-------->" + allId)
        all.close()


        val cursor = sqliteDataBase.rawQuery("select * from $user where _id=$id", null)
        val user = User()
        cursor.moveToNext()
        if (cursor.isFirst) {
            user.id = cursor.getString(cursor.getColumnIndex("_id"))
            user.name = cursor.getString(cursor.getColumnIndex("name"))
            user.image = cursor.getString(cursor.getColumnIndex("image"))
            user.sex = cursor.getString(cursor.getColumnIndex("sex"))
            user.autograph = cursor.getString(cursor.getColumnIndex("autograph"))
        }
        cursor.close()
        return user
    }
}