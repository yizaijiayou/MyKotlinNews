package com.scy.mykotlinnews.news

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.scy.mykotlinnews.R

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 17:29
 * 本类描述:
 */
class Adapter_Fragment(context: Context, private val fragments:ArrayList<Fragment>, private val titles : ArrayList<String>, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = fragments.size
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getPageTitle(position: Int): CharSequence = titles[position]
//    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`
}