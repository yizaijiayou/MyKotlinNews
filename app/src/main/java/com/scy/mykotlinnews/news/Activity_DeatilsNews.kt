package com.scy.mykotlinnews.news

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.toast
import kotlinx.android.synthetic.main.activity_details.*

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/7 16:20
 * 本类描述:
 */
class Activity_DeatilsNews:AppCompatActivity(){
    lateinit var url:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        url = intent.getStringExtra("url")

        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.color_white))
        toolbar.navigationIcon = ContextCompat.getDrawable(this,R.drawable.guide_left_true)
        setSupportActionBar(toolbar) //有时候需要这句话才回出现navagationIcon,要放在setNavigationOnClickListener才可以启动setNavigationOnClickListener的方法
        toolbar.setNavigationOnClickListener { finish() }

        val progressBar = ProgressBar(this)
        val loadingDialog = AlertDialog.Builder(this).setView(progressBar).create()

        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webView.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingDialog.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingDialog.dismiss()
            }
        }
    }
}