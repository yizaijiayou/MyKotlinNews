package com.scy.mykotlinnews

import android.util.Log
import com.scy.mykotlinnews.setting.sql.SqlUtils

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/8 14:54
 * 本类描述:
 */
class TestFile {
    companion object {
        private var sqlUtils: TestFile? = null
        val getInstance: TestFile
            get() {
                if (sqlUtils == null)
                    sqlUtils = TestFile()
                return sqlUtils as TestFile
            }
    }
    init {
        Log.d("TestFile","-------------------------")
    }
    fun get(){
        Log.d("TestFile","*************************")
    }
}