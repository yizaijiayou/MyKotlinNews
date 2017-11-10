package com.scy.mykotlinnews.setting.sql

import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.Glide.init
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.base.BaseApplication

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/8 11:03
 * 本类描述:
 */
class SharepreferencesUtils {
    companion object {
        private var sharepreferencesUtils:SharepreferencesUtils? = null
        val getInstance:SharepreferencesUtils
        get(){
            if (sharepreferencesUtils == null)
                sharepreferencesUtils = SharepreferencesUtils()
            return sharepreferencesUtils as SharepreferencesUtils
        }
    }

    private val context:Context
    private val editor :SharedPreferences.Editor
    private val sharepreferences :SharedPreferences
    init{
        context = BaseApplication.appContext!!
        sharepreferences = context.getSharedPreferences(context.getString(R.string.app_name),0)
        editor = sharepreferences.edit()
    }

    fun putString(value:String,msg:String){
        editor.putString(value,msg)
        editor.apply()
        editor.commit()
    }

    fun getString(value:String):String{
        return sharepreferences.getString(value,"0")
    }
}