package com.scy.mykotlinnews

import android.util.Log
import com.google.gson.Gson
import com.scy.mykotlinnews.news.entity.Bean_News
import com.scy.mykotlinnews.news.entity.impl.GetBeanNews
import com.scy.mykotlinnews.setting.bean.Weather
import com.scy.mykotlinnews.setting.bean.impl.WeatherImpl
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/7 13:43
 * 本类描述:
 */
val ip = "http://toutiao-ali.juheapi.com/toutiao/index?type="

/**
 * 1.获取新闻列表
 */
fun getNews(type: String,beanNews: GetBeanNews) {
    Okhttp3Utils.instance.getNews(ip + type, object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val str = response.body()?.string()
            Log.d("Nework", "---->" + str)
            beanNews.getBeanNews(Gson().fromJson<Bean_News>(str,Bean_News::class.java))
        }

        override fun onFailure(call: Call?, e: IOException?) {
            Log.d("Nework", "---->" + e?.message)
        }
    })
}

/**
 * 2.获取天气
 */
fun getWeather(city:String,weatherImpl: WeatherImpl){
    Okhttp3Utils.instance.get("https://api.seniverse.com/v3/weather/now.json?key=jlxdn9xqvruodfnt&location=$city&language=zh-Hans&unit=c",object :Callback{
        override fun onFailure(call: Call?, e: IOException?) {
            Log.d("Nework", "---->" + e?.message)
        }

        override fun onResponse(call: Call?, response: Response) {
            val str = response.body()?.string()
            Log.d("Nework", "---->" + str)
            try {
                weatherImpl.getWeather(Gson().fromJson<Weather>(str,Weather::class.java))
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    })
}
