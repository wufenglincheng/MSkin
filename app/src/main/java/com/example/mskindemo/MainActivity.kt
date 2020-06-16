package com.example.mskindemo

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.darkliu.mskin.SkinManager
import com.example.mskindemo.base.BaseSkinActivity
import com.example.mskindemo.ui.discovery.DiscoveryFragment
import com.example.mskindemo.ui.index.IndexFragment
import com.example.mskindemo.ui.me.ProfileFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : BaseSkinActivity() {
  private val TAG = arrayOf("home", "discovery", "profile")
  private val INDICATOR = arrayOf("Home", "Discovery", "Profile")
  private val FRAGMENT = arrayOf(IndexFragment::class.java, DiscoveryFragment::class.java, ProfileFragment::class.java)

  @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    onThemeChange(false)
    tabHost.setup(this, supportFragmentManager, R.id.container)
    tabHost.tabWidget.visibility = View.GONE
    (0 until 3).forEach {
      tabHost.addTab(tabHost.newTabSpec(TAG[it]).setIndicator(INDICATOR[it]), FRAGMENT[it], null)
    }
    tabHost.currentTab = 0
    bottom.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.indexFragment -> tabHost.currentTab = 0
        R.id.discoveryFragment -> tabHost.currentTab = 1
        R.id.profileFragment -> tabHost.currentTab = 2
      }
      return@setOnNavigationItemSelectedListener true
    }
  }

  override fun onThemeChange(isFromUser: Boolean) {
    val defaultColor = SkinManager.resource().getColor(this, R.color.skin_bottom_default_color)
    val focusColor = SkinManager.resource().getColor(this, R.color.skin_focus)
    val colorList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(-android.R.attr.state_checked), intArrayOf()), intArrayOf(focusColor, defaultColor, defaultColor))
    bottom.itemTextColor = colorList
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> {
        // 暗黑模式已开启
        SkinManager.instance().switchToExpandTheme(MyApplication.THEME_NIGHT, null)
      }
      Configuration.UI_MODE_NIGHT_NO -> {
        // 暗黑模式已关闭
        SkinManager.instance().switchToDefaultTheme()
      }
    }
  }

  fun defaultTheme(view: View) {
    SkinManager.instance().switchToDefaultTheme()
  }

  fun nightTheme(view: View) {
    SkinManager.instance().switchToExpandTheme(MyApplication.THEME_NIGHT, null)
  }

  fun color1Theme(view: View) {
    SkinManager.instance().switchToExpandTheme(MyApplication.THEME_ONE, null)
  }

  fun color2Theme(view: View) {
    SkinManager.instance().switchToExpandTheme(MyApplication.THEME_TWO, null)
  }

  fun color3Theme(view: View) {
    SkinManager.instance().switchToExpandTheme(MyApplication.THEME_THREE, null)
  }


}
