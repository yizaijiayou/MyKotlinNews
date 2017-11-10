package com.scy.mykotlinnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.scy.mykotlinnews.news.fragment.Fragment_Top
import kotlinx.android.synthetic.main.activity_text.*

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 18:13
 * 本类描述:
 */
class TestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        button.setOnClickListener {
            TestFile.getInstance.get()
            TestFile.getInstance.get()
            TestFile.getInstance.get()
        }
    }
}