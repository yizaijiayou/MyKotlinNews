package com.scy.mykotlinnews

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.bumptech.glide.Glide
import com.scy.mykotlinnews.base.BaseApplication
import com.scy.mykotlinnews.news.Adapter_Fragment
import com.scy.mykotlinnews.news.fragment.Fragment_Science
import com.scy.mykotlinnews.news.fragment.Fragment_Sport
import com.scy.mykotlinnews.news.fragment.Fragment_Top
import com.scy.mykotlinnews.setting.Activity_UserCenter
import com.scy.mykotlinnews.setting.bean.Weather
import com.scy.mykotlinnews.setting.bean.impl.WeatherImpl
import com.scy.mykotlinnews.setting.sql.SqlUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    val tabColors = intArrayOf(R.color.tabBackground1, R.color.tabBackground2, R.color.tabBackground3)   //状态栏颜色与tablayout颜色
    val toolBarColors = intArrayOf(R.color.toolBarBackground1, R.color.toolBarBackground2, R.color.toolBarBackground3) //toolBar颜色
    val iconBackgroundArr = intArrayOf(R.drawable.side_nav_bar, R.drawable.side_nav_bar2, R.drawable.side_nav_bar3) //用户中心颜色
    var tabPosition: Int = 0
    lateinit var iconBackground: LinearLayout
    lateinit var iconImage: ImageView
    lateinit var iconWeatherImage: ImageView
    lateinit var iconName: TextView
    lateinit var iconWeatherText: TextView
    lateinit var iconWeatherD: TextView
    lateinit var iconAutograph: TextView
    lateinit var iconLocation: TextView
    lateinit var iconProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        //drawer左侧滑栏的单击事件
        nav_view.setNavigationItemSelectedListener(this)
        val view = nav_view.inflateHeaderView(R.layout.nav_header_main)
        iconBackground = view.findViewById<LinearLayout>(R.id.header_main)
        iconImage = view.findViewById<ImageView>(R.id.imageView)
        iconWeatherImage = view.findViewById<ImageView>(R.id.iconWeatherImage)
        iconAutograph = view.findViewById<TextView>(R.id.textView)
        iconName = view.findViewById<TextView>(R.id.name)
        iconLocation = view.findViewById<TextView>(R.id.locationText)
        iconWeatherText = view.findViewById<TextView>(R.id.iconWeatherText)
        iconWeatherD = view.findViewById<TextView>(R.id.iconWeatherD)
        iconProgressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        iconBackground.setOnClickListener(this)
        iconImage.setOnClickListener(this)
        iconName.setOnClickListener(this)
        iconAutograph.setOnClickListener(this)
        iconLocation.setOnClickListener(this)

        //TabLayout和viewPager配置
        val titles = ArrayList<String>()
        titles.add(getString(R.string.action_mian_tab_top))
        titles.add(getString(R.string.action_mian_tab_sports))
        titles.add(getString(R.string.action_mian_tab_science))
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]))
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]))
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]))

        val fragments = ArrayList<Fragment>()
        fragments.add(Fragment_Top())
        fragments.add(Fragment_Sport())
        fragments.add(Fragment_Science())

        //结合TabLayout与ViewPager
        viewPager.adapter = Adapter_Fragment(this, fragments, titles, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)   //为了能让单击tabLayout的Item不出现切换效果，但是需要保留左右切换滑动，就必须把这句话放在addOnTabSelectedListener之前

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabPosition = tab.position
                collapsingToolbarLayout.setBackgroundResource(tabColors[tab.position])
                toolbar.setBackgroundResource(toolBarColors[tab.position])
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.statusBarColor = ContextCompat.getColor(activity, tabColors[tab.position])
                iconBackground.setBackgroundResource(iconBackgroundArr[tab.position])
                viewPager.setCurrentItem(tab.position, false)
            }
        })

        //在toolbar上显示连接drawer的动画图标
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //定位
        getLocation()

        //默认选中第一个
        nav_view.menu.getItem(0).isChecked = true

    }

    companion object {
        var updateUserMessgae: Boolean = true
    }

    override fun onResume() {
        super.onResume()
        if (updateUserMessgae) {
            val user = SqlUtils.getInstance.checkUser()
            try {
                Picasso.with(this).load(File(user.image)).error(R.mipmap.ic_launcher_round).into(iconImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            iconName.text = user.name
            iconAutograph.text = user.autograph
            updateUserMessgae = false
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageView, R.id.textView, R.id.name -> {  //头像，姓名,签名
                startActivity(Intent(this, Activity_UserCenter::class.java).putExtra("bg", tabPosition))
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.header_main -> {
                toast("我是整一块区域")
            }
            R.id.locationText -> {
                iconProgressBar.visibility = View.VISIBLE
                iconLocation.visibility = View.GONE
                getLocation()
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
//                toast("我是第一行")
            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
            R.id.out -> {
                System.exit(0)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    var firstTime: Long = 0
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                val secondTime = System.currentTimeMillis()
                if (secondTime - firstTime < 2000) {
                    System.exit(0)
                    return true
                } else {
                    Toast.makeText(this, "再按一次退出程序", 2000).show()
                    firstTime = secondTime
                }
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            location()
        }
    }

    private fun location() {
        val client = AMapLocationClient(this)
        val option = AMapLocationClientOption()
        option.isOnceLocation = true
        client.setLocationOption(option)
        client.setLocationListener { aMapLocation ->
            try {
                iconLocation.text = aMapLocation.province + aMapLocation.city
                //获取天气
                getweather(aMapLocation.city)
            }catch (e:Exception){
                e.printStackTrace()
                iconLocation.text = getString(R.string.locationText)
            }

            iconProgressBar.visibility = View.GONE
            iconLocation.visibility = View.VISIBLE

            client.stopLocation()
            client.onDestroy()
        }
        client.startLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var perssionB = true
        if (requestCode == 0) {
            (0 until grantResults.size)
                    .filter { grantResults[it] != PackageManager.PERMISSION_GRANTED }
                    .forEach { perssionB = false }
            if (perssionB) {
                location()
            }
        }
    }

    /**
     * 获取天气
     */
    private fun getweather(city: String) {
        getWeather(city,object: WeatherImpl {
            override fun getWeather(weather: Weather) {
                runOnUiThread {
                    changeImage(weather.results?.get(0)?.now?.code)
                    iconWeatherText.text = weather.results?.get(0)?.now?.text
                    iconWeatherD.text = weather.results?.get(0)?.now?.temperature + "℃"
                }
            }
        })
    }

    private fun changeImage(text: String?) {
        when(text){
            "0"->{ Picasso.with(this).load(R.mipmap.a).error(R.mipmap.aaa).into(iconWeatherImage)}
            "1"->{ Picasso.with(this).load(R.mipmap.b).error(R.mipmap.aaa).into(iconWeatherImage)}
            "2"->{ Picasso.with(this).load(R.mipmap.c).error(R.mipmap.aaa).into(iconWeatherImage)}
            "3"->{ Picasso.with(this).load(R.mipmap.d).error(R.mipmap.aaa).into(iconWeatherImage)}
            "4"->{ Picasso.with(this).load(R.mipmap.e).error(R.mipmap.aaa).into(iconWeatherImage)}
            "5"->{ Picasso.with(this).load(R.mipmap.f).error(R.mipmap.aaa).into(iconWeatherImage)}
            "6"->{ Picasso.with(this).load(R.mipmap.g).error(R.mipmap.aaa).into(iconWeatherImage)}
            "7"->{ Picasso.with(this).load(R.mipmap.h).error(R.mipmap.aaa).into(iconWeatherImage)}
            "8"->{ Picasso.with(this).load(R.mipmap.i).error(R.mipmap.aaa).into(iconWeatherImage)}
            "9"->{ Picasso.with(this).load(R.mipmap.j).error(R.mipmap.aaa).into(iconWeatherImage)}
            "10"->{ Picasso.with(this).load(R.mipmap.k).error(R.mipmap.aaa).into(iconWeatherImage)}
            "11"->{ Picasso.with(this).load(R.mipmap.l).error(R.mipmap.aaa).into(iconWeatherImage)}
            "12"->{ Picasso.with(this).load(R.mipmap.m).error(R.mipmap.aaa).into(iconWeatherImage)}
            "13"->{ Picasso.with(this).load(R.mipmap.n).error(R.mipmap.aaa).into(iconWeatherImage)}
            "14"->{ Picasso.with(this).load(R.mipmap.o).error(R.mipmap.aaa).into(iconWeatherImage)}
            "15"->{ Picasso.with(this).load(R.mipmap.p).error(R.mipmap.aaa).into(iconWeatherImage)}
            "16"->{ Picasso.with(this).load(R.mipmap.q).error(R.mipmap.aaa).into(iconWeatherImage)}
            "17"->{ Picasso.with(this).load(R.mipmap.r).error(R.mipmap.aaa).into(iconWeatherImage)}
            "18"->{ Picasso.with(this).load(R.mipmap.s).error(R.mipmap.aaa).into(iconWeatherImage)}
            "19"->{ Picasso.with(this).load(R.mipmap.t).error(R.mipmap.aaa).into(iconWeatherImage)}
            "20"->{ Picasso.with(this).load(R.mipmap.u).error(R.mipmap.aaa).into(iconWeatherImage)}
            "21"->{ Picasso.with(this).load(R.mipmap.v).error(R.mipmap.aaa).into(iconWeatherImage)}
            "22"->{ Picasso.with(this).load(R.mipmap.w).error(R.mipmap.aaa).into(iconWeatherImage)}
            "23"->{ Picasso.with(this).load(R.mipmap.x).error(R.mipmap.aaa).into(iconWeatherImage)}
            "24"->{ Picasso.with(this).load(R.mipmap.y).error(R.mipmap.aaa).into(iconWeatherImage)}
            "25"->{ Picasso.with(this).load(R.mipmap.z).error(R.mipmap.aaa).into(iconWeatherImage)}
            "26"->{ Picasso.with(this).load(R.mipmap.aa).error(R.mipmap.aaa).into(iconWeatherImage)}
            "27"->{ Picasso.with(this).load(R.mipmap.bb).error(R.mipmap.aaa).into(iconWeatherImage)}
            "28"->{ Picasso.with(this).load(R.mipmap.cc).error(R.mipmap.aaa).into(iconWeatherImage)}
            "29"->{ Picasso.with(this).load(R.mipmap.dd).error(R.mipmap.aaa).into(iconWeatherImage)}
            "30"->{ Picasso.with(this).load(R.mipmap.ee).error(R.mipmap.aaa).into(iconWeatherImage)}
            "31"->{ Picasso.with(this).load(R.mipmap.ff).error(R.mipmap.aaa).into(iconWeatherImage)}
            "32"->{ Picasso.with(this).load(R.mipmap.gg).error(R.mipmap.aaa).into(iconWeatherImage)}
            "33"->{ Picasso.with(this).load(R.mipmap.hh).error(R.mipmap.aaa).into(iconWeatherImage)}
            "34"->{ Picasso.with(this).load(R.mipmap.ii).error(R.mipmap.aaa).into(iconWeatherImage)}
            "35"->{ Picasso.with(this).load(R.mipmap.jj).error(R.mipmap.aaa).into(iconWeatherImage)}
            "36"->{ Picasso.with(this).load(R.mipmap.kk).error(R.mipmap.aaa).into(iconWeatherImage)}
            "37"->{ Picasso.with(this).load(R.mipmap.ll).error(R.mipmap.aaa).into(iconWeatherImage)}
            "38"->{ Picasso.with(this).load(R.mipmap.mm).error(R.mipmap.aaa).into(iconWeatherImage)}
            "99"->{ Picasso.with(this).load(R.mipmap.aaa).error(R.mipmap.aaa).into(iconWeatherImage)}
        }
    }

    val activity: Activity = this
}
