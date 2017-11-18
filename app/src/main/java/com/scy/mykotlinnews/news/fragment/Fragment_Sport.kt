package com.scy.mykotlinnews.news.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.getNews
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 17:25
 * 本类描述:
 */
class Fragment_Sport : Fragment_Top() {
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && firstRefresh){
            if (fragmentSwipeRefreshLayout != null) fragmentSwipeRefreshLayout.isRefreshing = true
            getNews("tiyu",this)
            firstRefresh = false
        }
    }

    override fun onRefresh() {
        if (!firstRefresh) {
            getNews("tiyu", this)
            firstRefresh = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addMessage = "junshi"
    }
}