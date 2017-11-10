package com.scy.mykotlinnews.setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.scy.mykotlinnews.MainActivity
import com.scy.mykotlinnews.R
import com.scy.mykotlinnews.setting.sql.SqlUtils
import com.scy.mykotlinnews.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_usercenter.*
import java.io.File
import java.lang.Exception
import java.net.URI

/**
 * 项 目 名: MyKotlinNews
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/8 9:21
 * 本类描述:
 */
class Activity_UserCenter : AppCompatActivity() {

    val tabColors = intArrayOf(R.color.tabBackground1, R.color.tabBackground2, R.color.tabBackground3)   //状态栏颜色与tablayout颜色
    val toolBarColors = intArrayOf(R.color.toolBarBackground1, R.color.toolBarBackground2, R.color.toolBarBackground3) //toolBar颜色
    val iconBackgroundArr = intArrayOf(R.drawable.side_nav_bar, R.drawable.side_nav_bar2, R.drawable.side_nav_bar3) //用户中心颜色

    lateinit var userAutograph: TextView
    lateinit var userName: TextView
    lateinit var userIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usercenter)
        userName = findViewById(R.id.userCenter_name)
        userAutograph = findViewById(R.id.userCenter_autograph)
        userIcon = findViewById(R.id.userCenter_icon)

        //设置用户信息
        val user = SqlUtils.getInstance.checkUser()
        if (user.name != "暂无用户") {
            try {
            Picasso.with(this).load(user.image).error(R.mipmap.ic_launcher_round).into(userIcon)
            }catch(e:Exception){
                e.printStackTrace()
            }
            userName.text = user.name
            userAutograph.text = user.autograph
        }

        //设置头像和状态栏的背景
        var position = intent.getIntExtra("bg", 0)
        app_bar.setBackgroundResource(toolBarColors[position])
        toolbar.setBackgroundResource(toolBarColors[position])
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.statusBarColor = ContextCompat.getColor(this, tabColors[position])
    }

    fun getClick(view: View) {
        when (view.id) {
            R.id.userCenter_autograph -> {
                startDialog(16, getString(R.string.dialogInputAutographTitle), getString(R.string.dialogInputAutographHint))
            }
            R.id.userCenter_icon -> {
                picture()
            }
            R.id.userCenter_name -> {
                startDialog(7, getString(R.string.dialogInputNameTitle), getString(R.string.dialogInputNameHint))
            }
            R.id.userCenter_back -> {
                onBackPressed()
            }
        }
    }

    fun startDialog(length: Int, title: String, hint: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null)
        val editText = view.findViewById<EditText>(R.id.dialog_editText)
        editText.hint = hint
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
        val alerDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.confirm)) { dialogInterface, i ->
                    val str = editText.text.toString()
                    if (!TextUtils.isEmpty(str)) {
                        if (length == 7) {  //输入名字
                            SqlUtils.getInstance.updateUser("name", str)
                            userName.text = str
                        } else {
                            SqlUtils.getInstance.updateUser("autograph", str)
                            userAutograph.text = str
                        }
                        MainActivity.updateUserMessgae = true
                    } else {
                        toast(getString(R.string.dialogNotices))
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
        alerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_PICTURE -> {
                    Glide.with(this).load(data.data.toString()).into(userIcon)
                    SqlUtils.getInstance.updateUser("image", data.data.toString())
                    MainActivity.updateUserMessgae = true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPEN_PICTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), OPEN_PICTURE)
            }
        }
    }

    //跳转至图库
    private val OPEN_PICTURE = 0

    fun picture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), OPEN_PICTURE)
        } else {
            startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), OPEN_PICTURE)
        }
    }

    fun getPictureFile(uri: Uri): File {
        val s = uri.toString().split(":")[0]
        return if (s == "file") {
            File(URI(uri.toString()))
        } else {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, proj, null, null, null)
            val colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToNext()
            val filePath = cursor.getString(colum_index)
            cursor.close()
            File(filePath)
        }
    }
}