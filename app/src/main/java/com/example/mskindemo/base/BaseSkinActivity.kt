package com.example.mskindemo.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.darkliu.mskin.IThemeUpdate
import com.darkliu.mskin.SkinContext
import com.darkliu.mskin.SkinManager
import com.example.mskindemo.R


/**
 * Created by liuting on 18/8/2.
 */
abstract class BaseSkinActivity : AppCompatActivity(), IThemeUpdate {

  private lateinit var skinAdapter: SkinContext
  override fun onCreate(savedInstanceState: Bundle?) {
    skinAdapter = SkinContext.instance(this, delegate)
    SkinManager.instance().attach(this)
    window.decorView.background = null
    super.onCreate(savedInstanceState)
    fullTransparent()
  }

  override fun onDestroy() {
    SkinManager.instance().detach(this)
    super.onDestroy()
  }

  override fun onThemeUpdate(isChange: Boolean) {
    changeStatusBar(SkinManager.instance().isDefault)
    onThemeChange(isChange)
    supportFragmentManager.fragments.forEach {
      if (it is BaseSkinFragment) {
        it.onThemeUpdate(isChange)
      }
    }
  }

  private fun fullTransparent() {
    if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      //虚拟键盘也透明
      //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  private fun halfTransparent() {
    if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
      val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      window.decorView.systemUiVisibility = option
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      //虚拟键盘也透明
      // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }

  private fun changeStatusBar(dark: Boolean) {
    if ((Build.FINGERPRINT.toLowerCase().contains("xiaomi") || Build.FINGERPRINT.toLowerCase().contains("miui")) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //小米
      setMiuiStatusBarLightMode(this, dark)
    } else if (Build.FINGERPRINT.toLowerCase().contains("meizu") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //魅族
      setMeizuStatusBarLightMode(this, dark)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.decorView.systemUiVisibility = if (dark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_VISIBLE
//            (findViewById(R.id.container_fragment_container) as CoordinatorLayout).setStatusBarBackgroundColor(Res.getColor(R.color.top_tabbar_bg))
    }
  }

  protected fun skinStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val window = window
      // clear FLAG_TRANSLUCENT_STATUS flag:
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.statusBarColor = SkinManager.resource().getColor(this,R.color.skin_toolbar_bg)
    }
  }

  abstract fun onThemeChange(isFromUser: Boolean)

  /**
   * 设置状态栏字体图标为深色，需要MIUI6以上
   *
   * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  fun setMiuiStatusBarLightMode(activity: Activity, isFontColorDark: Boolean): Boolean {
    val window = activity.window
    var result = false
    if (window != null) {
      val clazz = window.javaClass
      try {
        var darkModeFlag = 0
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
        if (isFontColorDark) {
          extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
        } else {
          extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
        }
        //http://www.miui.com/thread-8946673-1-1.html 小米更新为了系统方法，这里需要做适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
          window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        result = true
      } catch (e: Exception) {
        e.printStackTrace()
      }

    }
    return result
  }

  /**
   * 设置状态栏图标为深色和魅族特定的文字风格
   * 可以用来判断是否为Flyme用户
   *
   * @param isFontColorDark 是否把状态栏字体及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  fun setMeizuStatusBarLightMode(activity: Activity, isFontColorDark: Boolean): Boolean {
    val window = activity.window
    var result = false
    if (window != null) {
      try {
        val lp = window.attributes
        val darkFlag = WindowManager.LayoutParams::class.java
          .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java
          .getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(lp)
        if (isFontColorDark) {
          value = value or bit
        } else {
          value = value and bit.inv()
        }
        meizuFlags.setInt(lp, value)
        window.attributes = lp
        result = true
      } catch (e: Exception) {
        e.printStackTrace()
      }

    }
    return result
  }
}