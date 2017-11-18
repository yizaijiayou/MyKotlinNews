package com.scy.mykotlinnews.news

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
class Adapter_News constructor(private val context: Context, private var lists: ArrayList<Bean_News.ResultBean.DataBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val REVYVLER_ITEM = 0
    private val REVYVLER_FOOT = 1
    private lateinit var footView: View

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = if (viewType == REVYVLER_ITEM)
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, null))
    else {
        footView = LayoutInflater.from(context).inflate(R.layout.foot_view, parent, false)
        FootHolder(footView)
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

    override fun getItemCount(): Int = lists.size + 1

    override fun getItemViewType(position: Int): Int = if (position == itemCount - 1)
        REVYVLER_FOOT
    else
        REVYVLER_ITEM

    fun goneFoot() {
        val layoutParams = footView.layoutParams
        layoutParams.height = 0
        footView.layoutParams = layoutParams
    }

    fun addFoot() {
        val layoutParams = footView.layoutParams
        layoutParams.height = 50
        footView.layoutParams = layoutParams
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder != null) {
            if (holder is ViewHolder) {
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

                holder.itemLinear.setOnClickListener { context.startActivity(Intent(context, Activity_DeatilsNews::class.java).putExtra("url", bean.url)) }
            } else if (holder is FootHolder) {
                if (finishFootLayout) {
                    finishFootLayout = false
                    holder.relativeLayout.visibility = View.GONE
                } else {
                    holder.relativeLayout.visibility = View.VISIBLE
                }
            }
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

    private var finishFootLayout = false

    class FootHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relativeLayout: RelativeLayout = itemView.findViewById(R.id.footRelativeLayout)
    }
}