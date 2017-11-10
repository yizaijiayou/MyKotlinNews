package com.scy.mykotlinnews.news.entity

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.scy.mykotlinnews.news.entity.impl.GetBeanNews

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/7 14:23
 * 本类描述:
 */

class Bean_News {
    var reason: String? = ""
    var result: ResultBean? = null
    var error_code: Int = 0

    class ResultBean {
        var stat: String? = ""
        var data: ArrayList<DataBean>? = null

        class DataBean {
            var uniquekey: String? = ""
            var title: String? = ""
            var date: String? = ""
            var category: String? = ""
            var author_name: String? = ""
            var url: String? = ""
            var thumbnail_pic_s: String? = ""
            var thumbnail_pic_s02: String? = ""
            var thumbnail_pic_s03: String? = ""

            var imageGson = View.GONE
            var imageLinearGson = View.GONE
        }
    }
}
