package com.scy.mykotlinnews.news.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.getNews
import com.scy.mykotlinnews.news.Adapter_News
import com.scy.mykotlinnews.news.entity.Bean_News
import com.scy.mykotlinnews.news.entity.impl.GetBeanNews
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 17:24
 * 本类描述:
 */
open class Fragment_Top : Fragment(), GetBeanNews, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapterNews: Adapter_News
    private lateinit var lists: ArrayList<Bean_News.ResultBean.DataBean>
    override fun getBeanNews(beanNews: Bean_News) {
        activity.runOnUiThread {
            for (data in beanNews.result!!.data!!) {
                if (TextUtils.isEmpty(data.thumbnail_pic_s03)) {
                    data.imageGson = View.VISIBLE
                    data.imageLinearGson = View.GONE
                } else {
                    data.imageGson = View.GONE
                    data.imageLinearGson = View.VISIBLE
                }
            }
            updateUI(beanNews)
        }
    }

    private fun updateUI(beanNews: Bean_News) {
        if (firstRefresh) {  //下拉刷新
            adapterNews.setRefreshDataList(beanNews.result!!.data!!)
        } else {
            adapterNews.setAddDataList(beanNews.result!!.data!!)
        }
        fragmentSwipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        if (!firstRefresh) {
            getNews("top", this)
            firstRefresh = true
        }
    }

    var firstRefresh = true
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && firstRefresh) {
            if (fragmentSwipeRefreshLayout != null) fragmentSwipeRefreshLayout.isRefreshing = true
            getNews("top", this)
            firstRefresh = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //初始化RecyclerView
        fragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        lists = ArrayList<Bean_News.ResultBean.DataBean>()
        adapterNews = Adapter_News(context, lists)
        fragmentRecyclerView.adapter = adapterNews

        //初始化SwipeRefreshLayout
        fragmentSwipeRefreshLayout.setOnRefreshListener(this)
    }
}