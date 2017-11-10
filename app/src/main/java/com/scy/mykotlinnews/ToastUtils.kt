package com.scy.mykotlinnews

import android.content.Context
import android.widget.Toast

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/6 9:23
 * 本类描述:
 */
fun Context.toast( msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}