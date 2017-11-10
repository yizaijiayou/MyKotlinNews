package com.scy.mykotlinnews.news

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.news.entity.Bean_News
import com.scy.mykotlinnews.toast

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 14:25
 * 本类描述:
 */
class Adapter_News constructor(private val context: Context,private var lists: ArrayList<Bean_News.ResultBean.DataBean>) : RecyclerView.Adapter<Adapter_News.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Adapter_News.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, null))
    }

    fun setAddDataList(lists: ArrayList<Bean_News.ResultBean.DataBean>) {
        for (i in 0 until lists.size) {
            this.lists.add(lists[i])
        }
        notifyDataSetChanged()
    }

    fun setRefreshDataList(lists: ArrayList<Bean_News.ResultBean.DataBean>) {
        this.lists.clear()
        this.lists = lists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: Adapter_News.ViewHolder?, position: Int) {
        if (holder != null) {
            val bean = lists[position]
            holder.title.text = bean.title
            holder.report.text = bean.author_name
            holder.time.text = bean.date

            holder.image1.visibility = bean.imageGson
            holder.imageLinear.visibility = bean.imageLinearGson

            Glide.with(context).load(bean.thumbnail_pic_s).into(holder.image1)
            Glide.with(context).load(bean.thumbnail_pic_s).into(holder.image2)
            Glide.with(context).load(bean.thumbnail_pic_s02).into(holder.image3)
            Glide.with(context).load(bean.thumbnail_pic_s03).into(holder.image4)

            holder.itemLinear.setOnClickListener { context.startActivity(Intent(context,Activity_DeatilsNews::class.java).putExtra("url",bean.url)) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.news_title)
        val report: TextView = itemView.findViewById(R.id.news_report)
        val time: TextView = itemView.findViewById(R.id.news_time)
        val image1: ImageView = itemView.findViewById(R.id.news_image1)
        val image2: ImageView = itemView.findViewById(R.id.news_image2)
        val image3: ImageView = itemView.findViewById(R.id.news_image3)
        val image4: ImageView = itemView.findViewById(R.id.news_image4)
        val imageLinear: LinearLayout = itemView.findViewById(R.id.news_imageLinear)
        val itemLinear: LinearLayout = itemView.findViewById(R.id.news_item)
    }
}