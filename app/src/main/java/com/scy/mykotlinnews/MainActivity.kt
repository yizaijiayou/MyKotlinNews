package com.scy.mykotlinnews

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.scy.mykotlinnews.base.BaseApplication
import com.scy.mykotlinnews.news.Adapter_Fragment
import com.scy.mykotlinnews.news.fragment.Fragment_Science
import com.scy.mykotlinnews.news.fragment.Fragment_Sport
import com.scy.mykotlinnews.news.fragment.Fragment_Top
import com.scy.mykotlinnews.setting.Activity_UserCenter
import com.scy.mykotlinnews.setting.sql.SqlUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    val tabColors = intArrayOf(R.color.tabBackground1, R.color.tabBackground2, R.color.tabBackground3)   //状态栏颜色与tablayout颜色
    val toolBarColors = intArrayOf(R.color.toolBarBackground1, R.color.toolBarBackground2, R.color.toolBarBackground3) //toolBar颜色
    val iconBackgroundArr = intArrayOf(R.drawable.side_nav_bar, R.drawable.side_nav_bar2, R.drawable.side_nav_bar3) //用户中心颜色
    var tabPosition: Int = 0
    lateinit var iconBackground: LinearLayout
    lateinit var iconImage: ImageView
    lateinit var iconName: TextView
    lateinit var iconAutograph: TextView

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
        iconAutograph = view.findViewById<TextView>(R.id.textView)
        iconName = view.findViewById<TextView>(R.id.name)

        iconBackground.setOnClickListener(this)
        iconImage.setOnClickListener(this)
        iconName.setOnClickListener(this)
        iconAutograph.setOnClickListener(this)

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
                Picasso.with(this).load(user.image).error(R.mipmap.ic_launcher_round).into(iconImage)
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
            }
            R.id.header_main -> {
                toast("我是整一块区域")
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                toast("我是第一行")
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

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

    val activity: Activity = this
}
