package com.scy.mykotlinnews.base

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 9:24
 * 本类描述:
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        var appContext: Context? = null
    }
}